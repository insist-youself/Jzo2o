package com.jzo2o.orders.manager.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.jzo2o.api.customer.EvaluationApi;
import com.jzo2o.api.customer.dto.request.EvaluationSubmitReqDTO;
import com.jzo2o.api.trade.RefundRecordApi;
import com.jzo2o.api.trade.dto.response.ExecutionResultResDTO;
import com.jzo2o.api.trade.enums.RefundStatusEnum;
import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.utils.NumberUtils;
import com.jzo2o.orders.base.config.OrderStateMachine;
import com.jzo2o.orders.base.enums.OrderRefundStatusEnum;
import com.jzo2o.orders.base.enums.OrderStatusChangeEventEnum;
import com.jzo2o.orders.base.enums.OrderStatusEnum;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.base.model.domain.OrdersRefund;
import com.jzo2o.orders.base.model.dto.OrderSnapshotDTO;
import com.jzo2o.orders.base.service.IHistoryOrdersSyncCommonService;
import com.jzo2o.orders.manager.model.dto.OrderCancelDTO;
import com.jzo2o.orders.manager.porperties.OrdersJobProperties;
import com.jzo2o.orders.manager.service.IOrdersCreateService;
import com.jzo2o.orders.manager.service.IOrdersManagerService;
import com.jzo2o.orders.manager.service.IOrdersRefundService;
import com.jzo2o.orders.manager.service.SimulateService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单相关定时任务
 *
 * @author itcast
 * @create 2023/9/2 16:44
 **/
@Slf4j
@Component
public class OrdersHandler {
    @Resource
    private IOrdersCreateService ordersCreateService;
    @Resource
    private IOrdersManagerService ordersManagerService;
    @Resource
    private OrderStateMachine orderStateMachine;
    @Resource
    private RefundRecordApi refundRecordApi;
    //解决同级方法调用，事务失效问题
    @Resource
    private OrdersHandler orderHandler;
    @Resource
    private IOrdersRefundService ordersRefundService;
    @Resource
    private OrdersJobProperties ordersJobProperties;
    @Resource
    private EvaluationApi evaluationApi;

//    @Resource
//    private SimulateService simulateService;

    @Resource
    private IHistoryOrdersSyncCommonService historyOrdersSyncCommonService;

    /**
     * 支付超时取消订单
     * 每分钟执行一次
     */
    @XxlJob(value = "cancelOverTimePayOrder")
    public void cancelOverTimePayOrder() {
        //查询支付超时状态订单
        Integer overTimePayOrderCount = ordersJobProperties.getOverTimePayOrderCount();
        List<Orders> ordersList = ordersCreateService.queryOverTimePayOrdersListByCount(overTimePayOrderCount);
        if (CollUtil.isEmpty(ordersList)) {
            XxlJobHelper.log("查询到订单列表为空！");
            return;
        }

        for (Orders orders : ordersList) {

            //取消订单
            OrderCancelDTO orderCancelDTO = BeanUtil.toBean(orders, OrderCancelDTO.class);
            orderCancelDTO.setCurrentUserType(UserType.SYSTEM);
            orderCancelDTO.setCancelReason("订单超时支付，自动取消");
            ordersManagerService.cancel(orderCancelDTO);
        }
    }


    /**
     * 订单退款异步任务
     */
    @XxlJob(value = "handleRefundOrders")
    public void handleRefundOrders() {
        //查询退款中订单
        List<OrdersRefund> ordersRefundList = ordersRefundService.queryRefundOrderListByCount(100);
        for (OrdersRefund ordersRefund : ordersRefundList) {
            //请求退款
            requestRefundOrder(ordersRefund);
        }
    }

    /**
     * 新启动一个线程请求退款
     * @param ordersRefundId
     */
    public void requestRefundNewThread(Long ordersRefundId){
        //启动一个线程请求第三方退款接口
        new Thread(()->{
            //查询退款记录
            OrdersRefund ordersRefund = ordersRefundService.getById(ordersRefundId);
            if(ObjectUtil.isNotNull(ordersRefund)){
                //请求退款
                requestRefundOrder(ordersRefund);
            }
        }).start();
    }

