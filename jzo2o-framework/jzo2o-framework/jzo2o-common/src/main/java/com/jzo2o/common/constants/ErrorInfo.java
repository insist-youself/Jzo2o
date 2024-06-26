package com.jzo2o.common.constants;

/**
 * 通用异常信息，只放系统相关异常信息
 *
 * @author itcast
 */
public class ErrorInfo {
    public static class Msg {
        public static final String PROCESS_FAILD = "失败";
        /**
         * 请求异常默认异常信息
         */
        public static final String REQUEST_PARAM_ILLEGAL = "请求参数不合法";
        /**
         * 频繁请求
         */
        public static final String REQUEST_OPERATE_FREQUENTLY = "操作频繁,请稍后重试";
        /**
         * 请求超时
         */
        public static final String REQUEST_TIME_OUT = "请求超时";

        /**
         * 请求失败
         */
        public static final String REQUEST_FAILD = "请求失败";

        public static final String REQUEST_FORBIDDEN = "拒绝访问";

        public static final String REQUEST_UNAUTHORIZED = "身份未识别";

        public static final String NO_PERMISSIONS = "无访问权限";

        public static final String FORBIDDEN_OPERATION = "禁止操作";
    }

    public static class Code {

        /**
         * 未登录
         */
        public static final int NOT_LOGIN = 600;

        /**
         * 登录过期
         */
        public static final int LOGIN_TIMEOUT = 601;

        /**
         * token不合法
         */
        public static final int ILLEGAL_TOKEN = 602;

        /**
         * 无权限访问
         */
        public static final int NO_PERMISSIONS = 603;

        /**
         * 禁止操作
         */
        public static final int FORBIDDEN_OPERATION = 604;

        /**
         * 账号冻结
         */
        public static final int ACCOUNT_FREEZED = 605;

        /**
         * 派单拒单
         */
        public static final int DISPATCH_REJECT = 606;

        /**
         * 机构/服务端取消订单失败
         */
        public static final int ORDERS_CANCEL = 607;

        /**
         * 抢单失败
         */
        public static final int SEIZE_ORDERS_FAILD = 608;

        /**
         * 交易失败
         */
        public static final int TRADE_FAILED = 609;

        /**
         * 对接评价系统http请求失败
         */
        public static final int HTTP_EVALUATION_FAILED = 610;

        /**
         * 抢券失败
         */
        public static final int SEIZE_COUPON_FAILD = 611;
    }
}
