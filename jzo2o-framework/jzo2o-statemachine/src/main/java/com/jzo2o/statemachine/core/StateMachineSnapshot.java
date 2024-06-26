package com.jzo2o.statemachine.core;

/**
 * @author Mr.M
 * @version 1.0
 * @description 状态快照基础类
 * @date 2023/9/18 11:07
 */
public abstract class StateMachineSnapshot {

    /**
     * 返回快照id
     * @return
     */
    public abstract String getSnapshotId();
    /**
     * 返回快照状态
     * @return
     */
    public abstract Integer getSnapshotStatus();
    /**
     * 设置快照id
     */
    public abstract void setSnapshotId(String snapshotId);
    /**
     * 设置快照状态
     */
    public abstract void setSnapshotStatus(Integer snapshotStatus);
}
