package com.jzo2o.orders.dispatch.rules.impl;

import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.orders.dispatch.model.dto.ServeProviderDTO;
import com.jzo2o.orders.dispatch.rules.IDispatchRule;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 按最少接单优先排序
 */
@Setter
@Slf4j
public class AcceptNumDispatchRule extends AbstractIDispatchRule {

    public AcceptNumDispatchRule(IDispatchRule next) {
        super(next);
    }

    @Override
    public List<ServeProviderDTO> doFilter(List<ServeProviderDTO> originServeProviderDTOS) {
        // 1.判断originServeProviderDTOS列表是否少于2，少于2直接返回
        if (CollUtils.size(originServeProviderDTOS) < 2) {
            return originServeProviderDTOS;
        }
        //  2.按照比较器进行排序，排在最前方优先级最高
        originServeProviderDTOS = originServeProviderDTOS.stream().sorted(Comparator.comparing(ServeProviderDTO::getAcceptanceNum)).collect(Collectors.toList());
        // 3.遍历优先级最高一批数据
        ServeProviderDTO first = CollUtils.getFirst(originServeProviderDTOS);

        //获取相同级别的
        return originServeProviderDTOS.stream()
                .filter(origin -> Comparator.comparing(ServeProviderDTO::getAcceptanceNum).compare(origin, first) == 0)
                .collect(Collectors.toList());
    }
}
