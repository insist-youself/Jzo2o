package com.jzo2o.orders.dispatch.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IOrdersDispatchServiceTest {

    @Resource
    private IOrdersDispatchService ordersDispatchService;

    @Test
    void dispatch() {
        for (int count = 0; count < 10; count++) {
            ordersDispatchService.dispatch(2310100000000065454L);
        }
    }

    @Test
    void searchDispatchInfo() {
    }
}