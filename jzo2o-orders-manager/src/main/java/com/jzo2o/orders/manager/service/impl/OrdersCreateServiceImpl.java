package com.jzo2o.orders.manager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.DbRuntimeException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.api.foundations.ServeApi;
import com.jzo2o.api.foundations.dto.response.ServeAggregationResDTO;
import com.jzo2o.api.market.CouponApi;
import com.jzo2o.api.market.dto.request.CouponUseReqDTO;
import com.jzo2o.api.market.dto.response.AvailableCouponsResDTO;
import com.jzo2o.api.market.dto.response.CouponUseResDTO;
import com.jzo2o.api.trade.NativePayApi;
import com.jzo2o.api.trade.TradingApi;
import com.jzo2o.api.trade.dto.request.NativePayReqDTO;
import com.jzo2o.api.trade.dto.response.NativePayResDTO;
import com.jzo2o.api.trade.dto.response.TradingResDTO;
import com.jzo2o.api.trade.enums.PayChannelEnum;
import com.jzo2o.api.trade.enums.TradingStateEnum;
import com.jzo2o.common.expcetions.BadRequestException;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.model.msg.TradeStatusMsg;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.common.utils.NumberUtils;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.orders.base.config.OrderStateMachine;
import com.jzo2o.orders.base.enums.*;
import com.jzo2o.orders.base.mapper.OrdersMapper;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.base.model.dto.OrderSnapshotDTO;
import com.jzo2o.orders.manager.model.dto.request.OrdersPayReqDTO;
import com.jzo2o.orders.manager.model.dto.request.PlaceOrderReqDTO;
import com.jzo2o.orders.manager.model.dto.response.OrdersPayResDTO;
import com.jzo2o.orders.manager.model.dto.response.PlaceOrderResDTO;
import com.jzo2o.orders.manager.porperties.TradeProperties;
import com.jzo2o.orders.manager.service.IOrdersCreateService;
import com.jzo2o.orders.manager.service.client.CustomerClient;
import com.jzo2o.orders.manager.service.client.MarketClient;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.jzo2o.common.constants.ErrorInfo.Code.TRADE_FAILED;
import static com.jzo2o.orders.base.constants.RedisConstants.Lock.ORDERS_SHARD_KEY_ID_GENERATOR;

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
    private CustomerClient customerClient;

    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    @Resource
    private IOrdersCreateService ordersCreateService;

    @Resource
    private ServeApi serveApi;

    @Resource
    private MarketClient marketClient;

    @Resource
    private TradeProperties tradeProperties;

    @Resource
    private NativePayApi nativePayApi;

    @Resource
    private TradingApi tradingApi;

    @Resource
    private OrderStateMachine orderStateMachine;

    @Resource
    private CouponApi couponApi;

    @Override
