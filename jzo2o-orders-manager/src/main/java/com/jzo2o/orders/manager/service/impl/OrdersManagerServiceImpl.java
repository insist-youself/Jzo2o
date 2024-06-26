package com.jzo2o.orders.manager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.jzo2o.api.customer.dto.response.CommonUserResDTO;
import com.jzo2o.api.market.CouponApi;
import com.jzo2o.api.market.dto.request.CouponUseBackReqDTO;
import com.jzo2o.api.orders.dto.response.OrderResDTO;
import com.jzo2o.api.orders.dto.response.OrderSimpleResDTO;
import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.enums.EnableStatusEnum;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.JsonUtils;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.mysql.utils.PageUtils;
import com.jzo2o.orders.base.config.OrderStateMachine;
import com.jzo2o.orders.base.enums.OrderPayStatusEnum;
import com.jzo2o.orders.base.enums.OrderRefundStatusEnum;
import com.jzo2o.orders.base.enums.OrderStatusChangeEventEnum;
import com.jzo2o.orders.base.enums.OrderStatusEnum;
import com.jzo2o.orders.base.mapper.OrdersMapper;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.base.model.domain.OrdersCanceled;
import com.jzo2o.orders.base.model.domain.OrdersRefund;
import com.jzo2o.orders.base.model.dto.OrderSnapshotDTO;
import com.jzo2o.orders.base.model.dto.OrderUpdateStatusDTO;
import com.jzo2o.orders.base.service.IOrdersCommonService;
import com.jzo2o.orders.manager.handler.OrdersHandler;
import com.jzo2o.orders.manager.model.dto.OrderCancelDTO;
import com.jzo2o.orders.manager.model.dto.request.OrderPageQueryReqDTO;
import com.jzo2o.orders.manager.model.dto.response.OrdersPayResDTO;
import com.jzo2o.orders.manager.service.IOrdersCanceledService;
import com.jzo2o.orders.manager.service.IOrdersCreateService;
import com.jzo2o.orders.manager.service.IOrdersManagerService;
import com.jzo2o.orders.manager.service.IOrdersRefundService;
import com.jzo2o.redis.helper.CacheHelper;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.parsson.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.jzo2o.orders.base.constants.FieldConstants.SORT_BY;
import static com.jzo2o.orders.base.constants.RedisConstants.RedisKey.ORDERS;
import static com.jzo2o.orders.base.constants.RedisConstants.Ttl.ORDERS_PAGE_TTL;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-07-10
 */
@Slf4j
@Service
public class OrdersManagerServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersManagerService {

    @Resource
    private OrdersManagerServiceImpl owner;

    @Resource
    private OrdersHandler ordersHandler;

    @Resource
    private IOrdersCanceledService ordersCanceledService;

    @Resource
    private IOrdersCreateService ordersCreateService;

    @Resource
    private IOrdersRefundService ordersRefundService;

    @Resource
    private OrderStateMachine orderStateMachine;

    @Resource
    private CacheHelper cacheHelper;

    @Resource
    private CouponApi couponApi;

