package com.jzo2o.orders.seize.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class OrderServeTest {

    @Resource
    private IOrdersServeService ordersServeService;

    @Test
    public void test() {
        Integer num = ordersServeService.countNoServedNum(1695628302246506498L);
        log.info("num : {}", num);
    }
}
