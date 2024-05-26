package com.jzo2o.foundations.service.impl;

import cn.hutool.core.util.ObjectUtil;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.es.core.ElasticSearchTemplate;
import com.jzo2o.es.utils.SearchResponseUtils;
import com.jzo2o.foundations.model.domain.ServeAggregation;
import com.jzo2o.foundations.model.dto.response.ServeSimpleResDTO;
import com.jzo2o.foundations.service.ServeAggregationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linger
 * @date 2024/5/25 20:00
 */
@Slf4j
@Service
public class ServeAggregationServiceImpl implements ServeAggregationService {
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

        builder.query(query -> query.bool(bool -> {

            bool.must(must ->
                    must.term(term ->
                            term.field("city_code").value(cityCode)));

            if(ObjectUtil.isNotEmpty(serveTypeId)) {
                //匹配服务类型
                bool.must(must ->
                        must.term(term ->
                                term.field("serve_type_id").value(serveTypeId)));
            }

            if (ObjectUtil.isNotEmpty(keyword)) {
                bool.must(must -> must.multiMatch(multiMatch ->
                        multiMatch.fields("serve_item_name", "serve_type_name").query(keyword)));
            }

            return bool;
        }));

        List<SortOptions> sortOptionsList = new ArrayList<>();
        sortOptionsList.add(SortOptions.of(sortOptions -> sortOptions.field(field ->
                field.field("serve_item_sort_num").order(SortOrder.Asc))));
        // 指定索引
        builder.index("serve_aggregation");

        // 请求对象
        SearchRequest searchRequest = builder.build();

        // 检索数据
        SearchResponse<ServeAggregation> searchResponse = elasticSearchTemplate.opsForDoc().search(searchRequest, ServeAggregation.class);

        // 如果搜索成功返回结果集
        if (SearchResponseUtils.isNotSuccess(searchResponse)) {
            return Collections.emptyList();
        }
        List<ServeAggregation> serveAggregations = searchResponse.hits().hits()
                .stream().map(hit -> {
                    ServeAggregation serve = hit.source();
                    return serve;
                }).collect(Collectors.toList());
        List<ServeSimpleResDTO> serveSimpleResDTOS = BeanUtils.copyToList(serveAggregations, ServeSimpleResDTO.class);
        return serveSimpleResDTOS;
    }
}
