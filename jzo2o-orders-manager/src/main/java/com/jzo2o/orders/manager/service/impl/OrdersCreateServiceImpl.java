package com.jzo2o.orders.manager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.customer.AddressBookApi;
import com.jzo2o.api.customer.InstitutionStaffApi;
import com.jzo2o.api.customer.ServeProviderApi;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.api.customer.dto.response.InstitutionStaffResDTO;
import com.jzo2o.api.customer.dto.response.ServeProviderResDTO;
import com.jzo2o.api.foundations.ServeApi;
import com.jzo2o.api.foundations.dto.response.ServeAggregationResDTO;
import com.jzo2o.api.market.CouponApi;
import com.jzo2o.api.market.dto.request.CouponUseBackReqDTO;
import com.jzo2o.api.market.dto.request.CouponUseReqDTO;
import com.jzo2o.api.market.dto.response.AvailableCouponsResDTO;
import com.jzo2o.api.market.dto.response.CouponUseResDTO;
import com.jzo2o.api.orders.dto.response.OrderResDTO;
import com.jzo2o.api.orders.dto.response.OrderSimpleResDTO;
import com.jzo2o.api.trade.NativePayApi;
import com.jzo2o.api.trade.TradingApi;
import com.jzo2o.api.trade.dto.request.NativePayReqDTO;
import com.jzo2o.api.trade.dto.response.NativePayResDTO;
import com.jzo2o.api.trade.dto.response.TradingResDTO;
import com.jzo2o.api.trade.enums.PayChannelEnum;
import com.jzo2o.api.trade.enums.TradingStateEnum;
import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.enums.EnableStatusEnum;
import com.jzo2o.common.expcetions.BadRequestException;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.model.msg.TradeStatusMsg;
import com.jzo2o.common.utils.*;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.mysql.utils.PageUtils;
import com.jzo2o.orders.base.config.OrderStateMachine;
import com.jzo2o.orders.base.enums.OrderPayStatusEnum;
import com.jzo2o.orders.base.enums.OrderStatusChangeEventEnum;
import com.jzo2o.orders.base.enums.OrderStatusEnum;
import com.jzo2o.orders.base.mapper.OrdersMapper;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.base.model.domain.OrdersServe;
import com.jzo2o.orders.base.model.dto.OrderSnapshotDTO;
import com.jzo2o.orders.base.service.IOrdersDiversionCommonService;
import com.jzo2o.orders.manager.model.dto.OrderCancelDTO;
import com.jzo2o.orders.manager.model.dto.request.OrderPageQueryReqDTO;
import com.jzo2o.orders.manager.model.dto.request.OrdersPayReqDTO;
import com.jzo2o.orders.manager.model.dto.request.PlaceOrderReqDTO;
import com.jzo2o.orders.manager.model.dto.response.OperationOrdersDetailResDTO;
import com.jzo2o.orders.manager.model.dto.response.OrdersPayResDTO;
import com.jzo2o.orders.manager.model.dto.response.PlaceOrderResDTO;
import com.jzo2o.orders.manager.porperties.TradeProperties;
import com.jzo2o.orders.manager.service.IOrdersCreateService;
import com.jzo2o.orders.manager.service.IOrdersManagerService;
import com.jzo2o.orders.manager.service.IOrdersServeManagerService;
import com.jzo2o.orders.manager.strategy.OrderCancelStrategyManager;
import com.jzo2o.redis.helper.CacheHelper;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.jzo2o.common.constants.ErrorInfo.Code.TRADE_FAILED;
import static com.jzo2o.orders.base.constants.FieldConstants.SORT_BY;
import static com.jzo2o.orders.base.constants.OrderConstants.PRODUCT_APP_ID;
import static com.jzo2o.orders.base.constants.RedisConstants.Lock.ORDERS_SHARD_KEY_ID_GENERATOR;
import static com.jzo2o.orders.base.constants.RedisConstants.RedisKey.ORDERS;
import static com.jzo2o.orders.base.constants.RedisConstants.Ttl.ORDERS_PAGE_TTL;

