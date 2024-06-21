package com.jzo2o.orders.manager.strategy;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.orders.base.enums.OrderStatusEnum;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.manager.service.IOrdersManagerService;
import com.jzo2o.orders.manager.model.dto.OrderCancelDTO;
import com.jzo2o.orders.manager.strategy.OrderCancelStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author itcast
 */
@Slf4j
@Component
public class OrderCancelStrategyManager {
    @Resource
    private IOrdersManagerService ordersManagerService;

    //key格式：userType+":"+orderStatusEnum，例：1：NO_PAY
    private final Map<String, OrderCancelStrategy> strategyMap = new HashMap<>();

    @PostConstruct
    public void init() {
        Map<String, OrderCancelStrategy> strategies = SpringUtil.getBeansOfType(OrderCancelStrategy.class);
        strategyMap.putAll(strategies);
        log.debug("订单取消策略类初始化到map完成！");
    }

    /**
     * 获取策略实现类
     *
     * @param userType    用户类型
     * @param orderStatus 订单状态
     * @return 策略实现类
     */
    public OrderCancelStrategy getStrategy(Integer userType, Integer orderStatus) {
        String key = userType + ":" + OrderStatusEnum.codeOf(orderStatus).toString();
        return strategyMap.get(key);
    }

    /**
     * 订单取消
     *
     * @param orderCancelDTO 订单取消模型
     */
    public void cancel(OrderCancelDTO orderCancelDTO) {
        Orders orders = ordersManagerService.queryById(orderCancelDTO.getId());
        OrderCancelStrategy strategy = getStrategy(orderCancelDTO.getCurrentUserType(), orders.getOrdersStatus());
        if (ObjectUtil.isEmpty(strategy)) {
            throw new ForbiddenOperationException("不被许可的操作");
        }

        orderCancelDTO.setUserId(orders.getUserId());
        orderCancelDTO.setServeStartTime(orders.getServeStartTime());
        orderCancelDTO.setCityCode(orders.getCityCode());
        orderCancelDTO.setRealPayAmount(orders.getRealPayAmount());
        orderCancelDTO.setTradingOrderNo(orders.getTradingOrderNo());
        orderCancelDTO.setRealServeEndTime(orders.getRealServeEndTime());
        strategy.cancel(orderCancelDTO);
    }

}