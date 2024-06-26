package com.jzo2o.customer.service;

import com.jzo2o.customer.model.dto.response.EvaluationResDTO;
import com.jzo2o.customer.model.dto.response.EvaluationSystemInfoResDTO;
import com.jzo2o.customer.properties.EvaluationProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class EvaluationServiceTest {
    @Resource
    private EvaluationProperties evaluationProperties;
    @Resource
    private EvaluationService evaluationService;

    @Test
    void test01(){
        String appId = evaluationProperties.getAppId();
        System.out.println(appId);

        String targetTypeId = evaluationProperties.getServeItem().getTargetTypeId();
        System.out.println(targetTypeId);
    }

    @Test
    void queryServeProviderScoreByOrdersId(){
        List<Long> ids=new ArrayList<>();
        ids.add(2309070000000000526L);
        ids.add(2309070000000000527L);
        Map<String, Double> stringDoubleMap = evaluationService.queryServeProviderScoreByOrdersId(ids);
        System.out.println(stringDoubleMap);
    }
}