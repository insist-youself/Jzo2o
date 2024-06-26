package com.jzo2o.es.core.impl;

import com.jzo2o.es.core.ElasticSearchTemplate;
import com.jzo2o.es.core.operations.DefaultDocumentOperations;
import com.jzo2o.es.core.operations.DefaultIndexOperations;
import com.jzo2o.es.core.operations.DocumentOperations;
import com.jzo2o.es.core.operations.IndexOperations;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchTemplateImpl implements ElasticSearchTemplate {
    private final ElasticsearchClient elasticsearchClient;

    public ElasticSearchTemplateImpl(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public DocumentOperations opsForDoc() {
        return new DefaultDocumentOperations(elasticsearchClient);
    }

    @Override
    public IndexOperations opsForIndex() {
        return new DefaultIndexOperations(elasticsearchClient);
    }
}