    @Override
    public List<Orders> batchQuery(List<Long> ids) {
        LambdaQueryWrapper<Orders> queryWrapper = Wrappers.<Orders>lambdaQuery().in(Orders::getId, ids).ge(Orders::getUserId, 0);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Orders queryById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 滚动分页查询
     *
     * @param currentUserId 当前用户id
     * @param ordersStatus  订单状态，0：待支付，100：派单中，200：待服务，300：服务中，400：待评价，500：订单完成，600：已取消，700：已关闭
     * @param sortBy        排序字段
     * @return 订单列表
     */
    @Override
    public List<OrderSimpleResDTO> consumerQueryList(Long currentUserId, Integer ordersStatus, Long sortBy) {
        //1.构件查询条件
        LambdaQueryWrapper<Orders> queryWrapper = Wrappers.<Orders>lambdaQuery()
                .eq(ObjectUtils.isNotNull(ordersStatus), Orders::getOrdersStatus, ordersStatus)
                .lt(ObjectUtils.isNotNull(sortBy), Orders::getSortBy, sortBy)
                .eq(Orders::getUserId, currentUserId)
                .eq(Orders::getDisplay, EnableStatusEnum.ENABLE.getStatus())
                .select(Orders::getId); // 只查询id
        Page<Orders> queryPage = new Page<>();
        queryPage.addOrder(OrderItem.desc(SORT_BY));
        queryPage.setSearchCount(false);

        //2.查询订单列表
        Page<Orders> ordersPage = baseMapper.selectPage(queryPage, queryWrapper);
        if (ObjectUtil.isEmpty(ordersPage.getRecords())) {
            return new ArrayList<>();
        }

        // 提取订单id列表
        List<Long> orderIds = CollUtils.getFieldValues(ordersPage.getRecords(), Orders::getId);
        //先查询缓存，缓存没有再查询数据库
        //参数1：redisKey的一部分
        String redisKey = String.format(ORDERS, currentUserId);
        //参数2：订单id列表
        //参数3：batchDataQueryExecutor 当缓存中没有时执行batchDataQueryExecutor从数据库查询
        // batchDataQueryExecutor的方法：Map<K, T> execute(List<K> objectIds, Class<T> clazz); objectIds表示缓存中未匹配到的id，clazz指定map中value的数据类型
        //参数4：返回List中的数据类型
        //参数5：过期时间
        List<OrderSimpleResDTO> orderSimpleResDTOS = cacheHelper.<Long, OrderSimpleResDTO>batchGet(redisKey, orderIds, (noCacheIds, clazz) -> {
            List<Orders> ordersList = batchQuery(noCacheIds);
            if (ObjectUtil.isNull(ordersList)) {
                return  new HashMap<>();
            }
            Map<Long, OrderSimpleResDTO> collect = ordersList.stream().collect(Collectors.toMap(Orders::getId, o -> BeanUtils.copyBean(o, OrderSimpleResDTO.class)));
            return collect;
        }, OrderSimpleResDTO.class, ORDERS_PAGE_TTL);

//        // 根据订单id查询订单列表
//        List<Orders> orders = batchQuery(orderIds);
//        List<OrderSimpleResDTO> orderSimpleResDTOS = BeanUtils.copyToList(orders, OrderSimpleResDTO.class);
        return orderSimpleResDTOS;

    }
//    @Override
//    public List<OrderSimpleResDTO> consumerQueryList(Long currentUserId, Integer ordersStatus, Long sortBy) {
//        //1.构件查询条件
//        LambdaQueryWrapper<Orders> queryWrapper = Wrappers.<Orders>lambdaQuery()
//                .eq(ObjectUtils.isNotNull(ordersStatus), Orders::getOrdersStatus, ordersStatus)
//                .lt(ObjectUtils.isNotNull(sortBy), Orders::getSortBy, sortBy)
//                .eq(Orders::getUserId, currentUserId)
//                .eq(Orders::getDisplay, EnableStatusEnum.ENABLE.getStatus());
//        Page<Orders> queryPage = new Page<>();
//        queryPage.addOrder(OrderItem.desc(SORT_BY));
//        queryPage.setSearchCount(false);
//
//        //2.查询订单列表
//        Page<Orders> ordersPage = baseMapper.selectPage(queryPage, queryWrapper);
//        List<Orders> records = ordersPage.getRecords();
//        List<OrderSimpleResDTO> orderSimpleResDTOS = BeanUtil.copyToList(records, OrderSimpleResDTO.class);
//        return orderSimpleResDTOS;
//
//    }

    /**
     * 根据订单id查询
     *
     * @param id 订单id
     * @return 订单详情
     */
    @Override
    public OrderResDTO getDetail(Long id) {
        //查询订单
        Orders orders = queryById(id);
        // 查询快照表
        String currentSnapshot = orderStateMachine.getCurrentSnapshotCache(String.valueOf(id));
        OrderSnapshotDTO orderSnapshotDTO = JSONUtil.toBean(currentSnapshot, OrderSnapshotDTO.class);
        // 懒加载方式取消支付超时订单
        orderSnapshotDTO = canalIfPayOvertime(orderSnapshotDTO);
        OrderResDTO orderResDTO = BeanUtils.copyBean(orders, OrderResDTO.class);

        return orderResDTO;
    }
//    @Override
//    public OrderResDTO getDetail(Long id) {
//        //查询订单
//        Orders orders = queryById(id);
//        // 如果过期则取消订单
//        orders = canalIfPayOvertime(orders);
//        OrderResDTO orderResDTO = BeanUtil.toBean(orders, OrderResDTO.class);
//        return orderResDTO;
//    }

    private OrderSnapshotDTO canalIfPayOvertime(OrderSnapshotDTO orderSnapshotDTO) {
        //判断当前订单是否为存在，是否为待支付已过期订单
        if (ObjectUtil.isNotNull(orderSnapshotDTO) && orderSnapshotDTO.getOrdersStatus() == OrderStatusEnum.NO_PAY.getStatus()
                && orderSnapshotDTO.getCreateTime().plusMinutes(15).isBefore(LocalDateTime.now())) {
            //查询一下最新的支付状态，如果没有支付成功，再执行下边的取消代码
            OrdersPayResDTO payResultFromTradServer = ordersCreateService.getPayResultFromTradServer(orderSnapshotDTO.getId());
            // 判断当前订单状态是否为已支付
            if (ObjectUtils.isNotNull(payResultFromTradServer) && orderSnapshotDTO.getPayStatus() != OrderPayStatusEnum.PAY_SUCCESS.getStatus()) {
                OrderCancelDTO orderCancelDTO = BeanUtils.toBean(orderSnapshotDTO, OrderCancelDTO.class);
                orderCancelDTO.setCurrentUserType(UserType.SYSTEM);
                orderCancelDTO.setCancelReason("订单超时未支付，自动取消");
                cancelByNoPay(orderCancelDTO);

                // 从快照中查询订单数据
                String snapshotCache = orderStateMachine.getCurrentSnapshotCache(String.valueOf(orderSnapshotDTO.getSnapshotId()));
                orderSnapshotDTO = JSONUtil.toBean(snapshotCache, OrderSnapshotDTO.class);
                return orderSnapshotDTO;
            }
        }
        return orderSnapshotDTO;
    }

    /**
     * 订单评价
     *
     * @param ordersId 订单id
     */
    @Override
    @Transactional
    public void evaluationOrder(Long ordersId) {
//        //查询订单详情
//        Orders orders = queryById(ordersId);
//
//        //构建订单快照
//        OrderSnapshotDTO orderSnapshotDTO = OrderSnapshotDTO.builder()
//                .evaluationTime(LocalDateTime.now())
//                .build();
//
//        //订单状态变更
//        orderStateMachine.changeStatus(orders.getUserId(), orders.getId().toString(), OrderStatusChangeEventEnum.EVALUATE, orderSnapshotDTO);


    }


    @Override
    @GlobalTransactional
    public void cancel(OrderCancelDTO orderCancelDTO) {

        Orders orders = getById(orderCancelDTO.getId());
        BeanUtils.copyProperties(orders, orderCancelDTO);
        // 检验订单是否存在
        if (ObjectUtil.isNull(orders)) {
            throw new DbRuntimeException("找不到要取消的订单,订单号：{}", orderCancelDTO.getId());
        }
        Integer ordersStatus = orders.getOrdersStatus();
        // 订单状态未支付
        if (ordersStatus == OrderStatusEnum.NO_PAY.getStatus()) {
            owner.cancelByNoPay(orderCancelDTO);
        } else if (ordersStatus == OrderStatusEnum.DISPATCHING.getStatus()) {
            // 订单状态派单中
            owner.cancelByDispatching(orderCancelDTO);
            // 新启动一个线程进行退款
            ordersHandler.requestRefundNewThread(orders.getId());
        } else {
            // 其他情况暂时不考虑
            throw new CommonException("除待支付、派单中状态，其他情况暂不考虑");
        }

        // 若优惠金额大于0， 则说明使用了优惠券，需进行优惠券退回操作
        if(orders.getDiscountAmount().compareTo(new BigDecimal(0)) > 0) {
            CouponUseBackReqDTO couponUseBackReqDTO = new CouponUseBackReqDTO();
            couponUseBackReqDTO.setOrdersId(orderCancelDTO.getId());
            couponUseBackReqDTO.setUserId(orderCancelDTO.getUserId());
            couponApi.useBack(couponUseBackReqDTO);
        }

    }

    //派单中状态取消订单
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelByDispatching(OrderCancelDTO orderCancelDTO) {
        // 保存取消订单的记录
        OrdersCanceled ordersCanceled = BeanUtils.copyBean(orderCancelDTO, OrdersCanceled.class);
        ordersCanceled.setCancellerId(orderCancelDTO.getCurrentUserId());
        ordersCanceled.setCancelerName(orderCancelDTO.getCurrentUserName());
        ordersCanceled.setCancellerType(orderCancelDTO.getCurrentUserType());
        ordersCanceled.setCancelTime(LocalDateTime.now());
        ordersCanceledService.save(ordersCanceled);

//        // 更新订单状态为关闭订单
//        OrderUpdateStatusDTO orderUpdateStatusDTO = OrderUpdateStatusDTO.builder()
//                .id(orderCancelDTO.getId())
//                .targetStatus(OrderStatusEnum.CANCELED.getStatus())
//                .originStatus(OrderStatusEnum.DISPATCHING.getStatus())
//                .refundStatus(OrderRefundStatusEnum.REFUNDING.getStatus())
//                .build();
//        int result = ordersCommonService.updateStatus(orderUpdateStatusDTO);
//        if(result <= 0) {
//            throw new DbRuntimeException("待服务订单关闭事件处理失败");
//        }
        // 使用状态机将支付状态改为派单中
        // Long dbShardId, String bizId, StatusChangeEvent statusChangeEventEnum, T bizSnapshot
        OrderSnapshotDTO orderSnapshotDTO = new OrderSnapshotDTO();
        orderSnapshotDTO.setCancellerId(orderCancelDTO.getCurrentUserId());
        orderSnapshotDTO.setCancelerName(orderCancelDTO.getCurrentUserName());
        orderSnapshotDTO.setCancelTime(LocalDateTime.now());
        orderSnapshotDTO.setCancelReason(orderCancelDTO.getCancelReason());

        orderStateMachine.changeStatus(orderCancelDTO.getUserId(), orderCancelDTO.getId().toString(),
                OrderStatusChangeEventEnum.CLOSE_DISPATCHING_ORDER, orderSnapshotDTO);


        // 添加退款记录
        OrdersRefund ordersRefund = new OrdersRefund();
        ordersRefund.setId(orderCancelDTO.getId());
        ordersRefund.setTradingOrderNo(orderCancelDTO.getTradingOrderNo());
        ordersRefund.setRealPayAmount(orderCancelDTO.getRealPayAmount());
        ordersRefundService.save(ordersRefund);

    }

    /**
     *  运营端总订单查询
     * @param orderPageQueryReqDTO 订单处理服务
     * @return 订单简略响应数据
     */
    @Override
    public PageResult<OrderSimpleResDTO> operationQueryList(OrderPageQueryReqDTO orderPageQueryReqDTO) {
        Page<Orders> ordersPage = PageUtils.parsePageQuery(orderPageQueryReqDTO, Orders.class);
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<Orders>()
                .eq(ObjectUtils.isNotEmpty(orderPageQueryReqDTO.getContactsPhone()), Orders::getContactsPhone, orderPageQueryReqDTO.getContactsPhone())
                .eq(ObjectUtils.isNotEmpty(orderPageQueryReqDTO.getId()), Orders::getId, orderPageQueryReqDTO.getId())
                .le(ObjectUtils.isNotEmpty(orderPageQueryReqDTO.getMaxCreateTime()), Orders::getCreateTime, orderPageQueryReqDTO.getMaxCreateTime())
                .ge(ObjectUtils.isNotEmpty(orderPageQueryReqDTO.getMinCreateTime()), Orders::getCreateTime, orderPageQueryReqDTO.getMinCreateTime())
                .eq(ObjectUtils.isNotEmpty(orderPageQueryReqDTO.getOrdersStatus()), Orders::getOrdersStatus, orderPageQueryReqDTO.getOrdersStatus())
                .eq(ObjectUtils.isNotEmpty(orderPageQueryReqDTO.getPayStatus()), Orders::getPayStatus, orderPageQueryReqDTO.getPayStatus())
                .eq(ObjectUtils.isNotEmpty(orderPageQueryReqDTO.getRefundStatus()), Orders::getRefundStatus, orderPageQueryReqDTO.getRefundStatus())
                .eq(ObjectUtils.isNotEmpty(orderPageQueryReqDTO.getUserId()), Orders::getUserId, orderPageQueryReqDTO.getUserId())
                .select(Orders::getId);
        Page<Orders> currentPage = baseMapper.selectPage(ordersPage, lambdaQueryWrapper);
        if(ObjectUtils.isEmpty(currentPage.getRecords())) {
            return PageUtils.toPage(new Page<>(), OrderSimpleResDTO.class);
        }
        List<Orders> records = currentPage.getRecords();
        orderPageQueryReqDTO.setOrdersIdList(records.stream().map(Orders::getId).collect(Collectors.toList()));
        lambdaQueryWrapper = new LambdaQueryWrapper<Orders>()
                .in(Orders::getId, orderPageQueryReqDTO.getOrdersIdList());
        currentPage = baseMapper.selectPage(ordersPage, lambdaQueryWrapper);
        return PageUtils.toPage(currentPage, OrderSimpleResDTO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    //未支付状态取消订单
    public void cancelByNoPay(OrderCancelDTO orderCancelDTO) {
        //保存订单记录
        OrdersCanceled ordersCanceled = BeanUtils.toBean(orderCancelDTO, OrdersCanceled.class);
        ordersCanceled.setCancellerId(orderCancelDTO.getCurrentUserId());
        ordersCanceled.setCancelerName(orderCancelDTO.getCurrentUserName());
        ordersCanceled.setCancellerType(orderCancelDTO.getCurrentUserType());
        ordersCanceled.setCancelTime(LocalDateTime.now());
        ordersCanceledService.save(ordersCanceled);
//        //更新状态为取消订单
//        OrderUpdateStatusDTO orderUpdateStatusDTO = OrderUpdateStatusDTO.builder()
//                .id(orderCancelDTO.getId())
//                .originStatus(OrderStatusEnum.NO_PAY.getStatus())
//                .targetStatus(OrderStatusEnum.CANCELED.getStatus())
//                .build();
//        Integer result = ordersCommonService.updateStatus(orderUpdateStatusDTO);
//        if(result <= 0) {
//            throw new CommonException("订单取消事件处理失败");
//        }

        // 使用状态机将支付状态改为派单中
        // Long dbShardId, String bizId, StatusChangeEvent statusChangeEventEnum, T bizSnapshot
        OrderSnapshotDTO orderSnapshotDTO = new OrderSnapshotDTO();
        orderSnapshotDTO.setCancellerId(orderCancelDTO.getCurrentUserId());
        orderSnapshotDTO.setCancelerName(orderCancelDTO.getCurrentUserName());
        orderSnapshotDTO.setCancelTime(LocalDateTime.now());
        orderSnapshotDTO.setCancelReason(orderCancelDTO.getCancelReason());

        orderStateMachine.changeStatus(orderCancelDTO.getUserId(), orderCancelDTO.getId().toString(),
                OrderStatusChangeEventEnum.CANCEL, orderSnapshotDTO);

    }

}