/**
 * <p>
 * 下单服务类
 * </p>
 *
 * @author itcast
 * @since 2023-07-10
 */
@Slf4j
@Service
public class OrdersCreateServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersCreateService {
    @Resource
    private IOrdersServeManagerService ordersServeManagerService;

    @Resource
    private ServeApi serveApi;

    @Resource
    private TradingApi tradingApi;

    @Resource
    private AddressBookApi addressBookApi;

    @Resource
    private IOrdersCreateService owner;

    @Resource
    private RedisTemplate<String, Long> redisTemplate;


    @Resource
    private ServeProviderApi serveProviderApi;
    @Resource
    private InstitutionStaffApi institutionStaffApi;
    @Resource
    private OrderCancelStrategyManager orderCancelStrategyManager;

    @Resource
    private IOrdersDiversionCommonService ordersDiversionService;
    @Resource
    private OrderStateMachine orderStateMachine;
    @Resource
    private TradeProperties tradeProperties;
    @Resource
    private NativePayApi nativePayApi;
    @Resource
    private CacheHelper cacheHelper;

    @Value("${jzo2o.openPay}")
    private Boolean openPay;

    @Resource
    private CouponApi couponApi;

    /**
     * 生成订单id 格式：{yyMMdd}{13位id}
     *
     * @return
     */
    private Long generatorOrderId() {
        //通过Redis自增序列得到序号
        Long id = redisTemplate.opsForValue().increment(ORDERS_SHARD_KEY_ID_GENERATOR, 1);
        long orderId = DateUtils.getFormatDate(LocalDateTime.now(), "yyMMdd") * 10000000000000L + id;
        return orderId;
    }

    @Override
    public List<AvailableCouponsResDTO> getAvailableCoupons(Long serveId, Integer purNum) {
        // 1.获取服务
        ServeAggregationResDTO serveResDTO = serveApi.findById(serveId);
        if (serveResDTO == null || serveResDTO.getSaleStatus() != 2) {
            throw new BadRequestException("服务不可用");
        }
        // 2.计算订单总金额
        BigDecimal totalAmount = serveResDTO.getPrice().multiply(new BigDecimal(purNum));
        // 3.获取可用优惠券,并返回优惠券列表
        return couponApi.getAvailable(totalAmount);
    }

