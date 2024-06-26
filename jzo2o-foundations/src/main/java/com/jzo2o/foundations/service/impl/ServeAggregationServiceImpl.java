package com.jzo2o.foundations.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.jzo2o.common.expcetions.ElasticSearchException;
import com.jzo2o.common.utils.LambdaUtils;
import com.jzo2o.es.core.ElasticSearchTemplate;
import com.jzo2o.es.utils.SearchResponseUtils;
import com.jzo2o.foundations.constants.IndexConstants;
import com.jzo2o.foundations.model.domain.ServeAggregation;
import com.jzo2o.foundations.model.dto.response.ServeSimpleResDTO;
import com.jzo2o.foundations.service.ServeAggregationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务相关
 *
 * @author itcast
 * @create 2023/7/9 14:42
 **/
@Slf4j
@Service
public class ServeAggregationServiceImpl implements ServeAggregationService {
    @Resource
    private ElasticsearchClient client;

    @Resource
    private ElasticSearchTemplate elasticSearchTemplate;


    /**
     * 查询服务列表
     *
     * @param cityCode    城市编码
     * @param serveTypeId 服务类型id
     * @param keyword     关键词
     * @return 服务列表
     */
    @Override
    public List<ServeSimpleResDTO> findServeList(String cityCode, Long serveTypeId, String keyword) {
        // 构造查询条件
        SearchRequest.Builder builder = new SearchRequest.Builder();

        builder.query(query->query.bool(bool->{
            //匹配citycode
            bool.must(must->
                    must.term(term->
                            term.field(LambdaUtils.getUnderLineFieldName(ServeAggregation::getCityCode)).value(cityCode)));
            //匹配服务类型
            if (ObjectUtil.isNotEmpty(serveTypeId)) {
                bool.must(must->
                        must.term(term->
                                term.field(LambdaUtils.getUnderLineFieldName(ServeAggregation::getServeTypeId)).value(serveTypeId)));
            }
            //匹配关键字
            if (ObjectUtil.isNotEmpty(keyword)) {
                String serveItemNameField = LambdaUtils.getUnderLineFieldName(ServeAggregation::getServeItemName);
                String serveTypeNameField = LambdaUtils.getUnderLineFieldName(ServeAggregation::getServeTypeName);

                bool.must(must->
                        must.multiMatch(multiMatch->multiMatch.fields(serveItemNameField,serveTypeNameField).query(keyword)));
            }

            return bool;
        }));
        // 排序 按服务项的serveItemSortNum排序(升序)
        List<SortOptions> sortOptions = new ArrayList<>();
        sortOptions.add(SortOptions.of(sortOption -> sortOption.field(field->field.field("serve_item_sort_num").order(SortOrder.Asc))));
        builder.sort(sortOptions);
        // 索引
        builder.index(IndexConstants.SERVE);
        // 检索数据
        SearchResponse<ServeAggregation> searchResponse = elasticSearchTemplate.opsForDoc().search(builder.build(), ServeAggregation.class);
        //如果搜索成功返回结果集
        if (SearchResponseUtils.isSuccess(searchResponse)) {
            List<ServeAggregation> collect = searchResponse.hits().hits()
                    .stream().map(hit -> {
                        ServeAggregation serve = hit.source();
                        return serve;
                    })
                    .collect(Collectors.toList());
            List<ServeSimpleResDTO> serveSimpleResDTOS = BeanUtil.copyToList(collect, ServeSimpleResDTO.class);
            return serveSimpleResDTOS;
        }
        return  Collections.emptyList();

    }
}
