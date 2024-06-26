package com.jzo2o.statemachine;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.statemachine.core.StateMachineSnapshot;
import com.jzo2o.statemachine.core.StatusChangeEvent;
import com.jzo2o.statemachine.core.StatusChangeHandler;
import com.jzo2o.statemachine.core.StatusDefine;
import com.jzo2o.statemachine.persist.StateMachinePersister;
import com.jzo2o.statemachine.snapshot.BizSnapshotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeUnit;

import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

/**
 * @author itcast
 */
@Slf4j
public abstract class AbstractStateMachine<T extends StateMachineSnapshot> {

    /**
     * 状态机持久化程序
     */
    private final StateMachinePersister stateMachinePersister;

    /**
     * 业务快照服务层程序
     */
    private final BizSnapshotService bizSnapshotService;

    /**
     * redis处理程序
     */
    private RedisTemplate redisTemplate;

    /**
     * 初始化状态
     */
    private final StatusDefine initState;

    /**
     * 状态机名称
     */
    private final String name;

    /**
     * 构造方法
     *
     * @param stateMachinePersister 状态机持久化程序
     * @param bizSnapshotService    业务快照服务层程序
     * @param redisTemplate         redis处理程序
     */
    protected AbstractStateMachine(StateMachinePersister stateMachinePersister, BizSnapshotService bizSnapshotService, RedisTemplate redisTemplate) {
        this.stateMachinePersister = stateMachinePersister;
        this.bizSnapshotService = bizSnapshotService;
        this.redisTemplate = redisTemplate;
        this.initState = getInitState();
        this.name = getName();
    }

    /**
     * @return 返回状态机名称
     */
    protected abstract String getName();

    /**
     * 后处理方法，在更改状态机执行逻辑
     */
    protected abstract void postProcessor(T bizSnapshot);

    /**
     * @return 初始状态
     */
    protected abstract StatusDefine getInitState();

    /**
     * 状态机初始化,不保存快照
     *
     * @param bizId 业务id
     * @return 初始化状态代码
     */
    public String start(String bizId) {
        return start(null, bizId, initState, null);
    }

    /**
     * 启动状态机，并设置当前状态,不保存快照
     *
     * @param bizId        业务id
     * @param statusDefine 当前状态
     * @return 当前状态代码
     */
    public String start(String bizId, StatusDefine statusDefine) {
        return start(null, bizId, statusDefine, null);
    }


    /**
     * 启动状态机，并设置当前状态和保存业务快照，快照不分库
     *
     * @param bizId        业务id
     * @param statusDefine 当前状态
     * @param bizSnapshot  快照
     * @return 当前状态代码
     */
    public String start(String bizId, StatusDefine statusDefine, T bizSnapshot) {
        return start(null, bizId, statusDefine, bizSnapshot);
    }

    /**
     * 状态机初始化，并保存业务快照，快照分库分表
     *
     * @param dbShardId   分库键
     * @param bizId       业务id
     * @param bizSnapshot 业务快照
     * @return 初始化状态代码
     */
    public String start(Long dbShardId, String bizId, T bizSnapshot) {
        return start(dbShardId, bizId, initState, bizSnapshot);
    }

    /**
     * 启动状态机，并设置当前状态和保存业务快照，快照分库分表
     *
     * @param dbShardId    分库键
     * @param bizId        业务id
     * @param statusDefine 当前状态
     * @param bizSnapshot  快照
     * @return 当前状态代码
     */
    public String start(Long dbShardId, String bizId, StatusDefine statusDefine, T bizSnapshot) {

        //1.初始化状态机状态
        String currentState = stateMachinePersister.getCurrentState(name, bizId);
        if (ObjectUtil.isEmpty(currentState)) {
            stateMachinePersister.init(name, bizId, statusDefine);
        } else {
            throw new IllegalStateException("已存在状态，不可初始化");
        }

        //2.保存业务快照
        if (bizSnapshot == null) {
            bizSnapshot = ReflectUtil.newInstance(getSnapshotClass());
        }
        //设置快照id
        bizSnapshot.setSnapshotId(bizId);
        //设置快照状态
        bizSnapshot.setSnapshotStatus(statusDefine.getStatus());
        //快照转json
        String bizSnapshotString = JSONUtil.toJsonStr(bizSnapshot);
        if (ObjectUtil.isNotEmpty(bizSnapshot)) {
            bizSnapshotService.save(dbShardId, name, bizId, statusDefine, bizSnapshotString);
        }
        //执行后处理方法
        postProcessor(bizSnapshot);

        return statusDefine.getCode();
    }

