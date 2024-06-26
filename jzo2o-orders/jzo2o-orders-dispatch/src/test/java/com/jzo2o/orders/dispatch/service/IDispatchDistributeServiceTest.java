package com.jzo2o.orders.dispatch.service;

import com.jzo2o.orders.dispatch.handler.DispatchJobHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

import java.util.Set;

import static com.jzo2o.orders.base.constants.RedisConstants.RedisKey.DISPATCH_LIST;

@SpringBootTest
public class IDispatchDistributeServiceTest {

    @Resource
    private DispatchJobHandler dispatchDistributeJobHandler;


    @Test
    public void testDispatchDistribute() {
        dispatchDistributeJobHandler.dispatchDistributeJob();
    }

}