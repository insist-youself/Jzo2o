package com.jzo2o.es.utils;

import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.ObjectUtils;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.util.List;
import java.util.stream.Collectors;

public class SearchResponseUtils {

    public static boolean isSuccess(SearchResponse<?> searchResponse) {
        return searchResponse != null && ObjectUtils.isNotEmpty(searchResponse.hits()) && CollUtils.isNotEmpty(searchResponse.hits().hits());
    }

    public static boolean isNotSuccess(SearchResponse<?> searchResponse) {
        return !isSuccess(searchResponse);
    }

    public static <T> List<T> getResponse(SearchResponse<T> searchResponse) {
        return getResponse(searchResponse, null);
    }


    public static <T> List<T> getResponse(SearchResponse<T> searchResponse, Convert<T> convert) {
        if (!isSuccess(searchResponse)) {
            return null;
        }
        if (convert == null) {
            return searchResponse.hits().hits().stream().map(hit -> hit.source()).collect(Collectors.toList());
        }

        return searchResponse.hits().hits()
                .stream().map(hit -> {
                    convert.convert(hit, hit.source());
                    return hit.source();
                }).collect(Collectors.toList());

    }

    public static interface Convert<T> {
        void convert(Hit<T> hit, T t);
    }
}