//    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional // 开启全局事务
    public void addWithCoupon(Orders orders, Long couponId) {
        // 设置优惠券核销参数
        CouponUseReqDTO couponUseReqDTO = new CouponUseReqDTO();
        // 优惠券核销
        couponUseReqDTO.setId(couponId);
        couponUseReqDTO.setOrdersId(orders.getId());
        couponUseReqDTO.setTotalAmount(orders.getTotalAmount());
        // 设置优惠金额
        CouponUseResDTO couponUseResDTO = couponApi.use(couponUseReqDTO);
        // 优惠金额 当前默认0
        orders.setDiscountAmount(couponUseResDTO.getDiscountAmount());
        // 实付金额 订单总金额 - 优惠金额
        orders.setRealPayAmount(orders.getTotalAmount().subtract(orders.getDiscountAmount()));
        // 保存订单
        ordersCreateService.add(orders);

        // 模拟异常
//        int i = 1 / 0;

    }

    @Override
    public PlaceOrderResDTO placeOrder(PlaceOrderReqDTO placeOrderReqDTO) {
        // 下单人信息, 从 customer 服务获取
        AddressBookResDTO detail = customerClient.getDetail(placeOrderReqDTO.getAddressBookId());
        if (detail == null) {
            throw new CommonException("预约地址异常，无法下单");
        }

        // 服务相关信息, 调用 foundations 获取
        ServeAggregationResDTO serveAggregationResDTO = serveApi.findById(placeOrderReqDTO.getServeId());
        if (serveAggregationResDTO == null || serveAggregationResDTO.getSaleStatus() != 2) {
            throw new CommonException("服务不可用");
        }

        // 2.下单前数据准备
        Orders orders = new Orders();
        // id 订单id
        orders.setId(generateOrderId());
        // userId
        orders.setUserId(UserContext.currentUserId());
        // 订单状态
        orders.setOrdersStatus(0);
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
        orders.setServeTypeId(serveAggregationResDTO.getServeTypeId());
        orders.setServeTypeName(serveAggregationResDTO.getServeTypeName());
        // 服务id
        orders.setServeId(placeOrderReqDTO.getServeId());
        // 服务项id
        orders.setServeItemId(serveAggregationResDTO.getServeItemId());
        orders.setServeItemName(serveAggregationResDTO.getServeItemName());
        orders.setServeItemImg(serveAggregationResDTO.getServeItemImg());
        orders.setUnit(serveAggregationResDTO.getUnit());
        // 价格
        orders.setPrice(serveAggregationResDTO.getPrice());
        // 城市编码
        orders.setCityCode(serveAggregationResDTO.getCityCode());
        // 计算
        // 订单总金额 价格 * 购买数量
        orders.setTotalAmount(orders.getPrice().multiply(new BigDecimal(orders.getPurNum())));
        // 优惠金额 当前默认0
        orders.setDiscountAmount(BigDecimal.ZERO);
        // 实付金额 订单总金额 - 优惠金额
        orders.setRealPayAmount(NumberUtils.sub(orders.getTotalAmount(), orders.getDiscountAmount()));
        //排序字段,根据服务开始时间转为毫秒时间戳+订单后5位
        long sortBy = DateUtils.toEpochMilli(orders.getServeStartTime()) + orders.getId() % 100000;
        orders.setSortBy(sortBy);

        // 设置优惠金额
        if (ObjectUtils.isNotNull(placeOrderReqDTO.getCouponId())) {
            ordersCreateService.addWithCoupon(orders,placeOrderReqDTO.getCouponId());
        } else {
            //保存订单
            ordersCreateService.add(orders);
        }

        return new PlaceOrderResDTO(orders.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Orders orders) {
        boolean save = this.save(orders);
        if(!save) {
            throw new DbRuntimeException("下单失败");
        }
        // 调用状态机的启动方法
        // Long dbShardId 分库分表的时候要用, String bizId 订单id, T bizSnapshot 订单快照
        OrderSnapshotDTO orderSnapshotDTO = BeanUtils.copyBean(getById(orders.getId()), OrderSnapshotDTO.class);
        // 分库按照用户来分，用户id % 3
        orderStateMachine.start(orders.getUserId(), orders.getId().toString(), orderSnapshotDTO);
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
        // 查询当前订单是否存在
        Orders orders = baseMapper.selectById(id);
        if (ObjectUtil.isNull(orders)) {
            throw new CommonException(TRADE_FAILED, "订单不存在");
        }
        // 订单支付状态成功直接返回
        if (OrderPayStatusEnum.PAY_SUCCESS.getStatus() == orders.getPayStatus()
        && ObjectUtil.isNotEmpty(orders.getTradingOrderNo())) {
            OrdersPayResDTO ordersPayResDTO = new OrdersPayResDTO();
            BeanUtil.copyProperties(orders, ordersPayResDTO);
            ordersPayResDTO.setProductOrderNo(orders.getId());
            return ordersPayResDTO;
        } else {
            //生成二维码
            NativePayResDTO nativePayResDTO = generateQrCode(orders, ordersPayReqDTO.getTradingChannel());
            OrdersPayResDTO ordersPayResDTO = BeanUtil.toBean(nativePayResDTO, OrdersPayResDTO.class);
            return ordersPayResDTO;
        }
    }

    private NativePayResDTO generateQrCode(Orders orders, PayChannelEnum tradingChannel) {
        // 判断支付渠道
        Long enterpriseId = ObjectUtil.equal(PayChannelEnum.ALI_PAY, tradingChannel) ?
                tradeProperties.getAliEnterpriseId() : tradeProperties.getWechatEnterpriseId();

        // 构建支付请求参数
        NativePayReqDTO nativePayReqDTO = new NativePayReqDTO();
        //商户号
        nativePayReqDTO.setEnterpriseId(enterpriseId);
        //业务系统标识
        nativePayReqDTO.setProductAppId("jzo2o.orders");
        //家政订单号
        nativePayReqDTO.setProductOrderNo(orders.getId());
        //支付渠道
        nativePayReqDTO.setTradingChannel(tradingChannel);
        //支付金额
        nativePayReqDTO.setTradingAmount(orders.getRealPayAmount());
        //备注信息
        nativePayReqDTO.setMemo(orders.getServeItemName());
        //判断是否切换支付渠道
        if(ObjectUtil.isNotEmpty(orders.getTradingChannel()) &&
                ObjectUtil.notEqual(orders.getTradingChannel(), tradingChannel.toString())) {
            nativePayReqDTO.setChangeChannel(true);
        }
        //生成支付二维码
        NativePayResDTO downLineTrading = nativePayApi.createDownLineTrading(nativePayReqDTO);
        //将二维码更新到交易订单中
        if (ObjectUtil.isNotEmpty(downLineTrading)) {
            log.info("订单：{} 请求支付，生成二维码：{}", orders.getId(), downLineTrading.toString());
            // 将二维码更新到交易订单中
            boolean update = lambdaUpdate()
                    .eq(Orders::getId, downLineTrading.getProductOrderNo())
                    .set(Orders::getTradingOrderNo, downLineTrading.getTradingOrderNo())
                    .set(Orders::getTradingChannel, downLineTrading.getTradingChannel())
                    .update();
            if(!update) {
                throw new CommonException("订单:"+orders.getId()+"请求支付更新交易单号失败");
            }
        }
        return downLineTrading;
    }
    @Override
    public OrdersPayResDTO getPayResultFromTradServer(Long id) {
        // 查询订单表
        Orders orders = baseMapper.selectById(id);
        if(ObjectUtil.isNull(orders)) {
            throw new CommonException("该订单不存在");
        }
        // 支付结果
        Integer payStatus = orders.getPayStatus();
        //未支付且已存在支付服务的交易单号此时远程调用支付服务查询支付结果
        if (OrderPayStatusEnum.NO_PAY.getStatus() == payStatus &&
                ObjectUtil.isNotEmpty(orders.getTradingOrderNo())) {
            //远程调用支付服务查询支付结果
            TradingResDTO tradingResDTO = tradingApi.findTradResultByTradingOrderNo(orders.getTradingOrderNo());

            //如果支付成功这里更新订单状态
            if(ObjectUtil.isNotNull(tradingResDTO)
                    && ObjectUtil.equal(tradingResDTO.getTradingState(), TradingStateEnum.YJS)) {
                //设置订单的支付状态成功
                TradeStatusMsg msg = TradeStatusMsg.builder()
                        .productOrderNo(orders.getId())
                        .tradingChannel(tradingResDTO.getTradingChannel())
                        .statusCode(TradingStateEnum.YJS.getCode())
                        .tradingOrderNo(tradingResDTO.getTradingOrderNo())
                        .transactionId(tradingResDTO.getTransactionId())
                        .build();
                ordersCreateService.paySuccess(msg);
                //构造返回数据
                OrdersPayResDTO ordersPayResDTO = BeanUtils.toBean(msg, OrdersPayResDTO.class);
                ordersPayResDTO.setPayStatus(OrderPayStatusEnum.PAY_SUCCESS.getStatus());
                return ordersPayResDTO;
            }
        }
        OrdersPayResDTO ordersPayResDTO = new OrdersPayResDTO();
        ordersPayResDTO.setPayStatus(payStatus);
        ordersPayResDTO.setProductOrderNo(orders.getId());
        ordersPayResDTO.setTradingOrderNo(orders.getTradingOrderNo());
        ordersPayResDTO.setTradingChannel(orders.getTradingChannel());
        return ordersPayResDTO;
    }

    /**
     * 支付成功， 其他信息暂且不填
     *
     * @param tradeStatusMsg 交易状态消息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void paySuccess(TradeStatusMsg tradeStatusMsg) {

//        //查询订单
//        Orders orders = baseMapper.selectById(tradeStatusMsg.getProductOrderNo());
//        if (ObjectUtil.isNull(orders)) {
//            throw new CommonException(TRADE_FAILED, "订单不存在");
//        }
//        //校验支付状态如果不是待支付状态则不作处理
//        if (ObjectUtil.notEqual(OrderPayStatusEnum.NO_PAY.getStatus(), orders.getPayStatus())) {
//            log.info("更新订单支付成功，当前订单:{}支付状态不是待支付状态", orders.getId());
//            return;
//        }
//        //第三方支付单号校验
//        if (ObjectUtil.isEmpty(tradeStatusMsg.getTransactionId())) {
//            throw new CommonException("支付成功通知缺少第三方支付单号");
//        }
//        boolean update = lambdaUpdate()
//                .eq(Orders::getId, orders.getId())
//                .set(Orders::getPayTime, LocalDateTime.now())
//                .set(Orders::getTradingOrderNo, tradeStatusMsg.getTradingOrderNo())
//                .set(Orders::getTradingChannel, tradeStatusMsg.getTradingChannel())
//                .set(Orders::getTransactionId, tradeStatusMsg.getTransactionId())
//                .set(Orders::getPayStatus, OrderPayStatusEnum.PAY_SUCCESS.getStatus())//支付状态
//                .set(Orders::getOrdersStatus, OrderStatusEnum.DISPATCHING.getStatus())//订单状态更新为派单中
//                .update();
//        if(!update) {
//            log.info("更新订单:{}支付失败",orders.getId());
//            throw new CommonException("更新订单"+orders.getId()+"支付成功失败");
//        }

        // 订单
        Orders orders = getById(tradeStatusMsg.getProductOrderNo());

        // 使用状态机将支付状态改为派单中
        // Long dbShardId, String bizId, StatusChangeEvent statusChangeEventEnum, T bizSnapshot
        OrderSnapshotDTO orderSnapshotDTO = new OrderSnapshotDTO();
        orderSnapshotDTO.setTradingOrderNo(tradeStatusMsg.getTradingOrderNo());
        orderSnapshotDTO.setTradingChannel(tradeStatusMsg.getTradingChannel());
        orderSnapshotDTO.setPayTime(LocalDateTime.now());
        orderSnapshotDTO.setThirdOrderId(tradeStatusMsg.getTransactionId());
        orderStateMachine.changeStatus(orders.getUserId(), tradeStatusMsg.getProductOrderNo().toString(), OrderStatusChangeEventEnum.PAYED, orderSnapshotDTO);
    }

    /**
     * 查询超时订单id列表
     *
     * @param count 数量
     * @return 订单id列表
     */
    @Override
    public List<Orders> queryOverTimePayOrdersListByCount(Integer count) {
        // 查询当前超过15分钟未支付的订单
        List<Orders> orders = lambdaQuery()
                .eq(Orders::getOrdersStatus, OrderStatusEnum.NO_PAY.getStatus())
                .lt(Orders::getCreateTime, LocalDateTime.now().minusMinutes(15))
                .last("limit " + count)
                .list();
        return orders;
    }

    /**
     * 获取可用优惠券
     *
     * @param serveId 服务id
     * @param purNum  购买数量
     * @return 可用优惠券列表
     */
    @Override
    public List<AvailableCouponsResDTO> getCoupons(Long serveId, Integer purNum) {
        // 1.获取服务
        ServeAggregationResDTO serveResDTO = serveApi.findById(serveId);
        if(ObjectUtils.isNull(serveResDTO) || serveResDTO.getSaleStatus() != ServeStatusEnum.SERVING.getStatus()) {
            throw new BadRequestException("服务不可用");
        }
        // 2.计算订单总金额
        BigDecimal totalAmount = serveResDTO.getPrice().multiply(new BigDecimal(purNum));

        // 3. 调用远程服务获取 优惠券列表
        List<AvailableCouponsResDTO> available = marketClient.getAvailable(totalAmount);
        return available;
    }


    private Long generateOrderId() {
        // 通过redis自增序列得到序号
        Long id = redisTemplate.opsForValue().increment(ORDERS_SHARD_KEY_ID_GENERATOR, 1);
        // 生成订单号 2位年 + 2为月+2为日+13为序号
        long orderId = DateUtils.getFormatDate(LocalDateTime.now(), "yyMMdd") * 10000000000000L + id;
        return orderId;
    }

}
