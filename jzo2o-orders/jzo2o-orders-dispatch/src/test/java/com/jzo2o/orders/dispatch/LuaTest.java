package com.jzo2o.orders.dispatch;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest
@Slf4j
public class LuaTest {

    @Resource(name = "dispatchOrdersScript")
    private DefaultRedisScript<Long> dispatchOrdersScript;

    @Resource
    private RedisTemplate redisTemplate;
    @Test
    public void testLua() {
        // 派单连续失败次数redisKey
        String dispatchFaildTimesRedisKey = "ORDERS:DISPATCH:FAILD_TIMES_1691712276350382080_{0}";
        // 派单成功同步记录redisKey
        String dispatchSyncListRedisKey = "ORDERS:DISPATCH:SYNC_{0}";
        // 资源库存redisKey
        String resourceStockRedisKey = "ORDERS:RESOURCE:STOCK:{0}";
        // 服务时间状态redisKey
        String serveTimesRedisKey = "PROVIDER:SERVE_TIME:1691345690120187906_2023081617_{0}";
        Long id = 1691712276350382080L;
        int serveTime = 2023081617;

        // 服务单数量
        String providerServeNumRedisKey = "PROVIDER:SERVE:NUM_010_{0}";
        Object result = null;
        try {
            result = redisTemplate.execute(dispatchOrdersScript, Arrays.asList(dispatchSyncListRedisKey, resourceStockRedisKey, dispatchFaildTimesRedisKey, serveTimesRedisKey, providerServeNumRedisKey),
                    id, 1683504743982964738L, serveTime, 10);

        }catch (Exception e) {
            e.printStackTrace();
        }
        log.info("result : {}", result);
    }
}
