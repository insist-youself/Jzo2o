package com.jzo2o.statemachine.persist;

import com.jzo2o.statemachine.core.StatusDefine;

/**
 * @author itcast
 */
public interface StateMachinePersister {

    /**
     * 业务数据状态初始化
     *
     * @param stateMachineName 状态机名称
     * @param bizId            业务id
     * @param statusDefine     当前状态
     */
    void init(String stateMachineName, String bizId, StatusDefine statusDefine);

    /**
     * 业务数据状态持久化
     *
     * @param stateMachineName 状态机名称
     * @param bizId            业务id
     * @param statusDefine     当前状态
     */
    void persist(String stateMachineName, String bizId, StatusDefine statusDefine);

    /**
     * 查询业务数据当前持久化状态
     *
     * @param stateMachineName 状态机名称
     * @param bizId            业务id
     * @return 当前持久化状态代码
     */
    String getCurrentState(String stateMachineName, String bizId);

    /**
     * 根据状态机名称和业务id清理持久化状态
     *
     * @param stateMachineName 状态机名称
     * @param bizId            业务id
     */
    void clear(String stateMachineName, String bizId);
}