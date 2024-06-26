package com.jzo2o.redis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;


/**
 * @author Mr.M
 * @version 1.0
 * @description 分布式锁工具类
 * @date 2023/7/23 22:48
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Lock {


    /**
     * 加锁key的表达式，支持表达式
     */
    String formatter();

    /**
     * 加锁时长
     */
    long time() default 5;

    /**
     * 阻塞超时时间，默认2分钟,当block为true的时候生效
     */
    long waitTime() default 120;

    /**
     * 加锁时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 方法访问完后要不要解锁，默认自动解锁
     */
    boolean unlock() default true;

    /**
     * 如果设定了true将在获取锁时阻塞等待waitTime时间
     */
    boolean block() default false;

    /**
     * 是否启用自动续期,如果使用自动续期则unlock必须设置为true
     */
    boolean startDog() default false;

}
