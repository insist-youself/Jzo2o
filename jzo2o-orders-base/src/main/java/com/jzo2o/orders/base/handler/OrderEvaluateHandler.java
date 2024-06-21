//package com.jzo2o.orders.base.handler;
//
//import cn.hutool.db.DbRuntimeException;
//import com.jzo2o.orders.base.enums.OrderStatusEnum;
//import com.jzo2o.orders.base.model.dto.OrderSnapshotDTO;
//import com.jzo2o.orders.base.model.dto.OrderUpdateStatusDTO;
//import com.jzo2o.orders.base.service.IOrdersCommonService;
//import com.jzo2o.statemachine.core.StatusChangeEvent;
//import com.jzo2o.statemachine.core.StatusChangeHandler;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.time.LocalDateTime;
//
///**
// * 订单评价处理器
// *
// * @author itcast
// * @create 2023/8/17 18:08
// **/
//@Slf4j
//@Component("order_evaluate")
//public class OrderEvaluateHandler implements StatusChangeHandler<OrderSnapshotDTO> {
//    @Resource
//    private IOrdersCommonService ordersService;
//
//    /**
//     * 订单评价处理逻辑
//     *
//     * @param bizId   业务id
//     * @param bizSnapshot 快照
//     */
//    @Override
//    public void handler(String bizId, StatusChangeEvent statusChangeEventEnum, OrderSnapshotDTO bizSnapshot) {
//        log.info("订单评价事件处理逻辑开始，订单号：{}", bizId);
//        // 修改订单状态
//        OrderUpdateStatusDTO orderUpdateStatusDTO = OrderUpdateStatusDTO.builder().id(Long.valueOf(bizId))
//                .originStatus(OrderStatusEnum.NO_EVALUATION.getStatus())
//                .targetStatus(OrderStatusEnum.FINISHED.getStatus())
//                .evaluationTime(LocalDateTime.now())
//                .build();
//        int result = ordersService.updateStatus(orderUpdateStatusDTO);
//        if (result <= 0) {
//            throw new DbRuntimeException("订单评价事件处理失败");
//        }
//    }
//}
