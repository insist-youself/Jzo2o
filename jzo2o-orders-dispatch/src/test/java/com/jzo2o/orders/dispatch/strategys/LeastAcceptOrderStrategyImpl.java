//package com.jzo2o.orders.dispatch.strategys;
//
///**
// * @author Mr.M
// * @version 1.0
// * @description 最少接单优先
// * @date 2023/11/24 11:59
// */
//public class LeastAcceptOrderStrategyImpl extends AbstractStrategyImpl implements IProcessStrategy {
//    @Override
//    protected IProcessRule getRules() {
//        // 构建责任链,先接单数优先，接单数相同再判断评分
//        IProcessRule rule = new ScoreRule(null);
//        IProcessRule ruleChain = new AcceptNumRule(rule);
//        return ruleChain;
//    }
//}
