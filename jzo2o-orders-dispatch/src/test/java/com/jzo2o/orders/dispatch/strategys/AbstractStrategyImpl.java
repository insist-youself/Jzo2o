package com.jzo2o.orders.dispatch.strategys;

import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.orders.dispatch.model.dto.ServeProviderDTO;
import com.jzo2o.orders.dispatch.rules.IDispatchRule;

import java.util.List;
import java.util.Objects;

/**
 * @author Mr.M
 * @version 1.0
 * @description 抽象策略类
 * @date 2023/11/24 11:53
 */
public abstract class AbstractStrategyImpl implements IProcessStrategy {

    private final IProcessRule processRule;

    public AbstractStrategyImpl() {
        this.processRule = getRules();
    }

    /**
     * 设置派单规则
     *
     * @return
     */
    protected abstract IProcessRule getRules();

    @Override
    public ServeProviderDTO getPrecedenceServeProvider(List<ServeProviderDTO> serveProviderDTOS) {
        // 1.判空
        if (CollUtils.isEmpty(serveProviderDTOS)) {
            return null;
        }
        IProcessRule dr = processRule;

        // 2.根据优先级获取高优先级别的
        serveProviderDTOS = dr.filter(serveProviderDTOS);

        // 3.数据返回
        // 3.1.唯一高优先级直接返回
        int size = 1;
        if ((size = CollUtils.size(serveProviderDTOS)) == 1) {
            return serveProviderDTOS.get(0);
        }
        // 3.2.多个高优先级随即将返回
        int randomIndex = (int) (Math.random() * size);
        return serveProviderDTOS.get(randomIndex);
    }
}
