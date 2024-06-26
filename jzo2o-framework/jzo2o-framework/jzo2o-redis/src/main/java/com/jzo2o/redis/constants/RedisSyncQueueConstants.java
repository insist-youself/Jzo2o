package com.jzo2o.redis.constants;

public class RedisSyncQueueConstants {
    /**
     * hash - redis同步队列存储结构hash
     */
    public static final int STORAGE_TYPE_HASH = 1;
    /**
     * list - redis同步队列存储结构list
     */
    public static final int STORAGE_TYPE_LIST = 2;

    /**
     * zset - redis同步队列存储结构zset
     */
    public static final int STORAGE_TYPE_ZSET = 3;

    /**
     * 单条执行模式
     */
    public static final int MODE_SINGLE = 1;

    /**
     * 批量支持模式
     */
    public static final int MODE_BATCH = 2;
}