    @Override
    public PlaceOrderResDTO placeOrder(PlaceOrderReqDTO placeOrderReqDTO) {
        // 1.数据校验
        // 校验服务地址
        AddressBookResDTO detail = addressBookApi.detail(placeOrderReqDTO.getAddressBookId());
        if (detail == null) {
            throw new BadRequestException("预约地址异常，无法下单");
        }
        // 服务
        ServeAggregationResDTO serveResDTO = serveApi.findById(placeOrderReqDTO.getServeId());
        //服务下架不可下单
        if (serveResDTO == null || serveResDTO.getSaleStatus() != 2) {
            throw new BadRequestException("服务不可用");
        }


        // 2.下单前数据准备
        Orders orders = new Orders();
        // id 订单id
        orders.setId(generatorOrderId());
        // userId
        orders.setUserId(UserContext.currentUserId());
        // 订单状态
        orders.setOrdersStatus(OrderStatusEnum.NO_PAY.getStatus());
        // 支付状态，暂不支持，初始化一个空状态
        orders.setPayStatus(OrderPayStatusEnum.NO_PAY.getStatus());
        // 服务时间
        orders.setServeStartTime(placeOrderReqDTO.getServeStartTime());
        // 购买数量
        orders.setPurNum(NumberUtils.null2Default(placeOrderReqDTO.getPurNum(), 1));
        // 地理位置
        orders.setLon(detail.getLon());
        orders.setLat(detail.getLat());

        String serveAddress = new StringBuffer(detail.getProvince())
                .append(detail.getCity())
                .append(detail.getCounty())
                .append(detail.getAddress())
                .toString();
        orders.setServeAddress(serveAddress);
        // 联系人
        orders.setContactsName(detail.getName());
        orders.setContactsPhone(detail.getPhone());

        //服务类型信息
        orders.setServeTypeId(serveResDTO.getServeTypeId());
        orders.setServeTypeName(serveResDTO.getServeTypeName());
        // 服务id
        orders.setServeId(placeOrderReqDTO.getServeId());
        // 服务项id
        orders.setServeItemId(serveResDTO.getServeItemId());
        orders.setServeItemName(serveResDTO.getServeItemName());
        orders.setServeItemImg(serveResDTO.getServeItemImg());
        orders.setUnit(serveResDTO.getUnit());
        // 价格
        orders.setPrice(serveResDTO.getPrice());
        // 城市编码
        orders.setCityCode(serveResDTO.getCityCode());
        // 计算
        // 订单总金额 价格 * 购买数量
        orders.setTotalAmount(orders.getPrice().multiply(new BigDecimal(orders.getPurNum())));
        // 优惠金额 当前默认0
        orders.setDiscountAmount(BigDecimal.ZERO);
        // 实付金额 订单总金额 - 优惠金额
        orders.setRealPayAmount(NumberUtils.sub(orders.getTotalAmount(), orders.getDiscountAmount()));
        //排序字段
        long sortBy = DateUtils.toEpochMilli(orders.getServeStartTime()) + orders.getId() % 100000;
        orders.setSortBy(sortBy);

        // 使用优惠券下单
        if (ObjectUtils.isNotNull(placeOrderReqDTO.getCouponId())) {
            // 使用优惠券，走全局事务
            owner.addWithCoupon(orders, placeOrderReqDTO.getCouponId());
        } else {
            // 无优惠券下单，走本地事务
            owner.add(orders);
        }
        //TODO 暂时不需要支付
        if (Boolean.FALSE.equals(openPay)) {
            TradeStatusMsg msg = TradeStatusMsg.builder()
                    .productOrderNo(orders.getId())
                    .tradingChannel("WECHAT_PAY")
                    .statusCode(TradingStateEnum.YJS.getCode())
                    .tradingOrderNo(IdUtil.getSnowflakeNextId())
                    .transactionId(IdUtils.getSnowflakeNextIdStr())
                    .build();
            paySuccess(msg);
        }
        return new PlaceOrderResDTO(orders.getId());
    }


    /**
     * 更新支付状态
     *
     * @param id        订单id
     * @param payStatus 支付状态
     */
    @Override
    public Boolean updatePayStatus(Long id, Integer payStatus) {
        LambdaUpdateWrapper<Orders> updateWrapper = Wrappers.<Orders>lambdaUpdate()
                .eq(Orders::getId, id)
                .ne(Orders::getPayStatus, payStatus)
                .set(Orders::getPayStatus, payStatus);
        return super.update(updateWrapper);
    }

