package com.jzo2o.orders.dispatch.service;

import com.jzo2o.common.utils.ComparatorUtils;
import com.jzo2o.common.utils.JsonUtils;
import com.jzo2o.orders.dispatch.enums.DispatchStrategyEnum;
import com.jzo2o.orders.dispatch.rules.impl.DefaultIDispatchRule;
import com.jzo2o.orders.dispatch.model.dto.ServeProviderDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

@SpringBootTest
@Slf4j
class IDispatchSearchServiceTest {


    @Resource
    private IOrdersDispatchService ordersDispatchService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void searchDispatchInfo() {

        DefaultIDispatchRule scoreDispatchRule = new DefaultIDispatchRule(null, ComparatorUtils.nullToLastComparing(ServeProviderDTO::getEvaluationScore));
        DefaultIDispatchRule dispatchRule = new DefaultIDispatchRule(scoreDispatchRule, ComparatorUtils.nullToLastComparing(ServeProviderDTO::getAcceptanceDistance));

        List<ServeProviderDTO> serveProviderDTOS = ordersDispatchService.searchDispatchInfo("010", 1683432288440897537L, 5d, 2023080920, DispatchStrategyEnum.EVELUATION_SCORE,  116.36458,40.0010, 10);
        log.info("serveProviderDTOS : {}", JsonUtils.toJsonPrettyStr(serveProviderDTOS));

        List<ServeProviderDTO> filter = dispatchRule.filter(serveProviderDTOS);

        log.info("过滤后： {}", JsonUtils.toJsonStr(filter));
    }
}