    /**
     * 请求退款
     * @param ordersRefund 退款记录
     */
    public void requestRefundOrder(OrdersRefund ordersRefund){
        //调用第三方进行退款
        ExecutionResultResDTO executionResultResDTO = null;
        try {
            executionResultResDTO = refundRecordApi.refundTrading(ordersRefund.getTradingOrderNo(), ordersRefund.getRealPayAmount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(executionResultResDTO!=null){
            //退款后处理订单相关信息
            orderHandler.refundOrder(ordersRefund, executionResultResDTO);
        }
    }



    @Transactional(rollbackFor = Exception.class)
    public void refundOrder(OrdersRefund ordersRefund, ExecutionResultResDTO executionResultResDTO) {
        //根据响应结果更新支付状态
        int refundStatus = OrderRefundStatusEnum.REFUNDING.getStatus();//退款中
        if (ObjectUtil.equal(RefundStatusEnum.SUCCESS.getCode(), executionResultResDTO.getRefundStatus())) {
            //退款成功
            refundStatus = OrderRefundStatusEnum.REFUND_SUCCESS.getStatus();
        } else if (ObjectUtil.equal(RefundStatusEnum.FAIL.getCode(), executionResultResDTO.getRefundStatus())) {
            //退款失败
            refundStatus = OrderRefundStatusEnum.REFUND_FAIL.getStatus();
        }

        //如果是退款中状态，程序结束
        if (ObjectUtil.equal(refundStatus, OrderRefundStatusEnum.REFUNDING.getStatus())) {
            return;
        }

        //非退款中状态，更新订单的退款状态
        ordersCreateService.updateRefundStatus(ordersRefund.getId(), refundStatus, executionResultResDTO.getRefundId(), executionResultResDTO.getRefundNo());

        //非退款中状态，删除申请退款记录，删除后定时任务不再扫描
        ordersRefundService.removeById(ordersRefund.getId());

        //新增快照
        String jsonResult = orderStateMachine.getCurrentSnapshot(ordersRefund.getId().toString());
        OrderSnapshotDTO orderSnapshotDTO = JSONUtil.toBean(jsonResult, OrderSnapshotDTO.class);
        //退款状态
        orderSnapshotDTO.setRefundStatus(refundStatus);
        //支付服务的退款单号
        orderSnapshotDTO.setRefundNo(executionResultResDTO.getRefundNo());
        //第三方的退款id
        orderSnapshotDTO.setThirdRefundOrderId(executionResultResDTO.getRefundId());
        //保存快照
        orderStateMachine.saveSnapshot(orderSnapshotDTO.getUserId(), orderSnapshotDTO.getId().toString(), OrderStatusEnum.CLOSED, orderSnapshotDTO);
        // 统计历史订单信息
        historyOrdersSyncCommonService.writeHistorySync(ordersRefund.getId());
    }


    /**
     * 超过15日订单自动评价
     */
    @XxlJob(value = "autoEvaluate")
    public void autoEvaluate() {

        //每次批量查询订单数量
        Integer autoEvaluateCount = ordersJobProperties.getAutoEvaluateCount();
        while (true) {
            List<Orders> ordersList = ordersManagerService.queryOverTimeEvaluateOrdersList(autoEvaluateCount);
            if (CollUtil.isEmpty(ordersList)) {
                break;
            }

            for (Orders orders : ordersList) {
                //订单评价
                orderHandler.evaluateOrders(orders);
            }
        }
    }


    /**
     * 订单评价
     *
     * @param orders 订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void evaluateOrders(Orders orders) {
        //发起评价
        EvaluationSubmitReqDTO evaluationSubmitReqDTO = new EvaluationSubmitReqDTO();
        evaluationSubmitReqDTO.setUserType(UserType.C_USER);
        evaluationSubmitReqDTO.setUserId(orders.getUserId());
        evaluationSubmitReqDTO.setOrdersId(orders.getId());
        evaluationApi.autoEvaluate(evaluationSubmitReqDTO);
        //订单已评价
        ordersManagerService.evaluationOrder(orders.getId());
//        orderStateMachine.changeStatus(orders.getUserId(), orders.getId().toString(), OrderStatusChangeEventEnum.EVALUATE);
    }

    /**
     * 派单超时订单取消
     */
    @XxlJob(value = "cancelDispatchOverTimeOrders")
    public void cancelDispatchOverTimeOrders() {

        //查询派单超时订单
        Integer count = ordersJobProperties.getDispatchOverTimeOrderCount();
        List<Orders> ordersList = ordersManagerService.queryDispatchOverTimeOrdersList(count);
        if (CollUtil.isEmpty(ordersList)) {
            XxlJobHelper.log("查询到派单超时订单列表为空！");
            return;
        }

        for (Orders orders : ordersList) {
            //取消订单
            OrderCancelDTO orderCancelDTO = BeanUtil.toBean(orders, OrderCancelDTO.class);
            orderCancelDTO.setCurrentUserType(UserType.SYSTEM);
            orderCancelDTO.setCancelReason("派单超时取消");
            ordersManagerService.cancel(orderCancelDTO);
        }
    }

    /**
     * 模拟交易完成记录
     */
//    @XxlJob(value = "simulateFinished")
//    public void simulateFinished(String... params) {
//        log.info("模拟订单完成任务开始执行，参数：{}", params);
//        if (params == null || params.length <= 0) {
//            simulateService.simulateFinished();
//        } else {
//            for (int count = 0; count < NumberUtils.parseInt(params[0]); count++) {
//                try {
//                    simulateService.simulateFinished();
//                } catch (Exception e) {
//                    log.error("模拟交易失败，e:", e);
//                }
//            }
//        }
//        log.info("模拟订单完成任务结束");
//    }
}
