//package com.jzo2o.orders.dispatch.strategys;
//
///**
// * @author Mr.M
// * @version 1.0
// * @description 先评分优先，评分相同再判断接单数
// * @date 2023/11/24 12:00
// */
//public class EvaluationScoreStrategyImpl extends AbstractStrategyImpl implements IProcessStrategy {
//    @Override
//    protected IProcessRule getRules() {
//        // 构建责任链,先评分优先，评分相同再判断接单数
//        IProcessRule rule = new AcceptNumRule(null);
//        IProcessRule ruleChain = new ScoreRule(rule);
//        return ruleChain;
//    }
//}
