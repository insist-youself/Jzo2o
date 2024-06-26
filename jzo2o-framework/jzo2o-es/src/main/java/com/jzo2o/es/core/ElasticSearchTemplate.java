package com.jzo2o.es.core;

import com.jzo2o.es.core.operations.DocumentOperations;
import com.jzo2o.es.core.operations.IndexOperations;

public interface ElasticSearchTemplate {
    DocumentOperations opsForDoc();
    IndexOperations opsForIndex();
}
