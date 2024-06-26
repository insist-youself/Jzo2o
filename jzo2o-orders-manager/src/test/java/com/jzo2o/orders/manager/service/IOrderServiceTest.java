//package com.jzo2o.orders.manager.service;
//
//import com.jzo2o.orders.base.model.domain.Orders;
//import com.jzo2o.orders.manager.service.IOrdersService;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@SpringBootTest
//@Slf4j
//public class IOrderServiceTest {
//
//    @Resource
//    private IOrdersService ordersService;
//
//    @Test
//    public void test() {
//        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.ge(Orders::getId, 0);
//        List<Orders> orders = ordersService.getBaseMapper().selectList(lambdaQueryWrapper);
//        log.info("orders:{}", orders);
//    }
//}
