package com.jzo2o.es.core.operations;


import co.elastic.clients.elasticsearch._types.Result;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.expcetions.ElasticSearchException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.model.dto.PageQueryDTO;
import com.jzo2o.common.utils.*;
import com.jzo2o.es.constants.FieldConstants;
import com.jzo2o.es.utils.TermUtils;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.GeoDistanceType;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.WriteResponseBase;
import co.elastic.clients.elasticsearch._types.query_dsl.FieldAndFormat;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后期优化分页功能 todo，临时使用
 */
@Slf4j
public class DefaultDocumentOperations implements DocumentOperations {
    private final ElasticsearchClient elasticsearchClient;

    public DefaultDocumentOperations(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public <T> Boolean insert(String index, T document) {

        try {
            CreateResponse createResponse = elasticsearchClient.create(builder -> builder.id(getId(document)).document(document).index(index));
            log.debug("create document response : {}", createResponse);
            boolean success = isSuccess(createResponse);
            return success;

        } catch (IOException e) {
//            e.printStackTrace();
            log.error(e.getMessage(),e);
            throw new CommonException(500,e.getMessage());
        }
//        return false;
    }

    @Override
    public <T> Boolean batchInsert(String index, List<T> documents) {
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (T document : documents) {
            br.operations(op -> op.index(idx -> idx.index(index)
                    .id(getId(document))
                    .document(document)));
        }
        try {
            BulkResponse bulk = elasticsearchClient.bulk(br.build());
            Boolean success = isSuccess(bulk);
            return success;
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new CommonException(500,e.getMessage());
        }
    }

    @Override
    public <T> Boolean batchUpsert(String index, List<T> documents) {
        if (CollUtils.isEmpty(documents)) {
            return false;
        }
        List<String> ids = documents.stream().map(document -> getId(document)).collect(Collectors.toList());
        List<?> documentInEs = this.findByIds(index, ids, Arrays.asList(FieldConstants.ID), documents.get(0).getClass());
        List<String> idsInEs = CollUtils.isEmpty(documentInEs) ? new ArrayList<>() : documentInEs.stream().map(document -> getId(document)).collect(Collectors.toList());

        BulkRequest.Builder builder = new BulkRequest.Builder();
        for (T document : documents) {
            String id = getId(document);
            boolean exists = idsInEs.contains(id);

            builder.operations(op -> {
                if (exists) {
                    op.update(u -> u.action(a -> a.doc(document)).index(index).id(id));
                } else {
                    op.index(idx -> idx.index(index)
                            .id(id)
                            .document(document));
                }
                return op;
            });
        }
        try {
            BulkResponse bulk = elasticsearchClient.bulk(builder.build());
            Boolean success = isSuccess(bulk);
            return success;
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new CommonException(500,e.getMessage());
        }
    }

    @Override
    public <T> Boolean updateById(String index, T document) {
        Object id = ReflectUtils.getFieldValue(document, IdUtils.ID);
        if (id == null) {
            throw new ElasticSearchException("es更新失败,id为空");
        }
        try {
            // 2.数据更新
            UpdateResponse<?> response = elasticsearchClient.update(u -> u
                            .index(index)
                            .id(id.toString())
                            .doc(document)
                    , document.getClass());
            Boolean success = isSuccess(response);
            return success;

        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new CommonException(500,e.getMessage());
        }
    }

    @Override
    public <ID> Boolean deleteById(String index, ID id) {

        try {
            // 2.数据更新
            DeleteResponse response = elasticsearchClient.delete(builder -> builder.id(id.toString()).index(index));
            Boolean success = isSuccess(response);
            return success;
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new CommonException(500,e.getMessage());
        }

    }

    @Override
    public <ID> Boolean batchDelete(String index, List<ID> ids) {
        BulkRequest.Builder builder = new BulkRequest.Builder();


        ids.stream().forEach(id ->
                builder.operations(b -> b.delete(d -> d.index(index).id(id.toString())))
        );


        try {
            BulkResponse bulk = elasticsearchClient.bulk(builder.build());
            Boolean success = isSuccess(bulk);
            return success;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new CommonException(500,e.getMessage());
        }

    }

    @Override
    public <T, ID> T findById(String index, ID id, Class<T> clazz) {
        try {
            GetResponse<T> response = elasticsearchClient.get(GetRequest.of(builder -> builder.id(id.toString()).index(index)), clazz);
            return response.source();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new CommonException(500,e.getMessage());
        }
    }

    @Override
    public <T, ID> List<T> findByIds(String index, List<ID> ids, Class<T> clazz) {
        SearchRequest.Builder searchRequestBuild = new SearchRequest.Builder();
        TermsQuery termsQuery = TermsQuery.of(t -> t.field(FieldConstants.ID).terms(new TermsQueryField.Builder().value(TermUtils.parse(ids)).build()));

        searchRequestBuild.index(index)
                .query(builder -> builder.terms(termsQuery));
        try {

            SearchResponse<T> searchResponse = elasticsearchClient.search(searchRequestBuild.build(), clazz);
            return searchResponse.hits().hits()
                    .stream()
                    .map(tHit -> tHit.source())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new CommonException(500,e.getMessage());
        }
    }

    @Override
    public <T, ID> List<T> findByIds(String index, List<ID> ids, List<String> fields, Class<T> clazz) {
        SearchRequest.Builder searchRequestBuild = new SearchRequest.Builder();
        TermsQuery termsQuery = TermsQuery.of(t -> t.field(FieldConstants.ID).terms(new TermsQueryField.Builder().value(TermUtils.parse(ids)).build()));
        searchRequestBuild.index(index)
                .query(builder -> builder.terms(termsQuery));
        if (CollUtils.isNotEmpty(fields)) {
            List<FieldAndFormat> fieldAndFormats = fields.stream().map(field -> FieldAndFormat.of(builder -> builder.field(field))).collect(Collectors.toList());
            searchRequestBuild.fields(fieldAndFormats);
        }
        try {

            SearchResponse<T> searchResponse = elasticsearchClient.search(searchRequestBuild.build(), clazz);
            return searchResponse.hits().hits()
                    .stream()
                    .map(tHit -> tHit.source())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new CommonException(500,e.getMessage());
        }

    }

    @Override
    public <T> PageResult<T> findForPage(PageQueryDTO pageQueryDTO, Class<T> targetClass) {

        SearchRequest.Builder builder = new SearchRequest.Builder();
        builder.from(pageQueryDTO.calFrom().intValue());
        builder.size(pageQueryDTO.getPageSize().intValue());

        SearchRequest searchRequest = new SearchRequest.Builder().build();
        try {

            SearchResponse<T> search = elasticsearchClient.search(searchRequest, targetClass);
            long total = search.hits().total().value();
            List<T> data = search.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
            return PageResult.of(data,Integer.parseInt(pageQueryDTO.getPageSize()+""),pageQueryDTO.getPageNo(),total);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new CommonException(500,e.getMessage());
        }
    }

    @Override
    public <T> List<T> searchByWithGeo(String index, SearchRequest.Builder searchBuilder, String locationName, String location, double distance, String sortBy, Boolean isAsc, int size, Class<T> clazz) {
        // 坐标，距离
        searchBuilder.query(query ->
            query.bool(q ->
                q.filter(filter->
                    filter.geoDistance(geo->{
                        geo.distance(distance + "km");
                        geo.field(locationName);
                        geo.location(location1 -> location1.text(location));
                        geo.distanceType(GeoDistanceType.Arc);
                        return geo;}))
            )
        );
        // 自定排序字段和排序
        if(StringUtils.isNotEmpty(sortBy)){
            searchBuilder.sort(sortOptionsBuilder ->
                sortOptionsBuilder.field(fieldSortBuilder->{
                    fieldSortBuilder.field(sortBy);
                    fieldSortBuilder.order(BooleanUtils.isTrue(isAsc) ? SortOrder.Asc : SortOrder.Desc);
                    return fieldSortBuilder;
                })
            );
        }
        searchBuilder.size(size);
        searchBuilder.index(index);
        try {
            SearchResponse<T> searchResponse = elasticsearchClient.search(searchBuilder.build(), clazz);

            return searchResponse.hits().hits()
                    .stream()
                    .map(tHit -> tHit.source())
                    .collect(Collectors.toList());
        }catch (IOException e){
            log.error(e.getMessage(),e);
            throw new CommonException(500,e.getMessage());
        }
    }

    @Override
    public <T> SearchResponse<T> search(SearchRequest searchRequest, Class<T> clazz) {
        try {
            return elasticsearchClient.search(searchRequest,clazz);
        }catch (IOException e){
            log.error(e.getMessage(),e);
            throw new CommonException(500,e.getMessage());
        }
    }

    private boolean isSuccess(WriteResponseBase writeResponseBase) {
        //todo
        return writeResponseBase != null &&
                (Result.Created.equals(writeResponseBase.result()) ||
                        Result.Deleted.equals(writeResponseBase.result()) ||
                        Result.Updated.equals(writeResponseBase.result()) ||
                        Result.NoOp.equals(writeResponseBase.result()));
    }

    private Boolean isSuccess(BulkResponse response) {
        //todo
        log.debug("bulk response : {}", JsonUtils.toJsonStr(response));
        if(response.errors()) {
            return false;
        }
        return response.items().stream()
                .filter(item -> item.status() != 200)
                .map(item -> false)
                .findFirst().orElse(true);
    }

    public Boolean isSuccess(DeleteByQueryResponse deleteByQueryResponse) {
        return deleteByQueryResponse.deleted() > 0;
    }

    /**
     * 获取文档id， 如果文档中设置了id，使用文档的id，如果未设置，使用雪花算法生成
     *
     * @param document
     * @param <T>
     * @return
     */
    private <T> String getId(T document) {
        Object objectId = ReflectUtils.getFieldValue(document, IdUtils.ID);
        if (objectId == null) {
            objectId = IdUtils.objectId();
        }
        return objectId.toString();
    }


}
