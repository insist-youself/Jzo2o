package com.jzo2o.orders.base.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.orders.base.mapper.OrdersMapper;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.base.model.dto.OrderUpdateStatusDTO;
import com.jzo2o.orders.base.service.IOrdersCommonService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-08-02
 */
@Service
public class OrdersCommonServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersCommonService {
    @Override
    public Integer updateStatus(OrderUpdateStatusDTO orderUpdateStatusReqDTO) {
        LambdaUpdateWrapper<Orders> updateWrapper = Wrappers.<Orders>lambdaUpdate()
                .eq(Orders::getId, orderUpdateStatusReqDTO.getId())
                .gt(Orders::getUserId, 0)
                .eq(ObjectUtil.isNotNull(orderUpdateStatusReqDTO.getOriginStatus()),Orders::getOrdersStatus,orderUpdateStatusReqDTO.getOriginStatus())
                .set(Orders::getOrdersStatus, orderUpdateStatusReqDTO.getTargetStatus())
                .set(ObjectUtil.isNotNull(orderUpdateStatusReqDTO.getPayStatus()),Orders::getPayStatus,orderUpdateStatusReqDTO.getPayStatus())
                .set(ObjectUtil.isNotNull(orderUpdateStatusReqDTO.getPayTime()),Orders::getPayTime,orderUpdateStatusReqDTO.getPayTime())
                .set(ObjectUtil.isNotNull(orderUpdateStatusReqDTO.getRealServeEndTime()),Orders::getRealServeEndTime,orderUpdateStatusReqDTO.getRealServeEndTime())
//                .set(ObjectUtil.isNotNull(orderUpdateStatusReqDTO.getEvaluationTime()),Orders::getEvaluationTime,orderUpdateStatusReqDTO.getEvaluationTime())
                .set(ObjectUtil.isNotNull(orderUpdateStatusReqDTO.getTradingOrderNo()),Orders::getTradingOrderNo,orderUpdateStatusReqDTO.getTradingOrderNo())
                .set(ObjectUtil.isNotNull(orderUpdateStatusReqDTO.getTransactionId()),Orders::getTransactionId,orderUpdateStatusReqDTO.getTransactionId())
                .set(ObjectUtil.isNotNull(orderUpdateStatusReqDTO.getTradingChannel()),Orders::getTradingChannel,orderUpdateStatusReqDTO.getTradingChannel())
                .set(ObjectUtil.isNotNull(orderUpdateStatusReqDTO.getRefundStatus()),Orders::getRefundStatus,orderUpdateStatusReqDTO.getRefundStatus());
        boolean update = super.update(updateWrapper);
        return update?1:0;
    }
}
