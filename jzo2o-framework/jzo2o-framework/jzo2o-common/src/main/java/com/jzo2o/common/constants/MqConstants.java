package com.jzo2o.common.constants;

/**
 * 静态变量
 *
 * @author zzj
 * @version 1.0
 */
public interface MqConstants {
    /**
     * 默认延时时间为-1
     */
    int DEFAULT_DELAY = -1;

    /**
     * 低延迟时间：5秒
     */
    int LOW_DELAY = 5000;

    /**
     * 标准延迟时间：10秒
     */
    int NORMAL_DELAY = 10000;

    /**
     * 延迟交换机关键字
     */
    String DELAYED_KEYWORD = "delayed";

    /**
     * 表明是延迟队列
     */
    String DELAYED = "true";

    /**
     * 定义消息交换机，约定：1：类型都为topic，2：延迟队列命名由.delayed结尾
     */
    interface Exchanges {

        /**
         * 交易（支付）
         */
        String TRADE = "jzo2o.exchange.topic.trade";

        /**
         * 评分
         */
        String EVALUATION_SCORE = "evaluation.score";
    }

    /**
     * 定义消息队列
     */
    interface Queues {

        /**
         * 订单微服务：更新支付状态
         */
        String ORDERS_TRADE_UPDATE_STATUS = "jzo2o.queue.orders.trade.update.Status";

        /**
         * 订单微服务：更新支付状态
         */
        String SCORE_STATISTICS = "jzo2o.queue.customer.score.statistics";
    }

    /**
     * 定义路由key
     */
    interface RoutingKeys {

        /**
         * 更新支付状态
         */
        String TRADE_UPDATE_STATUS = "UPDATE_STATUS";

        /**
         * 分数统计
         */
        String SCORE_STATISTICS = "evaluation.score.statistics";
    }
}
