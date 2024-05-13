//package com.jzo2o.foundations.service;
//
//import com.jzo2o.es.core.ElasticSearchTemplate;
//import com.jzo2o.foundations.constants.IndexConstants;
//import com.jzo2o.foundations.model.domain.ServeSync;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@SpringBootTest
//class IServeSyncServiceTest {
//    @Resource
//    private ElasticSearchTemplate elasticSearchTemplate;
//    @Resource
//    private IServeSyncService serveSyncService;
//
//    @Test
//    void syncEs() {
//        List<ServeSync> list = serveSyncService.list();
//        elasticSearchTemplate.opsForDoc().batchInsert(IndexConstants.SERVE, list);
//    }
//}