package com.jzo2o.orders.seize.service;

import com.jzo2o.redis.constants.RedisSyncQueueConstants;
import com.jzo2o.redis.sync.SyncManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

import static com.jzo2o.orders.base.constants.RedisConstants.RedisKey.ORERS_SEIZE_SYNC_QUEUE_NAME;

@SpringBootTest
@Slf4j
public class SeizeSyncTest {

    @Resource
    private SyncManager syncManager;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void testSync() {
        try {
            syncManager.start(ORERS_SEIZE_SYNC_QUEUE_NAME, RedisSyncQueueConstants.STORAGE_TYPE_HASH, RedisSyncQueueConstants.MODE_SINGLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