    /**
     * 持久化删除
     *
     * @param bizId 业务id
     */
    public void clear(String bizId) {
        stateMachinePersister.clear(name, bizId);
    }

    /**
     * 获取当前状态
     *
     * @param bizId 业务id
     * @return 当前状态代码
     */
    public String getCurrentState(String bizId) {
        return stateMachinePersister.getCurrentState(name, bizId);
    }

    /**
     * 获取当前快照
     *
     * @param bizId 业务id
     * @return 快照信息
     */
    public String getCurrentSnapshot(String bizId) {
        //当前状态code
        String currentState = getCurrentState(bizId);
        return bizSnapshotService.findLastSnapshotByBizIdAndState(name, bizId, currentState);
    }

    /**
     * 根据状态查询业务快照
     *
     * @param bizId        业务id
     * @param statusDefine 状态
     * @return 业务快照
     */
    public String getSnapshotByStatus(String bizId, StatusDefine statusDefine) {
        String statusCode = statusDefine == null ? null : statusDefine.getCode();
        return bizSnapshotService.findLastSnapshotByBizIdAndState(name, bizId, statusCode);
    }

    /**
     * 获取当前状态的快照缓存
     *
     * @param bizId 业务id
     * @return 快照信息
     */
    public String getCurrentSnapshotCache(String bizId) {
        //先查询缓存，如果缓存没有就查询数据库然后存缓存
        String key = "JZ_STATE_MACHINE:" + name + ":" + bizId;
        Object object = redisTemplate.opsForValue().get(key);
        if (ObjectUtil.isNotEmpty(object)) {
            return object.toString();
        }

        String bizSnapshot = getCurrentSnapshot(bizId);
        redisTemplate.opsForValue().set(key, bizSnapshot, 30, TimeUnit.MINUTES);
        return bizSnapshot;
    }

    /**
     * 新增快照
     *
     * @param dbShardId    分库键
     * @param bizId        业务id
     * @param statusDefine 状态
     * @param bizSnapshot  业务快照
     */
    public void saveSnapshot(Long dbShardId, String bizId, StatusDefine statusDefine, T bizSnapshot) {
        //快照转json
        String jsonString = JSONUtil.toJsonStr(bizSnapshot);
        //新增快照
        bizSnapshotService.save(dbShardId, name, bizId, statusDefine, jsonString);

        //清理缓存
        String key = "JZ_STATE_MACHINE:" + name + ":" + bizId;
        redisTemplate.delete(key);
    }

    /**
     * 变更状态并保存快照，快照不进行分库
     *
     * @param bizId                 业务id
     * @param statusChangeEventEnum 状态变换事件
     */
    public void changeStatus(String bizId, StatusChangeEvent statusChangeEventEnum) {
        changeStatus(null, bizId, statusChangeEventEnum, null);
    }

    /**
     * 变更状态并保存快照，快照不进行分库
     *
     * @param bizId                 业务id
     * @param statusChangeEventEnum 状态变换事件
     * @param bizSnapshot           业务数据快照（json格式）
     */
    public void changeStatus(String bizId, StatusChangeEvent statusChangeEventEnum, T bizSnapshot) {
        changeStatus(null, bizId, statusChangeEventEnum, bizSnapshot);
    }

