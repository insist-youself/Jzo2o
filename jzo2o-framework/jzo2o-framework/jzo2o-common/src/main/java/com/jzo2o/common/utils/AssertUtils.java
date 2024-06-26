package com.jzo2o.common.utils;

import com.jzo2o.common.constants.ErrorInfo;
import com.jzo2o.common.expcetions.BadRequestException;

import java.util.Map;

/**
 * 断言工具类，符合条件不会抛出异常，不符合条件则会抛出异常
 *
 * @author itheima
 */
public class AssertUtils {
    /**
     * 两个对象相同，不同则会抛出异常
     *
     * @param obj1    duixiang1
     * @param obj2    对象2
     * @param message 异常信息
     */
    public static void equals(Object obj1, Object obj2, String... message) {
        if (obj1 == null || obj2 == null) {
            handleException(message);
            return;
        }
        if (obj1 == obj2) {
            return;
        }
        if (!obj1.equals(obj2)) {
            handleException(message);
        }
    }

    /**
     * 判断对象不为null，为null抛出异常
     *
     * @param obj     判断对象
     * @param message 异常信息
     */
    public static void isNotNull(Object obj, String... message) {
        if (obj == null) {
            handleException(message);
        }
    }

    /**
     * 判断字符串不为空，为空抛出异常
     *
     * @param str     判空字符串
     * @param message 异常信息
     */
    public static void isNotBlank(String str, String... message) {
        if (StringUtils.isBlank(str)) {
            handleException(message);
        }
    }

    /**
     * 判断boolean对象是否为真，为真不抛出异常
     *
     * @param boo     boolean对象
     * @param message 异常信息
     */
    public static void isTrue(Boolean boo, String... message) {
        if (!BooleanUtils.isTrue(boo)) {
            handleException(message);
        }
    }

    /**
     * 判断集合是否不为空，为空抛出异常
     *
     * @param coll    判空集合对象
     * @param message 异常信息
     */
    public static void isNotEmpty(Iterable<?> coll, String... message) {
        if (CollUtils.isEmpty(coll)) {
            handleException(message);
        }
    }

    /**
     * 判断map是否不为空，为空抛出异常
     *
     * @param map     判不为空map
     * @param message 异常信息
     */
    public static void isNotEmpty(Map<?, ?> map, String... message) {
        if (CollUtils.isEmpty(map)) {
            handleException(message);
        }
    }


    /**
     * 判断boolean对象是否为假，为假则不抛出异常
     *
     * @param boo     boolean对象
     * @param message 异常信息
     */
    public static void isFalse(Boolean boo, String... message) {
        if (!BooleanUtils.isFalse(boo)) {
            handleException(message);
        }
    }

    /**
     * 判断一个值是否是指定值中的一个，没有匹配到抛出异常信息
     *
     * @param t 需要比较的数据
     * @param message 抛出异常信息
     * @param targets 比较集合
     * @param <T> 比较数据类型
     */
    public static <T> void in(T t,String message, T ... targets) {
        for (T target : targets) {
            if(t.equals(target)) {
               return;
            }
        }
        // 没有匹配到抛出指定信息
        handleException(message);
    }

    /**
     * 判断一个值是否是指定值不被包含在集合中，没有匹配到抛出异常信息
     *
     * @param t 需要比较的数据
     * @param message 抛出异常信息
     * @param targets 比较集合中
     * @param <T> 比较数据类型
     */
    public static <T> void notIn(T t,String message, T ... targets) {
        for (T target : targets) {
            if(t.equals(target)) {
                // 信息被匹配到抛出异常信息
                handleException(message);
            }
        }

    }
    /**
     * 异常信息处理
     *
     * @param message 异常信息
     */
    private static void handleException(String... message) {
        String msg = ErrorInfo.Msg.REQUEST_PARAM_ILLEGAL;
        if (message != null && message.length > 0) {
            msg = message[0];
        }
        throw new BadRequestException(msg);
    }


}
