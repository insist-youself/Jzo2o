package com.jzo2o.trade.annotation;

import com.jzo2o.api.trade.enums.PayChannelEnum;

import java.lang.annotation.*;

/**
 * @author itcast
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented //标记注解
public @interface PayChannel {

    PayChannelEnum type();

}