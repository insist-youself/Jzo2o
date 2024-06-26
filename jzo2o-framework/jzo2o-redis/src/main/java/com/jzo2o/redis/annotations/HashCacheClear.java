package com.jzo2o.redis.annotations;

import com.jzo2o.common.utils.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分页缓存中重要的字段，必须马上同步，
 * 单个缓存清理优先于批量处理，如果批量清理请不要设置dataId
 * @author 86188
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface HashCacheClear {

    /**
     * hash表的redisKey，支持aspl表达式，例如 evaluation_#{targetId},targetId的类中的字段
     * @return
     */
    String key() default StringUtils.EMPTY;

    /**
     * 无法直接获取到dataType时可以通过自定义适配器换取dataType,dataTypeAdapterName数据适配器名称
     * @return
     */
    String keyHandlerName() default StringUtils.EMPTY;

    /**
     * 无法直接获取到dataType时可以通过自定义适配器换取dataType,dataTypeConvertParamNames是转换方法入口参数
     *
     * @return
     */
    String[] keyHandlerParamNames() default {};


    /**
     * hashKey，支持aspl表达式，例如 evaluation_#{targetId},targetId的类中的字段
     * @return
     */
    String fieldId() default StringUtils.EMPTY;

    /**
     * 批量操作的模型类
     *
     * @return
     */
    int batchFieldIdsIndex() default -1;

    /**
     * 总是清理，该字段为true，fields无需设置
     *
     * @return
     */
    boolean always() default true;

    /**
     * 判断是否删除缓存的handler名称
     *
     * @return
     */
    String judgeRemoveCacheHandlerName() default StringUtils.EMPTY;

    /**
     * 判断是否删除缓存的handler
     * @return
     */
    String[] judgeRemoveCacheHandlerParamNames() default StringUtils.EMPTY;
}
