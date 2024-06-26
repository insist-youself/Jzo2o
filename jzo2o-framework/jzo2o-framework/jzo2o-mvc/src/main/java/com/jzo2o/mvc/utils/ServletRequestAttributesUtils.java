package com.jzo2o.mvc.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author itcast
 */
public class ServletRequestAttributesUtils {

    /**
     * 获取
     * @return
     */
    public static ServletRequestAttributes getServletRequestAttributes() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ra == null) {
            return null;
        }
        return (ServletRequestAttributes) ra;
    }
}