    /**
     * 更新退款状态
     *
     * @param id           订单id
     * @param refundStatus 退款状态
     * @param refundId     第三方支付的退款单号
     * @param refundNo     支付服务退款单号
     */
    @Override
    public Boolean updateRefundStatus(Long id, Integer refundStatus, String refundId, Long refundNo) {
        LambdaUpdateWrapper<Orders> updateWrapper = Wrappers.<Orders>lambdaUpdate()
                .eq(Orders::getId, id)
                .ne(Orders::getRefundStatus, refundStatus)
                .set(Orders::getRefundStatus, refundStatus)
                .set(ObjectUtil.isNotEmpty(refundId), Orders::getRefundId, refundId)
                .set(ObjectUtil.isNotEmpty(refundNo), Orders::getRefundNo, refundNo);
        return super.update(updateWrapper);
    }
    /**
     * 查询超时订单id列表
     *
     * @param count 数量
     * @return 订单id列表
     */
    @Override
    public List<Orders> queryOverTimePayOrdersListByCount(Integer count) {
        LambdaQueryWrapper<Orders> queryWrapper = Wrappers.<Orders>lambdaQuery()
                .eq(Orders::getOrdersStatus, OrderStatusEnum.NO_PAY.getStatus())
                .lt(Orders::getCreateTime, LocalDateTime.now().minusMinutes(15))
                .gt(Orders::getId, 0)
                .gt(Orders::getUserId, 0)
                .orderByAsc(Orders::getCreateTime)
                .last("LIMIT " + count);

        List<Orders> ordersList = baseMapper.selectList(queryWrapper);
        if (ObjectUtil.isEmpty(ordersList)) {
            return Collections.emptyList();
        }

        return ordersList;
    }
    /**
     * 支付成功， 其他信息暂且不填
     *
     * @param tradeStatusMsg 交易状态消息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(TradeStatusMsg tradeStatusMsg) {
        //查询订单
        Orders orders = baseMapper.selectById(tradeStatusMsg.getProductOrderNo());
        if (ObjectUtil.notEqual(OrderStatusEnum.NO_PAY.getStatus(), orders.getOrdersStatus())) {
            log.info("当前订单：{}，不是待支付状态", orders.getId());
            return;
        }

        //第三方支付单号校验
        if (ObjectUtil.isEmpty(tradeStatusMsg.getTransactionId())) {
            throw new CommonException("支付成功通知缺少第三方支付单号");
        }

        // 修改订单状态和支付状态
        OrderSnapshotDTO orderSnapshotDTO = OrderSnapshotDTO.builder()
                .payTime(LocalDateTime.now())
                .tradingOrderNo(tradeStatusMsg.getTradingOrderNo())
                .tradingChannel(tradeStatusMsg.getTradingChannel())
                .thirdOrderId(tradeStatusMsg.getTransactionId())
                .payStatus(OrderPayStatusEnum.PAY_SUCCESS.getStatus())
                .build();
        orderStateMachine.changeStatus(orders.getUserId(), String.valueOf(orders.getId()), OrderStatusChangeEventEnum.PAYED, orderSnapshotDTO);
        // 订单分流
        ordersDiversionService.diversion(orders);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Orders orders) {
        boolean save = this.save(orders);
        if (!save) {
            throw new DbRuntimeException("下单失败");
        }

        //状态机启动
        OrderSnapshotDTO orderSnapshotDTO = BeanUtil.toBean(baseMapper.selectById(orders.getId()), OrderSnapshotDTO.class);
        orderSnapshotDTO.setPayStatus(OrderPayStatusEnum.NO_PAY.getStatus());
        orderStateMachine.start(orders.getUserId(), orders.getId().toString(), orderSnapshotDTO);
    }

    @Override
    public int getPayResultFromTradServer(Long id) {
        //查询订单表
        Orders orders = baseMapper.selectById(id);
        if (ObjectUtil.isNull(orders)) {
            throw new CommonException(TRADE_FAILED, "订单不存在");
        }
        //支付结果
        Integer payStatus = orders.getPayStatus();
        //未支付且已存在支付服务的交易单号此时远程调用支付服务查询支付结果
        if (OrderPayStatusEnum.NO_PAY.getStatus() == payStatus
                && ObjectUtil.isNotEmpty(orders.getTradingOrderNo())) {
            //远程调用支付服务查询支付结果
            TradingResDTO tradingResDTO = tradingApi.findTradResultByTradingOrderNo(orders.getTradingOrderNo());
            //如果支付成功这里更新订单状态
            if (ObjectUtil.isNotNull(tradingResDTO)
                    && ObjectUtil.equals(tradingResDTO.getTradingState(), TradingStateEnum.YJS)) {
                //设置订单的支付状态成功
                TradeStatusMsg msg = TradeStatusMsg.builder()
                        .productOrderNo(orders.getId())
                        .tradingChannel(tradingResDTO.getTradingChannel())
                        .statusCode(TradingStateEnum.YJS.getCode())
                        .tradingOrderNo(tradingResDTO.getTradingOrderNo())
                        .transactionId(tradingResDTO.getTransactionId())
                        .build();
                paySuccess(msg);
                return OrderPayStatusEnum.PAY_SUCCESS.getStatus();
            }
        }
        return payStatus;
    }

    /**
     * 订单支付
     *
     * @param id              订单id
     * @param ordersPayReqDTO 订单支付请求体
     * @return 订单支付响应体
     */
    @Override
    public OrdersPayResDTO pay(Long id, OrdersPayReqDTO ordersPayReqDTO) {
        Orders orders =  baseMapper.selectById(id);
        if (ObjectUtil.isNull(orders)) {
            throw new CommonException(TRADE_FAILED, "订单不存在");
        }
        //订单的支付状态为成功直接返回
        if (OrderPayStatusEnum.PAY_SUCCESS.getStatus() == orders.getPayStatus()
                && ObjectUtil.isNotEmpty(orders.getTradingOrderNo())) {
            OrdersPayResDTO ordersPayResDTO = new OrdersPayResDTO();
            BeanUtil.copyProperties(orders, ordersPayResDTO);
            ordersPayResDTO.setProductOrderNo(orders.getId());
            return ordersPayResDTO;
        } else {
            //生成二维码
            NativePayResDTO nativePayResDTO = generatQrCode(orders, ordersPayReqDTO.getTradingChannel());
            OrdersPayResDTO ordersPayResDTO = BeanUtil.toBean(nativePayResDTO, OrdersPayResDTO.class);
            return ordersPayResDTO;
        }

    }


