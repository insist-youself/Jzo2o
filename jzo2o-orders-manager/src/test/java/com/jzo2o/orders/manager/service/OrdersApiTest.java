package com.jzo2o.orders.manager.service;

import com.jzo2o.api.orders.OrdersApi;
import com.jzo2o.api.orders.dto.request.OrderPageQueryReqDTO;
import com.jzo2o.api.orders.dto.response.OrderSimpleResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class OrdersApiTest {

    @Resource
    private OrdersApi ordersApi;

    @Test
    public void test() {
        OrderPageQueryReqDTO orderPageQueryReqDTO = new OrderPageQueryReqDTO();
        orderPageQueryReqDTO.setMinCreateTime(DateUtils.now());
//        PageResult<OrderSimpleResDTO> page = ordersApi.operationPageQuery(orderPageQueryReqDTO);
//        log.info("page:{}", page);
    }
}
