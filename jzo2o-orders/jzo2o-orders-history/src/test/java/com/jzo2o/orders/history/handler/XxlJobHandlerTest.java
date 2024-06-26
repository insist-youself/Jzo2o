package com.jzo2o.orders.history.handler;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class XxlJobHandlerTest {

    @Resource
    private XxlJobHandler xxlJobHandler;

    @Test
    void statAndSaveDataForDay() {
        xxlJobHandler.statAndSaveDataForDay();
    }

//    @Test
//    void statAndSaveDataForHour() {
//        xxlJobHandler.statAndSaveDataForHour();
//    }

    @Test
    void migrateHistoryOrdersServe() {
        xxlJobHandler.migrateHistoryOrders();
    }

//    @Test
//    void migrateHistoryOrders() {
//        xxlJobHandler.migrateHistoryOrdersServe();
//    }
//
//    @Test
//    void deleteMigratedOrders() {
//        xxlJobHandler.deleteMigratedOrders();
//    }
//
//    @Test
//    void deleteMigratedOrdersServe() {
//        xxlJobHandler.deleteMigratedOrdersServe();
//    }
}