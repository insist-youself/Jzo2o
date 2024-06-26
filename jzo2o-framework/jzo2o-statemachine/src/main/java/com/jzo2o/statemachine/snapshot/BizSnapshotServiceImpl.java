package com.jzo2o.statemachine.snapshot;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jzo2o.statemachine.core.StatusDefine;
import com.jzo2o.statemachine.mapper.BizSnapshotMapper;
import com.jzo2o.statemachine.model.BizSnapshot;
import org.springframework.stereotype.Component;

/**
 * 业务数据服务层
 *
 * @author itcast
 * @create 2023/8/18 20:33
 **/
@Component
public class BizSnapshotServiceImpl implements BizSnapshotService {
    /**
     * 业务快照数据层处理程序
     */
    private final BizSnapshotMapper bizSnapshotMapper;

    /**
     * 默认分库键值，不进行分库时使用
     */
    private static final Long DEFAULT_DB_SHARD_ID = 1L;

    /**
     * 构造器
     *
     * @param bizSnapshotMapper 业务快照数据层处理程序
     */
    public BizSnapshotServiceImpl(BizSnapshotMapper bizSnapshotMapper) {
        this.bizSnapshotMapper = bizSnapshotMapper;
    }

    /**
     * 新增业务快照
     *
     * @param dbShardId        分库键
     * @param stateMachineName 状态机名称
     * @param bizId            业务id
     * @param statusDefine     状态
     * @param bizSnapshot      业务快照
     */
    @Override
    public void save(Long dbShardId, String stateMachineName, String bizId, StatusDefine statusDefine, String bizSnapshot) {
        BizSnapshot model = BizSnapshot.builder()
                .id(IdUtil.getSnowflakeNextId())
                .stateMachineName(stateMachineName)
                .bizId(bizId)
                .dbShardId(dbShardId)
                .state(statusDefine.getCode())
                .bizData(bizSnapshot).build();

        //如果分库键为空，即数据库不进行分库，使其默认值为1
        if (null == dbShardId) {
            model.setDbShardId(DEFAULT_DB_SHARD_ID);
        }
        bizSnapshotMapper.insert(model);
    }

    /**
     * 根据业务id和状态查询最新业务快照
     *
     * @param stateMachineName 状态机名称
     * @param bizId            业务id
     * @param state            状态代码
     * @return 业务快照
     */
    @Override
    public String findLastSnapshotByBizIdAndState(String stateMachineName, String bizId, String state) {
        LambdaQueryWrapper<BizSnapshot> queryWrapper = Wrappers.<BizSnapshot>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(stateMachineName), BizSnapshot::getStateMachineName, stateMachineName)
                .eq(ObjectUtil.isNotEmpty(bizId), BizSnapshot::getBizId, bizId)
                .eq(ObjectUtil.isNotEmpty(state), BizSnapshot::getState, state)
                .gt(BizSnapshot::getDbShardId, 0)
                .orderByDesc(BizSnapshot::getCreateTime)
                .last("limit 1");
        BizSnapshot bizSnapshot = bizSnapshotMapper.selectOne(queryWrapper);
        return ObjectUtil.isNotNull(bizSnapshot)?bizSnapshot.getBizData():null;
    }
}
