package com.jzo2o.orders.manager.service;

import com.jzo2o.orders.base.config.OrderStateMachine;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class OrderStateMachineTest {

    @Resource
    private OrderStateMachine orderStateMachine;

    @Test
    public void test_start() {
        //启动状态机
        String start = orderStateMachine.start("101");
        log.info("返回初始状态:{}", start);
    }
//    @Test
    public void test_changeStatus() {
        //状态变更
        orderStateMachine.changeStatus("101",OrderStatusChangeEventEnumTest.PAYED_TEST);
    }

//    @Test
    public void test_orderStatMmachine() {
        //启动状态机
        String start = orderStateMachine.start("102");
        //状态变更
        orderStateMachine.changeStatus("102",OrderStatusChangeEventEnumTest.PAYED_TEST);
    }
}
