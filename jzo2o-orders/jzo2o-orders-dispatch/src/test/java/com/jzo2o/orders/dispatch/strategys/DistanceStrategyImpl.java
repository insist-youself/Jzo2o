package com.jzo2o.orders.dispatch.strategys;

/**
 * @author Mr.M
 * @version 1.0
 * @description 先距离优先，距离相同再判断评分
 * @date 2023/11/24 11:56
 */
public class DistanceStrategyImpl extends AbstractStrategyImpl implements IProcessStrategy {
    @Override
    protected IProcessRule getRules() {
        //构建责任链,先距离优先，距离相同再判断评分
        IProcessRule rule = new ScoreRule(null);
        IProcessRule ruleChain = new DistanceRule(rule);
        return ruleChain;
    }
}