    //生成二维码
    private NativePayResDTO generatQrCode(Orders orders, PayChannelEnum tradingChannel) {

        //判断支付渠道
        Long enterpriseId = ObjectUtil.equal(PayChannelEnum.ALI_PAY, tradingChannel) ?
                tradeProperties.getAliEnterpriseId() : tradeProperties.getWechatEnterpriseId();

        //构建支付请求参数
        NativePayReqDTO nativePayReqDTO = new NativePayReqDTO();
        nativePayReqDTO.setProductOrderNo(orders.getId());
        nativePayReqDTO.setTradingChannel(tradingChannel);
        nativePayReqDTO.setTradingAmount(orders.getRealPayAmount());
        nativePayReqDTO.setEnterpriseId(enterpriseId);
        nativePayReqDTO.setProductAppId(PRODUCT_APP_ID);//指定支付来源是家政订单
        //判断是否切换支付渠道
        if (ObjectUtil.isNotEmpty(orders.getTradingChannel())
                && ObjectUtil.notEqual(orders.getTradingChannel(), tradingChannel.toString())) {
            nativePayReqDTO.setChangeChannel(true);
        }
        //生成支付二维码
        NativePayResDTO downLineTrading = nativePayApi.createDownLineTrading(nativePayReqDTO);
        //将交易单信息更新到订单中
        LambdaUpdateWrapper<Orders> updateWrapper = Wrappers.<Orders>lambdaUpdate()
                .eq(Orders::getId, orders.getId())
                .gt(Orders::getUserId, 0)
                .set(Orders::getTradingOrderNo, downLineTrading.getTradingOrderNo())
                .set(Orders::getTradingChannel, downLineTrading.getTradingChannel().toString());
        super.update(updateWrapper);
        return downLineTrading;
    }

    @GlobalTransactional
    @Override
    public void addWithCoupon(Orders orders, Long couponId) {
        CouponUseReqDTO couponUseReqDTO = new CouponUseReqDTO();
        couponUseReqDTO.setOrdersId(orders.getId());
        couponUseReqDTO.setId(couponId);
        couponUseReqDTO.setTotalAmount(orders.getTotalAmount());
        //优惠券核销
        CouponUseResDTO couponUseResDTO = couponApi.use(couponUseReqDTO);
        // 设置优惠金额
        orders.setDiscountAmount(couponUseResDTO.getDiscountAmount());
        // 计算实付金额
        BigDecimal realPayAmount = orders.getTotalAmount().subtract(orders.getDiscountAmount());
        orders.setRealPayAmount(realPayAmount);
        //保存订单
        add(orders);
    }



}
