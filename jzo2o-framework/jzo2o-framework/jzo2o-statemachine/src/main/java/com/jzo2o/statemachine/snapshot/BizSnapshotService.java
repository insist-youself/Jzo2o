package com.jzo2o.statemachine.snapshot;

import com.jzo2o.statemachine.core.StatusDefine;

/**
 * 业务数据服务层
 *
 * @author itcast
 * @create 2023/8/18 20:32
 **/
public interface BizSnapshotService {
    /**
     * 新增业务快照
     *
     * @param dbShardId        分库键
     * @param stateMachineName 状态机名称
     * @param bizId            业务id
     * @param statusDefine     状态
     * @param bizSnapshot      业务快照
     */
    void save(Long dbShardId, String stateMachineName, String bizId, StatusDefine statusDefine, String bizSnapshot);

    /**
     * 根据业务id和状态查询最新业务快照
     *
     * @param stateMachineName 状态机名称
     * @param bizId            业务id
     * @param state            状态代码
     * @return 业务快照
     */
    String findLastSnapshotByBizIdAndState(String stateMachineName, String bizId, String state);
}
