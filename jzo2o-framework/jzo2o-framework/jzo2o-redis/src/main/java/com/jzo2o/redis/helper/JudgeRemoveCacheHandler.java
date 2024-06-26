package com.jzo2o.redis.helper;

import java.util.List;

public interface JudgeRemoveCacheHandler {

    /**
     * 判断是否删除缓存
     * @param params
     * @return
     */
    boolean judgeRemoveCache(List<Object> params);
}
