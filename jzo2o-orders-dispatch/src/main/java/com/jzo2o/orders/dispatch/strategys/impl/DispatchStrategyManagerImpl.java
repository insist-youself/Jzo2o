package com.jzo2o.orders.dispatch.strategys.impl;

import com.jzo2o.orders.dispatch.enums.DispatchStrategyEnum;
import com.jzo2o.orders.dispatch.strategys.IDispatchStrategy;
import com.jzo2o.orders.dispatch.strategys.IDispatchStrategyManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DispatchStrategyManagerImpl implements IDispatchStrategyManager {
    private static final Map<DispatchStrategyEnum, IDispatchStrategy> DISPATCH_STRATEGY_MAP = new HashMap<>(8);

    @Override
    public void put(DispatchStrategyEnum dispatchStrategyEnum, IDispatchStrategy dispatchStrategy) {
        DISPATCH_STRATEGY_MAP.put(dispatchStrategyEnum, dispatchStrategy);
    }

    @Override
    public IDispatchStrategy get(DispatchStrategyEnum dispatchStrategyEnum) {
        return DISPATCH_STRATEGY_MAP.get(dispatchStrategyEnum);
    }
}
