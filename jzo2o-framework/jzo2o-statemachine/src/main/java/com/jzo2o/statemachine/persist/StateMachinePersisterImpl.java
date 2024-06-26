package com.jzo2o.statemachine.persist;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.statemachine.core.StatusDefine;
import com.jzo2o.statemachine.mapper.StateMachineMapper;
import com.jzo2o.statemachine.model.StatePersister;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 状态机持久化实现
 *
 * @author itcast
 **/
@Component
@Primary
public class StateMachinePersisterImpl implements StateMachinePersister {
    /**
     * 状态机数据层处理类
     */
    private final StateMachineMapper stateMachineMapper;

    /**
     * 构造器
     *
     * @param stateMachineMapper 状态机数据层处理类
     */
    public StateMachinePersisterImpl(StateMachineMapper stateMachineMapper) {
        this.stateMachineMapper = stateMachineMapper;
    }

    /**
     * 业务数据状态初始化
     *
     * @param stateMachineName 状态机名称
     * @param bizId            业务id
     * @param statusDefine     当前状态
     */
    @Override
    public void init(String stateMachineName, String bizId, StatusDefine statusDefine) {
        String code = statusDefine.getCode();

        StatePersister statePersister = StatePersister.builder().stateMachineName(stateMachineName).bizId(bizId).state(code).build();
        stateMachineMapper.insert(statePersister);
    }

    /**
     * 业务数据状态持久化
     *
     * @param stateMachineName 状态机名称
     * @param bizId            业务id
     * @param statusDefine     当前状态
     */
    @Override
    public void persist(String stateMachineName, String bizId, StatusDefine statusDefine) {
        String code = statusDefine.getCode();

        LambdaUpdateWrapper<StatePersister> updateWrapper = Wrappers.<StatePersister>lambdaUpdate()
                .eq(StatePersister::getStateMachineName, stateMachineName)
                .eq(StatePersister::getBizId, bizId)
                .set(StatePersister::getState, code);
        stateMachineMapper.update(null, updateWrapper);
    }

    /**
     * 查询业务数据当前持久化状态
     *
     * @param stateMachineName 状态机名称
     * @param bizId            业务id
     * @return 当前持久化状态代码
     */
    @Override
    public String getCurrentState(String stateMachineName, String bizId) {
        LambdaQueryWrapper<StatePersister> queryWrapper = Wrappers.<StatePersister>lambdaQuery()
                .eq(StatePersister::getStateMachineName, stateMachineName)
                .eq(StatePersister::getBizId, bizId)
                .select(StatePersister::getState);
        StatePersister statePersister = stateMachineMapper.selectOne(queryWrapper);
        return ObjectUtils.get(statePersister, StatePersister::getState);
    }

    /**
     * 根据状态机名称和业务id清理持久化状态
     *
     * @param stateMachineName 状态机名称
     * @param bizId            业务id
     */
    @Override
    public void clear(String stateMachineName, String bizId) {
        LambdaQueryWrapper<StatePersister> queryWrapper = Wrappers.<StatePersister>lambdaQuery()
                .eq(StatePersister::getStateMachineName, stateMachineName)
                .eq(StatePersister::getBizId, bizId);
        stateMachineMapper.delete(queryWrapper);
    }
}
