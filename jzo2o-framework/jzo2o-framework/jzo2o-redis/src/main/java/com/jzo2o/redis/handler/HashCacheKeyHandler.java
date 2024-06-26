package com.jzo2o.redis.handler;

import java.util.List;

/**
 * @author 86188
 */
public interface HashCacheKeyHandler {

    /**
     * 获取redis hash表的key表达式
     *
     * @param params 适配器入参
     * @return dataType
     */
    String key(List<Object> params);
}
