package com.jzo2o.orders.seize.service;

import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.es.core.ElasticSearchTemplate;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.orders.base.model.domain.OrdersSeize;
import com.jzo2o.orders.seize.service.IOrdersSeizeService;
import com.jzo2o.orders.seize.handler.SeizeJobHandler;
import com.jzo2o.orders.seize.model.dto.request.OrdersSerizeListReqDTO;
import com.jzo2o.orders.seize.model.dto.response.OrdersSeizeListResDTO;
import co.elastic.clients.elasticsearch._types.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Slf4j
public class ISeizeServiceTest {

    private static final List<String> INCLUDE_FIELD_NAMES = Arrays.asList("id");

    @Resource
    public ElasticSearchTemplate elasticSearchTemplate;

    @Resource
    private IOrdersSeizeService ordersSeizeService;

    @Resource
    private SeizeJobHandler seizeJobHandler;

    @BeforeEach
    void setUp() {
        CurrentUserInfo currentUserInfo = new CurrentUserInfo(1696338624494202882L, null, null, UserType.WORKER);

        UserContext.set(currentUserInfo);
    }

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Test
    public void queryForList() {
        OrdersSerizeListReqDTO ordersSerizeListReqDTO =  new OrdersSerizeListReqDTO();
        OrdersSeizeListResDTO ordersSeizeListResDTO = ordersSeizeService.queryForList(ordersSerizeListReqDTO);
        log.info("ordersSeizeListResDTO:{}", ordersSeizeListResDTO);
    }

    @Test
    public void seize() {
        Long id = 2309220000000001807L;
        ordersSeizeService.seize(id, UserContext.currentUserId(), UserContext.currentUser().getUserType(), false);
    }

    @Test
    public void testEsSearch() {
        double lon = 116.00011;
        double lat = 40.00000;
        List<FieldValue> fieldValues = Arrays.asList(
                FieldValue.of(1685850705647194113L),
                FieldValue.of(1678654490336124929L));
        SearchRequest.Builder builder = new SearchRequest.Builder();
        builder.query(query ->
            query.bool(bool -> {
                bool.must(must -> must.term(term -> term.field("city_code").value("010")));
                bool.must(must -> must.terms(terms -> terms.field("serve_item_id").terms( t-> t.value(fieldValues))));
                bool.must(m -> {
                    m.geoDistance(geoDistance ->{
                        geoDistance.field("location");
                        geoDistance.location(location -> location.latlon( latlon -> latlon.lon(lon).lat(lat)));
                        geoDistance.distance("1000km");
                        return geoDistance;
                    });
                    return m;
                });
                bool.mustNot(mustNot->
                    mustNot.term(term -> term.field("serve_time").value(2023081819)));
                return bool;
            }));
        // 排序
        List<SortOptions> sortOptions = new ArrayList<>();
        sortOptions.add(SortOptions.of(sortOption -> sortOption.geoDistance(
                geoDistance ->{
                    geoDistance.field("location");
                    geoDistance.distanceType(GeoDistanceType.Arc);
                    geoDistance.order(SortOrder.Asc);
                    geoDistance.unit(DistanceUnit.Kilometers);
                    geoDistance.location( location -> location.latlon(latlon -> latlon.lat(lat).lon(lon)));
                    return geoDistance;
                }
        )));
        builder.sort(sortOptions);
        GeoLocation.Builder geoLocationBuilder = new GeoLocation.Builder();
        geoLocationBuilder.latlon(latlon -> latlon.lon(lon).lat(lat));
        builder.index("orders_seize");
        builder.source(s -> s.filter(ss -> ss.includes(INCLUDE_FIELD_NAMES)));
        SearchResponse<OrdersSeize> searchResponse = elasticSearchTemplate.opsForDoc().search(builder.build(), OrdersSeize.class);
        log.info("response : {}", searchResponse);
    }

    @Test
    public void testDeleteTimeoutSeizeOrder() {
//        ordersSeizeService.deleteTimeoutSeizeOrder();
    }

    @Test
    public void testTimeoutSeizeOrder() {
//        seizeJobHandler.seizeTimeoutContinueJob();
    }

}