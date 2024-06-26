package com.jzo2o.orders.base.constants;

public class ErrorInfo {
    public static class Msg {
        public static final String SEIZE_ORDERS_FAILD = "单子已经被抢走了";
        public static final String SEIZE_ORDERS_SERVE_TIME_EXISTS = "当前服务预约时间冲突";
        public static final String SEIZE_ORDERS_RECEIVE_ORDERS_NUM_OVER = "当前接单数已超出范围";

        public static final String SEIZE_ORDERS_RECEIVE_CLOSED = "当前接单设置已关闭，如需抢单，需开启接单";

    }
}
