package com.jzo2o.orders.dispatch.service;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.model.dto.PageQueryDTO;
import com.jzo2o.orders.dispatch.model.dto.request.DispatchReceiveReqDTO;
import com.jzo2o.orders.dispatch.model.dto.request.DispatchRejectReqDTO;
import com.jzo2o.orders.dispatch.model.dto.response.OrdersDispatchResDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class IDispatchReceiveServiceTest {


    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }



    @Test
    void queryForPage() {
    }

    @Test
    void getDetail() {
    }


    @Test
    void batchProcessTimeoutReceive() {
    }
}