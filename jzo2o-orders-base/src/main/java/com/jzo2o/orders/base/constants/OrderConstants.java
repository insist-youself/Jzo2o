package com.jzo2o.orders.base.constants;

/**
 * 订单常量
 *
 * @author itcast
 * @create 2023/7/28 15:06
 **/
public class OrderConstants {
    public static final class Status {
        public static final String NO_PAY = "NO_PAY";
        public static final String DISPATCHING = "DISPATCHING";
        public static final String NO_SERVE = "NO_SERVE";
        public static final String SERVING = "SERVING";
        public static final String NO_EVALUATION = "NO_EVALUATION";
        public static final String FINISHED = "FINISHED";
        public static final String CANCELED = "CANCELED";
        public static final String CHARGEBACK = "CHARGEBACK";
    }

    public static final class ChangeEvent {
        public static final String PAYED = "PAYED";
        public static final String DISPATCH = "DISPATCH";
        public static final String START_SERVE = "START_SERVE";
        public static final String COMPLETE_SERVE = "COMPLETE_SERVE";
        public static final String EVALUATE = "EVALUATE";
        public static final String CANCEL = "CANCEL";
        public static final String SERVE_PROVIDER_CANCEL = "SERVE_PROVIDER_CANCEL";

        public static final String CLOSE_DISPATCHING_ORDER = "CLOSE_DISPATCHING_ORDER";
        public static final String CLOSE_NO_SERVE_ORDER = "CLOSE_NO_SERVE_ORDER";
        public static final String CLOSE_SERVING_ORDER = "CLOSE_SERVING_ORDER";
        public static final String CLOSE_NO_EVALUATION_ORDER = "CLOSE_NO_EVALUATION_ORDER";
        public static final String CLOSE_FINISHED_ORDER = "CLOSE_FINISHED_ORDER";
    }
}
