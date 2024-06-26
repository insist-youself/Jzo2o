package com.jzo2o.orders.dispatch.service.impl;

import co.elastic.clients.elasticsearch._types.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.foundations.RegionApi;
import com.jzo2o.api.foundations.dto.response.ConfigRegionInnerResDTO;
import com.jzo2o.api.orders.OrdersSeizeApi;
import com.jzo2o.api.orders.dto.request.OrderSeizeReqDTO;
import com.jzo2o.common.utils.*;
import com.jzo2o.es.core.ElasticSearchTemplate;
import com.jzo2o.es.utils.SearchResponseUtils;
import com.jzo2o.orders.base.mapper.OrdersDispatchMapper;
import com.jzo2o.orders.base.model.domain.OrdersDispatch;
import com.jzo2o.orders.base.model.domain.ServeProviderInfo;
import com.jzo2o.orders.base.utils.ServeTimeUtils;
import com.jzo2o.orders.dispatch.enums.DispatchStrategyEnum;
import com.jzo2o.orders.dispatch.model.dto.ServeProviderDTO;
import com.jzo2o.orders.dispatch.service.IOrdersDispatchService;
import com.jzo2o.orders.dispatch.strategys.IDispatchStrategy;
import com.jzo2o.orders.dispatch.strategys.IDispatchStrategyManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jzo2o.orders.base.constants.EsIndexConstants.SERVER_PROVIDER_INFO;
import static com.jzo2o.orders.base.constants.RedisConstants.RedisKey.DISPATCH_LIST;

/**
 * <p>
 * 派单池 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-08-02
 */
@Service
@Slf4j
public class OrdersDispatchServiceImpl extends ServiceImpl<OrdersDispatchMapper, OrdersDispatch> implements IOrdersDispatchService {

    @Resource
    private RegionApi regionApi;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private IOrdersDispatchService ordersDispatchService;


    @Resource
    private IDispatchStrategyManager dispatchStrategyManager;

    @Resource
    private OrdersSeizeApi ordersSeizeApi;


//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void batchAdd(List<OrdersDispatch> ordersDispatchs) {
//        if (CollUtils.isEmpty(ordersDispatchs)) {
//            return;
//        }
//        // 用于分批次插入的临时列表,每批次100条
//        this.saveBatch(ordersDispatchs,100);
//    }


//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void transferManual(Long id) {
//        lambdaUpdate()
//                .set(OrdersDispatch::getIsTransferManual, 1)
//                .eq(OrdersDispatch::getId, id)
//                .update();
//    }
//
//    @Override
//    public int batchDeleteByIds(List<Long> ids) {
//
//        LambdaQueryWrapper<OrdersDispatch> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.in(OrdersDispatch::getId, ids);
//        int deleteResult = baseMapper.delete(lambdaQueryWrapper);
//        log.debug("批量删除派单数量：{}", deleteResult);
//        return deleteResult;
//    }

    public void dispatch(Long id) {
        // 1.数据准备
        // 1.1.获取订单信息
        OrdersDispatch ordersDispatch = ordersDispatchService.getById(id);
        if (ordersDispatch == null) {
            // 订单不在直接删除
            redisTemplate.opsForZSet().remove(DISPATCH_LIST, id);
            return;
        }

        // 1.3.服务时间,格式yyyyMMddHH
        int serveTime = ServeTimeUtils.getServeTimeInt(ordersDispatch.getServeStartTime());
        // 1.4.区域调度配置
        ConfigRegionInnerResDTO configRegionInnerResDTO = regionApi.findConfigRegionByCityCode(ordersDispatch.getCityCode());
        // 1.5.获取派单规则
        DispatchStrategyEnum dispatchStrategyEnum = DispatchStrategyEnum.of(configRegionInnerResDTO.getDispatchStrategy());


        // 2.修改下次执行时间(默认3分钟)，防止重复执行
        ConfigRegionInnerResDTO configRegion = regionApi.findConfigRegionByCityCode(ordersDispatch.getCityCode());
        redisTemplate.opsForZSet().incrementScore(DISPATCH_LIST, id, configRegion.getDispatchPerRoundInterval());
        // 2.获取派单人员或机构
        // 2.1.获取派单服务人员列表
        List<ServeProviderDTO> serveProvidersOfServe = searchDispatchInfo(ordersDispatch.getCityCode(),
                ordersDispatch.getServeItemId(),
                100,
                serveTime,
                dispatchStrategyEnum,
                ordersDispatch.getLon(),
                ordersDispatch.getLat(),
                10);
        // 2.3.机构和服务人员列表合并，如果为空当前派单失败
        log.info("派单筛选前数据,id:{},{}",id, serveProvidersOfServe);
        if (CollUtils.isEmpty(serveProvidersOfServe)) {
            log.info("id:{}匹配不到人",id);
            return;
        }

        // 3.派单过规则策略
        // 3.1.获取派单策略
        IDispatchStrategy dispatchStrategy = dispatchStrategyManager.get(dispatchStrategyEnum);
        // 3.2.过派单策略，并返回一个派单服务人员或机构
        ServeProviderDTO serveProvider = dispatchStrategy.getPrecedenceServeProvider(serveProvidersOfServe);
        log.info("id:{},serveProvider : {}",id, JsonUtils.toJsonStr(serveProvider));

//        // 4.机器抢单
        OrderSeizeReqDTO orderSeizeReqDTO = new OrderSeizeReqDTO();
        orderSeizeReqDTO.setSeizeId(id);
        orderSeizeReqDTO.setServeProviderId(serveProvider.getId());
        orderSeizeReqDTO.setServeProviderType(serveProvider.getServeProviderType());
        ordersSeizeApi.machineSeize(orderSeizeReqDTO);
    }

