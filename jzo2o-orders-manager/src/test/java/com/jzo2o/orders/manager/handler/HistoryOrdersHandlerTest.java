package com.jzo2o.orders.manager.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class HistoryOrdersHandlerTest {

    @Resource
    private HistoryXxlJobHandler historyXxlJobHandler;

//    @Test
//    public void testWriteHistoryOrdersToSyncTable() {
//        historyOrdersHandler.writeHistoryOrdersToSyncTable();
//    }

}