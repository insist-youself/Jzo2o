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
 * 最少接单优先策略
 * 先根据接单计算得分，每接1单等于1分，分值越高优先级越低，如果最高优先级数量大于1时按最少接单数规则计算得分，依次类推，算规则执行顺序如下：
 * 按最少接单数规则->按评分计算规则
 */
@Component("leastAcceptOrderDispatchStrategy")
@DispatchStrategy(DispatchStrategyEnum.LEAST_ACCEPT_ORDER)
public class LeastAcceptOrderDispatchStrategyImpl extends AbstractDispatchStrategyImpl {
    @Override
    protected IDispatchRule getRules() {
        // 按评分计算规则,评分越高优先级越高
        IDispatchRule evaluationDispatchRule = new EvaluationScoreDispatchRule(null);
        // 按最少接单数规则,接单数量越少优先级越高
        IDispatchRule acceptNumDispatchRule = new AcceptNumDispatchRule(evaluationDispatchRule);
        return acceptNumDispatchRule;
//        // 按评分计算规则,评分越高优先级越高
//        IDispatchRule evaluationDispatchRule = new DefaultIDispatchRule(null, ComparatorUtils.nullToFirstComparing(ServeProviderDTO::getEvaluationScore).reversed());
//        // 按最少接单数规则,接单数量越少优先级越高
//        IDispatchRule leastAcceptOrderRule = new DefaultIDispatchRule(evaluationDispatchRule, ComparatorUtils.nullToFirstComparing(ServeProviderDTO::getAcceptanceNum));
//        return leastAcceptOrderRule;
    }
}
