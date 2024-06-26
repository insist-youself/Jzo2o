package com.jzo2o.redis.sync;

import java.util.concurrent.Executor;

/**
 * 同步程序管理器
 */
public interface SyncManager {

    /**
     * 开始同步，使用默认线程池
     * @param queueName 同步队列名称
     * @param storageType 数据存储类型，1：redis hash数据结构，2：redis list数据结构，3：redis zSet结构
     * @param mode 单条执行 2批量执行
     */
    void start(String queueName, int storageType, int mode);


    /**
     * 开始同步，可以使用自定义线程池，如果不设置使用默认线程池
     * @param queueName 同步队列名称
     * @param storageType 数据存储类型，1：redis hash数据结构，2：redis list数据结构，3：redis zSet结构
     * @param mode 1 单条执行 2批量执行
     * @param dataSyncExecutor 数据同步线程池
     */
    void start(String queueName, int storageType, int mode, Executor dataSyncExecutor);


}
