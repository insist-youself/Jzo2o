package com.jzo2o.orders.dispatch.strategys.impl;

import com.jzo2o.common.utils.ComparatorUtils;
import com.jzo2o.orders.dispatch.annotations.DispatchStrategy;
import com.jzo2o.orders.dispatch.enums.DispatchStrategyEnum;
import com.jzo2o.orders.dispatch.model.dto.ServeProviderDTO;
import com.jzo2o.orders.dispatch.rules.IDispatchRule;
import com.jzo2o.orders.dispatch.rules.impl.AcceptNumDispatchRule;
import com.jzo2o.orders.dispatch.rules.impl.DefaultIDispatchRule;
import com.jzo2o.orders.dispatch.rules.impl.EvaluationScoreDispatchRule;
import org.springframework.stereotype.Component;

/**
 * 评分优先策略
 * 先根据评分规则获取高优先级的服务人员或机构，评分越高优先级越高，如果存在多个最高优先级的服务人员或机构再去进行最少
 * 按评分计算->按最少接单数计算
 */
@Component("evaluationScoreDispatchStrategy")
@DispatchStrategy(DispatchStrategyEnum.EVELUATION_SCORE)
public class EvaluationScoreDispatchStrategyImpl extends AbstractDispatchStrategyImpl{
    @Override
    protected IDispatchRule getRules() {
        // 最少接单规则，数量越少优先级越高
        IDispatchRule acceptNumDispatchRule = new AcceptNumDispatchRule(null);
        // 按评分计算规则,评分越高优先级越高
        IDispatchRule evaluationScoreDispatchRule = new EvaluationScoreDispatchRule(acceptNumDispatchRule);
        return evaluationScoreDispatchRule;
//        // 最少接单规则，数量越少优先级越高
//        DefaultIDispatchRule leastAcceptOrderNumDispatchRule = new DefaultIDispatchRule(null, ComparatorUtils.nullToFirstComparing(ServeProviderDTO::getAcceptanceNum));
//        // 按评分计算规则,评分越高优先级越高
//        IDispatchRule evaluationScoreDispatchRule = new DefaultIDispatchRule(leastAcceptOrderNumDispatchRule, ComparatorUtils.nullToFirstComparing(ServeProviderDTO::getEvaluationScore).reversed());
//        return evaluationScoreDispatchRule;
    }
}
