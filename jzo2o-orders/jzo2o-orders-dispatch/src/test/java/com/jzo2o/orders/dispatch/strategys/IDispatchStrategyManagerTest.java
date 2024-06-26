package com.jzo2o.orders.dispatch.strategys;

import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.JsonUtils;
import com.jzo2o.orders.base.model.domain.OrdersDispatch;
import com.jzo2o.orders.dispatch.enums.DispatchStrategyEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import javax.annotation.Resource;

import java.util.List;
import java.util.Set;

@SpringBootTest
@Slf4j
class IDispatchStrategyManagerTest {

    @Resource
    private IDispatchStrategyManager dispatchStrategyManager;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void testZSet() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        Set set = zSetOperations.rangeByScore("ORDERS:DISPATCH:LIST", 0, 1691774636, 0, 1000);
        List<OrdersDispatch> list = BeanUtils.copyToList(set, OrdersDispatch.class);
        log.info("set : {}", set);

        Long remove = zSetOperations.remove("ORDERS:DISPATCH:LIST", JsonUtils.toJsonStr(list.get(0)));
        log.info("{}",remove);

    }

    @Test
    void get() {
        IDispatchStrategy distanceDispatchStragy = dispatchStrategyManager.get(DispatchStrategyEnum.DISTANCE);
        log.info("distanceDispatchStragy : {}", distanceDispatchStragy);

        IDispatchStrategy evaluationScoreDispatchStrategy = dispatchStrategyManager.get(DispatchStrategyEnum.EVELUATION_SCORE);
        log.info("evaluationScoreDispatchStrategy : {}", evaluationScoreDispatchStrategy);

        IDispatchStrategy leastAcceptOrderDispatchStrategy = dispatchStrategyManager.get(DispatchStrategyEnum.LEAST_ACCEPT_ORDER);
        log.info("leastAcceptOrderDispatchStrategy : {}", leastAcceptOrderDispatchStrategy);


    }

    
}