    private static final List<String> INCLUDE_FIELD_NAMES = Arrays.asList(
            LambdaUtils.getFieldName(ServeProviderInfo::getId), LambdaUtils.getUnderLineFieldName(ServeProviderInfo::getEvaluationScore),
            LambdaUtils.getUnderLineFieldName(ServeProviderInfo::getAcceptanceNum), LambdaUtils.getUnderLineFieldName(ServeProviderInfo::getServeProviderType));

    @Resource
    private ElasticSearchTemplate elasticSearchTemplate;

    @Override
    public List<ServeProviderDTO> searchDispatchInfo(String cityCode, long serveItemId, double maxDistance, int serveTime, DispatchStrategyEnum dispatchStrategyEnum, Double lon, Double lat, int limit) {
        SearchRequest.Builder builder = new SearchRequest.Builder();
        // 1.匹配
        builder.query(qb ->
                qb.bool(b -> {
                    // 服务人员是有服务时间冲突
                    b.mustNot(mn -> mn.term(t -> t.field(LambdaUtils.getUnderLineFieldName(ServeProviderInfo::getServeTimes)).value(serveTime)));
                    // 开启接单
                    b.must(m -> m.term(t -> t.field(LambdaUtils.getUnderLineFieldName(ServeProviderInfo::getPickUp)).value(1)));
                    // 完成认证
                    b.must(m -> m.term(t -> t.field(LambdaUtils.getUnderLineFieldName(ServeProviderInfo::getSettingStatus)).value(1)));
                    // 当前所在城市
                    b.must(m -> m.term(t -> t.field(LambdaUtils.getUnderLineFieldName(ServeProviderInfo::getCityCode)).value(cityCode)));
                    // 服务项匹配
                    b.must(m -> m.term(t -> t.field(LambdaUtils.getUnderLineFieldName(ServeProviderInfo::getServeItemIds)).value(serveItemId)));
                    // 校验服务人员或服务机构状态是否正常，0：表示正常，1：表示禁用
                    b.must(m -> m.term(t -> t.field(LambdaUtils.getUnderLineFieldName(ServeProviderInfo::getStatus)).value(0)));
                    // 距离限制
                    b.must(m -> m.geoDistance(geoDistance -> {
                        geoDistance.distance(maxDistance + "km");
                        geoDistance.field(LambdaUtils.getFieldName(ServeProviderInfo::getLocation));
                        geoDistance.location(location ->
                                location.latlon(latlon -> latlon.lon(lon).lat(lat)));
                        return geoDistance;
                    }));

                    return b;
                }));
        // 2.排序
        builder.sort(getSortOptions(dispatchStrategyEnum, lon, lat));

        // 3.目标字段
        builder.source(s -> s.filter(ss -> ss.includes(INCLUDE_FIELD_NAMES)));
        // 4.查询数量限制
        builder.size(30);

        builder.index(SERVER_PROVIDER_INFO);

        // 5.查询数据
        SearchResponse<ServeProviderDTO> searchResponse = elasticSearchTemplate.opsForDoc().search(builder.build(), ServeProviderDTO.class);

        // 6.返回数据
        // 6.1.未查询到数据返回空
        if (SearchResponseUtils.isNotSuccess(searchResponse)) {
            return null;
        }
        // 6.2.距离优先，使用距离排序可以获取服务人员到服务地点距离
        if (DispatchStrategyEnum.DISTANCE.equals(dispatchStrategyEnum)) {
            return SearchResponseUtils.getResponse(searchResponse, (hit, source) -> {
                //设置距离值
                source.setAcceptanceDistance(NumberUtils.parseInt(CollUtils.getFirst(hit.sort())));
                //接单数为空默认为0
                source.setAcceptanceNum(ObjectUtils.isNull(source.getAcceptanceNum())?0:source.getAcceptanceNum());

            });
        } else {
            // 6.2.评分优先/最少接单优先无需设置距离
            return SearchResponseUtils.getResponse(searchResponse);
        }

    }


    /**
     * 排序策略
     *
     * @param dispatchStrategyEnum
     * @return
     */
    private List<SortOptions> getSortOptions(DispatchStrategyEnum dispatchStrategyEnum, Double lon, Double lat) {

        List<SortOptions> sortOptions = new ArrayList<>();
        switch (dispatchStrategyEnum) {
            case DISTANCE:
                GeoLocation.Builder geoLocationBuilder = new GeoLocation.Builder();
                geoLocationBuilder.latlon(latlon -> latlon.lon(lon).lat(lat));
                sortOptions.add(SortOptions.of(fn -> fn.geoDistance(geoDistance -> {
                    geoDistance.field(LambdaUtils.getFieldName(ServeProviderInfo::getLocation));
                    geoDistance.location(CollUtils.singletonList(geoLocationBuilder.build()));
                    geoDistance.distanceType(GeoDistanceType.Arc);
                    geoDistance.unit(DistanceUnit.Kilometers);
                    geoDistance.order(SortOrder.Asc);
                    return geoDistance;
                })));
                break;
            case EVELUATION_SCORE:
                sortOptions.add(SortOptions.of(fn -> fn.field(field -> {
                    field.field(LambdaUtils.getUnderLineFieldName(ServeProviderInfo::getEvaluationScore));
                    // 不存在设置为最低优先级
                    field.missing(5);
                    field.order(SortOrder.Desc);
                    return field;
                })));
                break;
            case LEAST_ACCEPT_ORDER:
                sortOptions.add(SortOptions.of(fn -> fn.field(field -> {
                    field.field(LambdaUtils.getUnderLineFieldName(ServeProviderInfo::getAcceptanceNum));
                    field.order(SortOrder.Asc);
                    // 不存在接单量按0处理
                    field.missing(0);
                    return field;
                })));
        }
        return sortOptions;
    }
}
