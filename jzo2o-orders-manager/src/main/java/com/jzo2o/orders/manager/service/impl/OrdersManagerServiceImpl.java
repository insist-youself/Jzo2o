package com.jzo2o.orders.manager.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.orders.dto.response.OrderResDTO;
import com.jzo2o.api.orders.dto.response.OrderSimpleResDTO;
import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.enums.EnableStatusEnum;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.ObjectUtils;
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
import com.jzo2o.orders.manager.service.IOrdersCanceledService;
import com.jzo2o.orders.manager.service.IOrdersManagerService;
import com.jzo2o.orders.manager.service.IOrdersRefundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.jzo2o.orders.base.constants.FieldConstants.SORT_BY;

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
    private IOrdersCommonService ordersCommonService;

    @Resource
    private IOrdersRefundService ordersRefundService;

    @Resource
    private OrderStateMachine orderStateMachine;

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
                .eq(Orders::getDisplay, EnableStatusEnum.ENABLE.getStatus());
        Page<Orders> queryPage = new Page<>();
        queryPage.addOrder(OrderItem.desc(SORT_BY));
        queryPage.setSearchCount(false);

        //2.查询订单列表
        Page<Orders> ordersPage = baseMapper.selectPage(queryPage, queryWrapper);
        List<Orders> records = ordersPage.getRecords();
        List<OrderSimpleResDTO> orderSimpleResDTOS = BeanUtil.copyToList(records, OrderSimpleResDTO.class);
        return orderSimpleResDTOS;

    }
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
        // 如果过期则取消订单
        orders = canalIfPayOvertime(orders);
        OrderResDTO orderResDTO = BeanUtil.toBean(orders, OrderResDTO.class);
        return orderResDTO;
    }

    private Orders canalIfPayOvertime(Orders orders) {
        //判断当前订单是否为存在，是否为待支付已过期订单
        if(ObjectUtil.isNotNull(orders) && orders.getOrdersStatus() == OrderStatusEnum.NO_PAY.getStatus()
            && orders.getCreateTime().plusMinutes(15).isBefore(LocalDateTime.now())){
            orders = getById(orders.getId());
            // 判断当前订单状态是否为已支付
            if (orders.getPayStatus() != OrderPayStatusEnum.PAY_SUCCESS.getStatus()) {
                OrderCancelDTO orderCancelDTO = BeanUtils.toBean(orders, OrderCancelDTO.class);
                orderCancelDTO.setCurrentUserType(UserType.SYSTEM);
                orderCancelDTO.setCancelReason("订单超时未支付，自动取消");
                cancel(orderCancelDTO);
                orders = getById(orders.getId());
            }
        }
        return orders;
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
    public void cancel(OrderCancelDTO orderCancelDTO) {

        Orders orders = getById(orderCancelDTO.getId());
        BeanUtils.copyProperties(orders,orderCancelDTO);
        // 检验订单是否存在
        if (ObjectUtil.isNull(orders)) {
            throw new DbRuntimeException("找不到要取消的订单,订单号：{}",orderCancelDTO.getId());
        }
        Integer ordersStatus = orders.getOrdersStatus();
        // 订单状态未支付
        if(ordersStatus == OrderStatusEnum.NO_PAY.getStatus()) {
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
