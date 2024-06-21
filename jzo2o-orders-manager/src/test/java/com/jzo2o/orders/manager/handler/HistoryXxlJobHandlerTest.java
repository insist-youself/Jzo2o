package com.jzo2o.orders.manager.handler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HistoryXxlJobHandlerTest {

    @Resource
    private HistoryXxlJobHandler historyXxlJobHandler;

    @Test
    void deleteFinishedOrders() {
        historyXxlJobHandler.deleteFinishedOrders();
    }

    @Test
    void deleteFinishedOrdersServe() {
        historyXxlJobHandler.deleteFinishedOrdersServe();
    }
}