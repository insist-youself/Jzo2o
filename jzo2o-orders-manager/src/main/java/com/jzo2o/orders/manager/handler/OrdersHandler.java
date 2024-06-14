package com.jzo2o.orders.manager.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jzo2o.api.trade.RefundRecordApi;
import com.jzo2o.api.trade.dto.response.ExecutionResultResDTO;
import com.jzo2o.api.trade.enums.RefundStatusEnum;
import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.orders.base.enums.OrderRefundStatusEnum;
import com.jzo2o.orders.base.mapper.OrdersMapper;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.base.model.domain.OrdersRefund;
import com.jzo2o.orders.manager.model.dto.OrderCancelDTO;
import com.jzo2o.orders.manager.service.IOrdersCreateService;
import com.jzo2o.orders.manager.service.IOrdersManagerService;
import com.jzo2o.orders.manager.service.IOrdersRefundService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单相关定时任务
 *
 * @author linger
 * @date 2024/5/31 16:49
 */

@Component
@Slf4j
public class OrdersHandler {

    @Resource
    private IOrdersCreateService ordersCreateService;

    @Resource
    private IOrdersManagerService ordersManagerService;

    @Resource
    private RefundRecordApi refundRecordApi;
    //解决同级方法调用，事务失效问题
    @Resource
    private OrdersHandler ordersHandler;
    @Resource
    private IOrdersRefundService ordersRefundService;

    @Resource
    private OrdersMapper ordersMapper;

    /**
     * 支付超时取消订单
     * 每分钟执行一次
     */
    @XxlJob(value = "cancelOverTimePayOrder")
    public void cancelOverTimePayOrder() {

        // 查询支付超时状态订单
        List<Orders> ordersList = ordersCreateService.queryOverTimePayOrdersListByCount(100);
        if(CollUtil.isEmpty(ordersList)) {
            XxlJobHelper.log("查询支付超时订单列表为空");
            return;
        }
        for (Orders orders : ordersList) {
            // 取消订单
            OrderCancelDTO orderCancelDTO = BeanUtils.copyBean(orders, OrderCancelDTO.class);
            orderCancelDTO.setCurrentUserType(UserType.SYSTEM);
            orderCancelDTO.setCancelReason("订单支付超时，自动取消");
            ordersManagerService.cancel(orderCancelDTO);
        }
    }



    /**
     * 订单退款异步任务
     */
    @XxlJob(value = "handleRefundOrders")
    public void handleRefundOrders() {
        // 查询退款中订单
        List<OrdersRefund> ordersRefundList = ordersRefundService.queryRefundOrderListByCount(100);
        for (OrdersRefund ordersRefund : ordersRefundList) {
            // 请求退款
            requestRefundOrder(ordersRefund);
        }
    }

    /**
     * 请求退款
     * @param ordersRefund 退款记录
     */
    public void requestRefundOrder(OrdersRefund ordersRefund) {
        // 调用第三方进行退款
        ExecutionResultResDTO executionResultResDTO = null;
        try {
            executionResultResDTO = refundRecordApi.refundTrading(ordersRefund.getTradingOrderNo(), ordersRefund.getRealPayAmount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (executionResultResDTO != null) {
            //退款后 处理订单相关信息
            ordersHandler.refundOrder(ordersRefund, executionResultResDTO);
        }
    }


    /**
     * 新启动一个线程请求退款
     * @param ordersRefundId
     */
    public void requestRefundNewThread(Long ordersRefundId) {
        // 启动一个线程请求第三方退款接口
        new Thread(() -> {
            // 查询退款记录
            OrdersRefund ordersRefund = ordersRefundService.getById(ordersRefundId);
            if (ObjectUtil.isNotNull(ordersRefund)) {
                // 请求退款
                requestRefundOrder(ordersRefund);
            }
            // 请求退款
        }).start();
    }


    /**
     * 更新退款状态
     * @param ordersRefund
     * @param executionResultResDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public void refundOrder(OrdersRefund ordersRefund, ExecutionResultResDTO executionResultResDTO){
        // 根据响应结果更新退款状态
        int refundStatus = OrderRefundStatusEnum.REFUNDING.getStatus();// 退款中
        if (ObjectUtil.equal(RefundStatusEnum.SUCCESS.getCode(), executionResultResDTO.getRefundStatus())) {
            // 退款成功
            refundStatus = OrderRefundStatusEnum.REFUND_SUCCESS.getStatus();
        } else if (ObjectUtil.equal(RefundStatusEnum.FAIL.getCode(), executionResultResDTO.getRefundStatus())) {
            // 退款失败
            refundStatus = OrderRefundStatusEnum.REFUND_FAIL.getStatus();
        }
        //如果是退款中状态，程序结束
        if(ObjectUtil.equal(refundStatus, OrderRefundStatusEnum.REFUNDING.getStatus())) {
            return;
        }
        //非退款中状态，更新订单的退款状态
        LambdaUpdateWrapper<Orders> lambdaUpdateWrapper = new LambdaUpdateWrapper<Orders>()
                .eq(Orders::getId, ordersRefund.getId())
                .ne(Orders::getRefundStatus, refundStatus)
                .set(Orders::getRefundStatus, refundStatus)
                .set(ObjectUtil.isNotEmpty(executionResultResDTO.getRefundId()), Orders::getRefundId, executionResultResDTO.getRefundId())
                .set(ObjectUtil.isNotEmpty(executionResultResDTO.getRefundNo()), Orders::getRefundNo, executionResultResDTO.getRefundNo());
        int update = ordersMapper.update(null, lambdaUpdateWrapper);
        // 非退款中状态，删除申请退款记录，删除后定时任务不再扫描
        if(update > 0) {
            ordersRefundService.removeById(ordersRefund.getId());
        }
    }
}
