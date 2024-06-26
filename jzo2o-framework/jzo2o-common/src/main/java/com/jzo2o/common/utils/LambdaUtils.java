package com.jzo2o.common.utils;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;

public class LambdaUtils extends LambdaUtil {

    /**
     * 获取lambda表达式Getter或Setter函数（方法）对应的字段名称,并转换成下划线格式，规则如下：
     * getXxxx获取为xxxx，如getName得到name。
     * setXxxx获取为xxxx，如setName得到name。
     * isXxxx获取为xxxx，如isName得到name。
     * 其它不满足规则的方法名抛出IllegalArgumentException
     */
    public static <T> String getUnderLineFieldName(Func1<T, ?> func) throws IllegalArgumentException {
        String fieldName = getFieldName(func);
        return StringUtils.isEmpty(fieldName) ? null : StringUtils.toUnderlineCase(fieldName);
    }
}
