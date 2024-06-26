package com.jzo2o.redis.sync.impl;

import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.redis.constants.RedisSyncQueueConstants;
import com.jzo2o.redis.handler.SyncProcessHandler;
import com.jzo2o.redis.model.SyncMessage;
import com.jzo2o.redis.utils.RedisSyncQueueUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class HashSyncThread extends AbstractSyncThread<Object> {
    private final RedisTemplate redisTemplate;
    /**
     * 每批数量
     */
    private int perCount = 100;

    /**
     * 处理模式，默认为单条处理
     */
    private int mode = 1;

    public HashSyncThread(RedissonClient redissonClient, String queueName, int index, RedisTemplate redisTemplate, int perCount, int mode) {
        super(redissonClient, queueName, index);
        this.redisTemplate = redisTemplate;
        this.perCount = perCount;
        this.mode = mode;
    }

    @Override
    protected List<SyncMessage<Object>> getData() {
        Cursor<Map.Entry<String, Object>> cursor = null;
        // 通过scan从redis hash数据中批量获取数据，获取完数据需要手动关闭游标
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .count(perCount)
                .build();
        try {
            // sscan获取数据
            cursor = redisTemplate.opsForHash().scan(RedisSyncQueueUtils.getQueueRedisKey(getQueueName(), getIndex()), scanOptions);
            // 遍历数据转换成SyncMessage列表
            return cursor.stream()
                    .map(entry -> SyncMessage
                            .builder()
                            .key(entry.getKey().toString())
                            .value(entry.getValue())
                            .build())
                    .collect(Collectors.toList());
        }catch (Exception e){
            log.error("同步处理异常，e:", e);
            throw new RuntimeException(e);
        } finally {
            // 关闭游标
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected boolean process(List<SyncMessage<Object>> data) {
        // 校验数据是否为空，为空停止循环遍历
        if (CollUtils.isEmpty(data)) {
            return false;
        }
        // 根据名称获取同步处理器
        SyncProcessHandler<Object> syncProcessHandler = getSyncProcessHandler();
        // 队列redisKey
        String queueRedisKey = RedisSyncQueueUtils.getQueueRedisKey(getQueueName(), getIndex());
        // 单条执行模式
        if (mode == RedisSyncQueueConstants.MODE_SINGLE) {
            //逐条执行
            data.stream().forEach(objectSyncMessage -> {
                try {
                    // 执行单条数据
                    syncProcessHandler.singleProcess(objectSyncMessage);
                    // 从hash表中删除数据
                    redisTemplate.opsForHash()
                            .delete(queueRedisKey, objectSyncMessage.getKey());
                } catch (Exception e) {
                    log.error("hash结构同步消息单独处理异常，e:", e);
                }
            });
        } else {
            // 批量执行模式
            try {
                // 批量处理数据
                syncProcessHandler.batchProcess(data);
                // 获取所有hashKey
                List<String> hashKeys = data.stream().map(SyncMessage::getKey).collect(Collectors.toList());
                // 根据redisKey和hashKey列表从hash表中删除数据
                redisTemplate.opsForHash().delete(queueRedisKey, hashKeys);
            } catch (Exception e) {
                log.error("hash结构同步消息批量处理异常，e:", e);
            }
        }
        return true;
    }
}
