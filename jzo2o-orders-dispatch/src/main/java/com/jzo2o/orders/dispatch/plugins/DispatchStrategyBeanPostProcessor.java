package com.jzo2o.orders.dispatch.plugins;

import cn.hutool.core.util.ArrayUtil;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.orders.dispatch.annotations.DispatchStrategy;
import com.jzo2o.orders.dispatch.strategys.IDispatchStrategy;
import com.jzo2o.orders.dispatch.strategys.IDispatchStrategyManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class DispatchStrategyBeanPostProcessor implements BeanPostProcessor {

    private final IDispatchStrategyManager dispatchStrategyManager;

    public DispatchStrategyBeanPostProcessor(IDispatchStrategyManager dispatchStrategyManager) {
        this.dispatchStrategyManager = dispatchStrategyManager;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof IDispatchStrategy) {
            // 派单策略bean
            DispatchStrategy dispatchStrategy = bean.getClass().getAnnotation(DispatchStrategy.class);
            if(dispatchStrategy == null) {
                throw new CommonException("未定义完整派单策略...");
            }
            dispatchStrategyManager.put(ArrayUtil.get(dispatchStrategy.value(), 0), (IDispatchStrategy) bean);
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}
