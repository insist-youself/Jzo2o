package com.jzo2o.common.constants;

/**
 * 请求/响应header常量
 */
public class HeaderConstants {
    /**
     * 当前用户信息
     */
    public static final String USER_INFO = "USER-INFO";

    /**
     * 当前用户类型
     */
    public static final String USER_TYPE = "USER-TYPE";

    /**
     * 异常捕获标识位
     */
    public static final String EXCEPTION_CATCH_FLAG = "EXCEPTION-CATCH-FLAG";

    /**
     * 异常标识 1 - 未见异常
     */
    public static final String EXCEPTION_CATCH_FLAG_1 = "1";
    /**
     * 异常标识 2 - 异常
     */
    public static final String EXCEPTION_CATCH_FLAG_2 = "2";


    /**
     * 请求id
     */
    public static final String REQUEST_ID = "REQUEST-ID";

    /**
     * 请求来源标识，1:外界访问，2：内部访问
     */
    public static final String REQUEST_ORIGIN_FLAG = "REQUEST-ORIGIN-FLAG";

    /**
     * 访问来源标识 1： 外界访问
     */
    public static final String REQUEST_ORIGIN_FLAG_OUTSIDE = "1";

    /**
     * 访问来源标识 2：内部访问
     */
    public static final String REQUEST_ORIGIN_FLAG_INNER = "2";
}
