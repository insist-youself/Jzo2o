package com.jzo2o.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Object操作工具
 **/
public class ObjectUtils extends ObjectUtil {

    /**
     * 获取对象t的某个字段值
     *
     * @param t 获取对象t
     * @param function labda表达式，例如Orders::get
     * @return 对象t的某个字段值
     * @param <T> 对象t的类型
     * @param <R> 对象t对应字段的类型
     */
    public static <T,R> R get(T t, Function<T,R> function) {
        if(t == null) {
            return null;
        }
        return function.apply(t);
    }

    /**
     * 转换成指定类型数据
     * @param t 转换前对象t
     * @param clazz 转换目标对象class
     * @return 转换后的对象或数据
     * @param <T> 转换前对象类型
     * @param <R> 转换后对象类型
     */
    public static <T,R> R parse(T t, Class<R> clazz) {
        if(t == null) {
            return null;
        }
        if(!ClassUtils.equals(clazz, ClassUtils.getClassName(t, false), false)){
            throw new RuntimeException("数据转换异常，数据转换类型错误");
        }
        return (R)t;
    }

    /**
     * 如果有值直接返回，无值返回默认值
     *
     * @param originObject 原始值
     * @param defaultObject 默认值
     * @return T类型的对象
     * @param <T> 返回值和入参的类型
     */
    public static <T> T defaultIfEmpty(final T originObject, final T defaultObject) {
        return isNull(originObject) ? defaultObject : originObject;
    }
}
