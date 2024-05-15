package com.jzo2o.customer.test;

import com.jzo2o.common.model.Location;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.customer.model.domain.ServeProviderInfo;
import com.jzo2o.customer.model.domain.ServeProviderSync;
import com.jzo2o.customer.service.IServeProviderSyncService;
import com.jzo2o.es.core.ElasticSearchTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class EsTest {

    @Resource
    private ElasticSearchTemplate elasticSearchTemplate;

    @Resource
    private IServeProviderSyncService serveProviderSyncService;

    public EsTest() {
    }

    @Test
    public void testBatchInsert() {

        List<ServeProviderSync> data = serveProviderSyncService.list();
        List<ServeProviderInfo> serveProviderInfos = BeanUtils.copyToList(data, ServeProviderInfo.class, (sync, info) -> {
            info.setLocation(new Location(sync.getLon(), sync.getLat()));
        });


        Boolean batchInsertResult = elasticSearchTemplate.opsForDoc().batchInsert("serve_provider_info_1", serveProviderInfos);
        log.info("batchInsertResult:{}", batchInsertResult);
    }
}
