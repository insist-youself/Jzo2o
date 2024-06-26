package com.jzo2o.common.utils;

import cn.hutool.core.util.StrUtil;

/**
 * 字符串工具类,继承了{@link StrUtil}
 **/
public class StringUtils extends StrUtil {
    private static final byte[] TRUE = new byte[]{'T','R','U','E'};
    /**
     * 判断是否是字符串
     * @param bytes
     * @return
     */
    public static boolean isStr(byte[] bytes) {
        if(TRUE.equals(bytes)) {
            return false;
        }
        for (byte word : bytes) {
            //判断是数字
            if(word >= 48 && word <= 57) {
                continue;
            }
            return true;
        }
        return false;
    }
}