    /**
     * 变更状态并保存快照，快照不进行分库
     *
     * @param dbShardId             分库键
     * @param bizId                 业务id
     * @param statusChangeEventEnum 状态变换事件
     */
    public void changeStatus(Long dbShardId, String bizId, StatusChangeEvent statusChangeEventEnum) {
        changeStatus(dbShardId, bizId, statusChangeEventEnum, null);
    }

    /**
     * 变更状态并保存快照，快照分库分表
     *
     * @param dbShardId             分库键
     * @param bizId                 业务id
     * @param statusChangeEventEnum 状态变换事件
     * @param bizSnapshot           业务数据快照（json格式）
     */
    public void changeStatus(Long dbShardId, String bizId, StatusChangeEvent statusChangeEventEnum, T bizSnapshot) {
        //1.查询当前状态
        String statusCode = getCurrentState(bizId);

        //2.校验起止状态是否与事件匹配
        if (ObjectUtil.isNotEmpty(statusChangeEventEnum.getSourceStatus()) && ObjectUtil.notEqual(statusChangeEventEnum.getSourceStatus().getCode(), statusCode)) {
            throw new CommonException(HTTP_INTERNAL_ERROR, "状态机起止状态与事件不匹配");
        }

        //3.获取状态处理程序bean
        //事件代码
        String eventCode = statusChangeEventEnum.getCode();
        StatusChangeHandler bean = null;
        try {
            bean = SpringUtil.getBean(name + "_" + eventCode, StatusChangeHandler.class);
        } catch (Exception e) {
            log.info("不存在‘{}’StatusChangeHandler", name + "_" + eventCode);
        }
        if (bizSnapshot == null) {
            bizSnapshot = ReflectUtil.newInstance(getSnapshotClass());
        }
        //设置快照id
        bizSnapshot.setSnapshotId(bizId);
        //设置目标状态
        bizSnapshot.setSnapshotStatus(statusChangeEventEnum.getTargetStatus().getStatus());
        if (ObjectUtil.isNotNull(bean)) {
            //4.执行状态变更
            bean.handler(bizId, statusChangeEventEnum, bizSnapshot);
        }

        //5.状态持久化
        stateMachinePersister.persist(name, bizId, statusChangeEventEnum.getTargetStatus());

        //6、存储快照
        if (ObjectUtil.isNotEmpty(bizSnapshot)) {
            //构建新的快照信息
            bizSnapshot = buildNewSnapshot(bizId, bizSnapshot, statusChangeEventEnum.getSourceStatus());
            String newBizSnapShotString = JSONUtil.toJsonStr(bizSnapshot);
            bizSnapshotService.save(dbShardId, name, bizId, statusChangeEventEnum.getTargetStatus(), newBizSnapShotString);
        }

        //7.清理快照缓存
        String key = "JZ_STATE_MACHINE:" + name + ":" + bizId;
        redisTemplate.delete(key);

        //执行后处理方法
        postProcessor(bizSnapshot);
    }

    /**
     * 构建新的快照数据
     *
     * @param bizId        业务id
     * @param bizSnapshot  业务快照
     * @param statusDefine 状态
     * @return 业务快照（json格式）
     */
    public T buildNewSnapshot(String bizId, T bizSnapshot, StatusDefine statusDefine) {
        //1.获取上一个状态订单快照
        String currentSnapshot = getSnapshotByStatus(bizId, statusDefine);
        if (ObjectUtil.isEmpty(currentSnapshot)) {
            return bizSnapshot;
        }

        //2.将当前状态订单快照转为bean
        T oldOrderSnapshotDTO = JSONUtil.toBean(currentSnapshot, getSnapshotClass());

        //3.将新的订单快照数据覆盖旧订单快照数据，忽略null
        T orderSnapshotDTO = BeanUtils.copyIgnoreNull(bizSnapshot, oldOrderSnapshotDTO, getSnapshotClass());
        return orderSnapshotDTO;
    }

    private Class<T> getSnapshotClass() {

        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();

        return (Class<T>) parameterizedType.getActualTypeArguments()[0];

    }
}
