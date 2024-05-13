//package com.jzo2o.foundations.client;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
//import com.jzo2o.es.core.ElasticSearchTemplate;
//import com.jzo2o.foundations.model.domain.ServeAggregation;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//
//
//@SpringBootTest
//@Slf4j
//public class EsClientTest {
//
//    @Resource
//    private ElasticsearchClient elasticsearchClient;
//
//    @Resource
//    private ElasticSearchTemplate elasticSearchTemplate;
//
//    @Test
//    public void createIndex() throws IOException {
//        CreateIndexResponse response = elasticsearchClient.indices().create(builder -> builder
//                // 设置索引分片：number_of_shards：主分片数，默认为1；number_of_replicas：副本分片数，默认为1
//                // 合理设置索引分片可以提高ES的查询性能
//                .settings(indexSettingsBuilder -> indexSettingsBuilder.numberOfReplicas("1").numberOfShards("2"))
//                .mappings(typeMappingBuilder -> typeMappingBuilder
//                        .properties("age", propertyBuilder -> propertyBuilder.integer(integerNumberPropertyBuilder -> integerNumberPropertyBuilder))
//                        .properties("name", propertyBuilder -> propertyBuilder.keyword(keywordPropertyBuilder -> keywordPropertyBuilder))
//                )
//                .index("serve_aggregation"));
//        log.info("索引创建是否成功：{}", response.acknowledged());
//    }
//
//    @Test
//    public void addDocument() {
//        ServeAggregation serve = new ServeAggregation();
//        serve.setId(4L);
//        serve.setServeItemName("test");
//        Boolean isSuccess = elasticSearchTemplate.opsForDoc().insert("serve_aggregation", serve);
//        log.info("新增结果： {}", isSuccess);
//    }
//
//    @Test
//    public void find() {
//        ServeAggregation serve = elasticSearchTemplate.opsForDoc().findById("serve_aggregation", "1", ServeAggregation.class);
//        log.info("serve : {}", serve);
//    }
//
//
//}
