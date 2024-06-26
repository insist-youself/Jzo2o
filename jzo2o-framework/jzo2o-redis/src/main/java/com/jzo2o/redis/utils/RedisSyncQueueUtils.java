package com.jzo2o.redis.utils;

public class RedisSyncQueueUtils {
    private static final String QUEUE_REDIS_KEY_FORMAT = "QUEUE:%s:{%s}";

    /**
     * 获取redis队列redisKey
     *
     * @param queueName redis队列名称
     * @param index 队列序号
     * @return
     */
    public static String getQueueRedisKey(String queueName, int index){
        return String.format(QUEUE_REDIS_KEY_FORMAT, queueName, index);
    }
}
