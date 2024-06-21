package com.jzo2o.orders.seize.service.impl;

import co.elastic.clients.elasticsearch._types.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.customer.ServeProviderApi;
import com.jzo2o.api.customer.ServeSkillApi;
import com.jzo2o.api.customer.dto.response.ServeProviderResDTO;
import com.jzo2o.api.foundations.RegionApi;
import com.jzo2o.api.foundations.dto.response.ConfigRegionInnerResDTO;
import com.jzo2o.common.constants.ErrorInfo;
import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.utils.*;
import com.jzo2o.es.core.ElasticSearchTemplate;
import com.jzo2o.es.utils.SearchResponseUtils;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.orders.base.config.OrderStateMachine;
import com.jzo2o.orders.base.constants.EsIndexConstants;
import com.jzo2o.orders.base.constants.FieldConstants;
import com.jzo2o.orders.base.constants.OrdersOriginType;
import com.jzo2o.orders.base.constants.RedisConstants;
import com.jzo2o.orders.base.enums.OrderStatusChangeEventEnum;
import com.jzo2o.orders.base.enums.OrderStatusEnum;
import com.jzo2o.orders.base.enums.ServeStatusEnum;
import com.jzo2o.orders.base.mapper.OrdersDispatchMapper;
import com.jzo2o.orders.base.mapper.OrdersMapper;
import com.jzo2o.orders.base.mapper.OrdersSeizeMapper;
import com.jzo2o.orders.base.mapper.OrdersServeMapper;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.base.model.domain.OrdersSeize;
import com.jzo2o.orders.base.model.domain.OrdersServe;
import com.jzo2o.orders.base.model.dto.OrderSnapshotDTO;
import com.jzo2o.orders.base.properties.DispatchProperties;
import com.jzo2o.orders.base.service.*;
import com.jzo2o.orders.base.utils.RedisUtils;
import com.jzo2o.orders.base.utils.ServeTimeUtils;
import com.jzo2o.orders.seize.model.domain.OrdersSeizeInfo;
import com.jzo2o.orders.seize.model.dto.request.OrdersSerizeListReqDTO;
import com.jzo2o.orders.seize.model.dto.response.OrdersSeizeListResDTO;
import com.jzo2o.orders.seize.service.IOrdersDispatchService;
import com.jzo2o.orders.seize.service.IOrdersSeizeService;
import com.jzo2o.orders.seize.service.IOrdersServeService;
import com.jzo2o.orders.seize.service.IServeProviderSyncService;
import com.jzo2o.redis.annotations.Lock;
import com.jzo2o.redis.helper.CacheHelper;
import com.jzo2o.redis.utils.RedisSyncQueueUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.jzo2o.orders.base.constants.ErrorInfo.Msg.*;
import static com.jzo2o.orders.base.constants.FieldConstants.*;
import static com.jzo2o.orders.base.constants.FieldConstants.LOCATION;
import static com.jzo2o.orders.base.constants.RedisConstants.RedisKey.ORDERS_RESOURCE_STOCK;
import static com.jzo2o.orders.base.constants.RedisConstants.RedisKey.SERVE_PROVIDER_STATE;

/**
 * <p>
 * 抢单池 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-08-15
 */
@Service
@Slf4j
public class OrdersSeizeServiceImpl extends ServiceImpl<OrdersSeizeMapper, OrdersSeize> implements IOrdersSeizeService {

    @Resource
    private ServeProviderApi serveProviderApi;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private IOrdersServeService ordersServeService;

    @Resource
    private OrdersServeMapper ordersServeMapper;

//    @Resource
//    private IOrdersDispatchService ordersDispatchService;

    @Resource
    private CacheHelper cacheHelper;

    @Resource(name = "seizeOrdersScript")
    private DefaultRedisScript<String> seizeOrdersScript;

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private ServeSkillApi serveSkillApi;

    @Resource
    private DispatchProperties dispatchProperties;



    @Resource
    private ElasticSearchTemplate elasticSearchTemplate;

    @Resource
    private IServeProviderSyncService serveProviderSyncService;

    @Resource
    private IOrdersSeizeService ordersSeizeService;

    @Resource
    private OrderStateMachine orderStateMachine;

    @Resource
    private RegionApi regionApi;

    @Resource
    private OrdersDispatchMapper ordersDispatchMapper;

    @Override
    public List<OrdersSeize> queryTimeoutSeizeOrders(String cityCode, Integer timeoutInterval) {
        //当前时间加上配置的时间间隔
        LocalDateTime maxServeStartTime = DateUtils.now().plusMinutes(timeoutInterval);
        LambdaQueryWrapper<OrdersSeize> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.or()
                .eq(OrdersSeize::getCityCode, cityCode)
                .eq(OrdersSeize::getIsTimeOut, false)
                // 查询当前时间距离服务预约时间间隔小于指定值
                .le(OrdersSeize::getServeStartTime, maxServeStartTime)
                //预约时间大于当前时间
                .ge(OrdersSeize::getServeStartTime,DateUtils.now());
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchTimeout(List<Long> ids) {
        lambdaUpdate()
                .set(OrdersSeize::getIsTimeOut, true)
                .in(OrdersSeize::getId, ids)
                .update();
    }



    @Override
    public List<OrdersSeize> queryArriveServeStartTimeSeizeOrder() {
        return lambdaQuery()
                .le(OrdersSeize::getServeStartTime, DateUtils.now())
                .list();
    }
    /**
     * 查询到达预约时间还未抢单成功的记录 从es中查询
     * @return
     */
    @Override
    public List<OrdersSeizeListResDTO.OrdersSeize> queryArriveServeStartTimeSeizeOrderFromEs(){
        SearchRequest.Builder builder = new SearchRequest.Builder();
        //当前时间
        //得到服务开始时间(yyMMddHH)
        String now = DateTimeFormatter.ofPattern("yyMMddHH").format(LocalDateTime.now());

        builder.query(query ->
                query.range(range -> {
                    range.field("serve_time")
                            .lte(JsonData.of(Integer.parseInt(now)));
                    return range;
                }));
        SearchResponse<OrdersSeizeInfo> searchResponse = elasticSearchTemplate.opsForDoc().search(builder.build(), OrdersSeizeInfo.class);
        if (SearchResponseUtils.isSuccess(searchResponse)) {
            List<OrdersSeizeListResDTO.OrdersSeize> collect = searchResponse.hits().hits()
                    .stream().map(hit -> {
                        OrdersSeizeListResDTO.OrdersSeize ordersSeize = BeanUtils.toBean(hit.source(), OrdersSeizeListResDTO.OrdersSeize.class);
                        return ordersSeize;
                    })
                    .collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    @Override
    public void batchDeleteByIds(List<Long> ids) {
        LambdaQueryWrapper<OrdersSeize> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(OrdersSeize::getId, ids);
        int deleteResult = baseMapper.delete(lambdaQueryWrapper);
        log.info("批量删除抢单数据： {}",ids);

    }

    @Override
    public OrdersSeizeListResDTO queryForList(OrdersSerizeListReqDTO ordersSerizeListReqDTO) {

        // 1.校验是否可以查询（认证通过，开启抢单）
        ServeProviderResDTO detail = serveProviderApi.getDetail(UserContext.currentUserId());
        // 验证设置状态
        if (detail.getSettingsStatus() != 1 || !detail.getCanPickUp()) {
            return OrdersSeizeListResDTO.empty();
        }
        // 2.查询准备 （距离、技能，时间冲突）
        // 距离
        Double serveDistance = ordersSerizeListReqDTO.getServeDistance();
        if(ObjectUtils.isNull(ordersSerizeListReqDTO.getServeDistance())) {
            // 区域默认配置配置
            ConfigRegionInnerResDTO configRegionInnerResDTO = regionApi.findConfigRegionByCityCode(detail.getCityCode());
            serveDistance = (detail.getType() == UserType.INSTITUTION)
                    ? configRegionInnerResDTO.getInstitutionServeRadius().doubleValue() : configRegionInnerResDTO.getStaffServeRadius().doubleValue();
        }
        // 技能
        List<Long> serveItemIds = serveSkillApi.queryServeSkillListByServeProvider(UserContext.currentUserId(), UserContext.currentUser().getUserType(), detail.getCityCode());
        if(CollUtils.isEmpty(serveItemIds)) {
            log.info("当前机构或服务人员没有对应技能");
            return OrdersSeizeListResDTO.empty();
        }


        // 3.查询符合条件的抢单列表id
        List<OrdersSeizeListResDTO.OrdersSeize> ordersSeizes = getOrdersSeizeId(
                serveItemIds, detail.getLon(), detail.getLat(), serveDistance, detail.getCityCode(), ordersSerizeListReqDTO);

        return new OrdersSeizeListResDTO(CollUtils.defaultIfEmpty(ordersSeizes, new ArrayList<>()));
    }

    /**
     * 获取抢单id，抢单类型，抢单预约服务时间
     * @param serveItemIds 服务项id
     * @param lon 当前服务人员或机构所在位置经度
     * @param lat 当前服务人员或机构所在纬度
     * @param distanceLimit 抢单距离限制
     * @param cityCode 城市编码
     * @param ordersSerizeListReqDTO 抢单查询参数
     * @return
     */
    private List<OrdersSeizeListResDTO.OrdersSeize> getOrdersSeizeId(List<Long> serveItemIds, Double lon, Double lat, double distanceLimit, String cityCode, OrdersSerizeListReqDTO ordersSerizeListReqDTO) {

        // 服务项查询条件
        List<FieldValue> serveItemIdFieldValue = serveItemIds.stream().map(serveItemId -> FieldValue.of(serveItemId)).collect(Collectors.toList());

        SearchRequest.Builder builder = new SearchRequest.Builder();
        builder.query(query ->
                query.bool(bool -> {
                    // 所在城市
                    bool.must(must -> must.term(term -> term.field(CITY_CODE).value(cityCode)));
                    // 服务类型
                    if(ordersSerizeListReqDTO.getServeTypeId() != null) {
                        bool.must(must -> must.term(term -> term.field(SERVE_TYPE_ID).value(ordersSerizeListReqDTO.getServeTypeId())));
                    }
                    // 服务项
                    bool.must(must -> must.terms(terms -> terms.field(SERVE_ITEM_ID).terms(t -> t.value(serveItemIdFieldValue))));
                    // 距离条件
                    bool.must(must -> {
                        must.geoDistance(geoDistance -> {
                            geoDistance.field(LOCATION);
                            geoDistance.location(location -> location.latlon(latlon -> latlon.lon(lon).lat(lat)));
                            geoDistance.distance(distanceLimit + "km");
                            return geoDistance;});
                        return must;
                    });

                    // 关键字匹配 满足一个字段即可 服务项名称，服务类型名称，服务地址
                    if (StringUtils.isNotEmpty(ordersSerizeListReqDTO.getKeyWord())) {
                        bool.must(must -> must.match(match -> match.field(FieldConstants.KEY_WORDS).query(ordersSerizeListReqDTO.getKeyWord())));
                    }

                    return bool;
                }));
        // 排序 根据距离排序
        List<SortOptions> sortOptions = new ArrayList<>();
        sortOptions.add(SortOptions.of(sortOption -> sortOption.geoDistance(
                geoDistance -> {
                    geoDistance.field(LOCATION);
                    geoDistance.distanceType(GeoDistanceType.Arc);
                    geoDistance.order(SortOrder.Asc);
                    geoDistance.unit(DistanceUnit.Kilometers);
                    geoDistance.location(location -> location.latlon(latlon -> latlon.lat(lat).lon(lon)));
                    return geoDistance;
                }
        )));
        builder.sort(sortOptions);
        // 索引
        builder.index(EsIndexConstants.ORDERS_SEIZE);

        // 滚动分页,根据距离滚动分页
        if (ordersSerizeListReqDTO.getLastRealDistance() != null) {
            builder.searchAfter(ordersSerizeListReqDTO.getLastRealDistance().toString());
        }

        // 检索数据
        SearchResponse<OrdersSeizeInfo> searchResponse = elasticSearchTemplate.opsForDoc().search(builder.build(), OrdersSeizeInfo.class);
        if (SearchResponseUtils.isSuccess(searchResponse)) {
            return searchResponse.hits().hits()
                    .stream().map(hit -> {
                        // 从sort字段中获取实际距离
                        double realDistance = NumberUtils.parseDouble(CollUtils.getFirst(hit.sort()));
                        OrdersSeizeListResDTO.OrdersSeize ordersSeize = BeanUtils.toBean(hit.source(), OrdersSeizeListResDTO.OrdersSeize.class);
                        ordersSeize.setRealDistance(realDistance);
                        return ordersSeize;
                    })
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    @Lock(formatter = RedisConstants.RedisFormatter.SEIZE, time = 300)
    public void seize(Long id, Long serveProviderId, Integer serveProviderType, Boolean isMatchine) {

        // 1.抢单校验
        // 1.1.校验是否可以查询（认证通过，开启抢单）
        ServeProviderResDTO detail = serveProviderApi.getDetail(serveProviderId);
        if (!detail.getCanPickUp() || detail.getSettingsStatus() != 1) {
            throw new CommonException(ErrorInfo.Code.SEIZE_ORDERS_FAILD, SEIZE_ORDERS_RECEIVE_CLOSED);
        }
        // 1.2.校验抢单是否存在
        OrdersSeize ordersSeize = ordersSeizeService.getById(id);
        // 校验订单是否还存在，如果订单为空或id不存在，则认为订单已经不在
        if (ordersSeize == null || ObjectUtils.isNull(ordersSeize.getId())) {
            throw new CommonException(ErrorInfo.Code.SEIZE_ORDERS_FAILD, SEIZE_ORDERS_FAILD);
        }
        ConfigRegionInnerResDTO configRegionInnerResDTO = regionApi.findConfigRegionByCityCode(detail.getCityCode());


        // 城市编码最后1位序号
        int index = RedisUtils.getCityIndex(detail.getCityCode());
        // 1.3.校验时间冲突
        // 服务时间状态redisKey
        String serveProviderStateRedisKey = String.format(SERVE_PROVIDER_STATE, index);
        int serveTime = ServeTimeUtils.getServeTimeInt(ordersSeize.getServeStartTime());
        if(serveProviderType == UserType.WORKER) {
            Object serveTimes = redisTemplate.opsForHash().get(serveProviderStateRedisKey, serveProviderId + "_times");
            if(ObjectUtils.isNotNull(serveTimes) && serveTimes.toString().contains(serveTime+"")){
                throw new CommonException(ErrorInfo.Code.SEIZE_ORDERS_FAILD, SEIZE_ORDERS_SERVE_TIME_EXISTS);
            }
        }
        // 1.4.订单数量已达上限
        // 接单数量上限
        int receiveOrderMax = (serveProviderType == UserType.INSTITUTION) ? configRegionInnerResDTO.getInstitutionReceiveOrderMax() : configRegionInnerResDTO.getStaffReceiveOrderMax();

        Object ordersNum = redisTemplate.opsForHash().get(serveProviderStateRedisKey, serveProviderId + "_num");
        if(ObjectUtils.isNotNull(ordersNum) && NumberUtils.parseInt(ordersNum.toString()) >= receiveOrderMax){
            throw new CommonException(ErrorInfo.Code.SEIZE_ORDERS_FAILD, SEIZE_ORDERS_RECEIVE_ORDERS_NUM_OVER);
        }

        // 2.执行redis脚本

        // 2.1.redisKey
        // 抢单结果同步队列 redis key
        String ordersSeizeSyncRedisKey = RedisSyncQueueUtils.getQueueRedisKey(RedisConstants.RedisKey.ORERS_SEIZE_SYNC_QUEUE_NAME, index);
        // 库存redisKey
        String resourceStockRedisKey = String.format(ORDERS_RESOURCE_STOCK, index);

        log.debug("抢单key：{}，values:{}", Arrays.asList(ordersSeizeSyncRedisKey, resourceStockRedisKey),
                Arrays.asList(id, serveProviderId,serveProviderType));
        // 2.2.执行lua脚本
        Object execute = redisTemplate.execute(seizeOrdersScript,
                // 序列化串行器
                new GenericJackson2JsonRedisSerializer(), new GenericJackson2JsonRedisSerializer(),
                Arrays.asList(ordersSeizeSyncRedisKey, resourceStockRedisKey, serveProviderStateRedisKey),
                id, serveProviderId,serveProviderType,isMatchine ? 1 : 0);
        log.debug("抢单结果 : {}", execute);

        // 3.处理lua脚本结果
        if (execute == null) {
            throw new CommonException(ErrorInfo.Code.SEIZE_ORDERS_FAILD, SEIZE_ORDERS_FAILD);
        }
        // 4.抢单结果判断 大于0抢单成功，-1/-2：库存数量不足，-3：抢单失败
        long result = NumberUtils.parseLong(execute.toString());
        if(result < 0) {
            throw new CommonException(ErrorInfo.Code.SEIZE_ORDERS_FAILD, SEIZE_ORDERS_FAILD);
        }
    }

//    @Override
//    public Integer getSeizeListDisplayNum(Integer currentProviderType) {
//        if (currentProviderType.equals(UserType.INSTITUTION)) {
//            return dispatchProperties.getSeizeListDispalyNumOfInstitution();
//        } else {
//            return dispatchProperties.getSeizeListDispalyNumOfServe();
//        }
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void seizeOrdersSuccess(OrdersSeize ordersSeize, Long serveProviderId, Integer serveProviderType, Boolean isMatchine) {

        // 1.校验服务单是否已经生成
        OrdersServe ordersServeInDb = ordersServeService.findById(ordersSeize.getId());
        if(ordersServeInDb != null){
            return;
        }
        // 2.生成服务单,
        OrdersServe ordersServe = BeanUtils.toBean(ordersSeize, OrdersServe.class);
        ordersServe.setCreateTime(null);
        ordersServe.setUpdateTime(null);
        // 服务单状态 机构抢单状态：待分配；服务人员抢单状态：待服务
        int serveStatus = UserType.WORKER == serveProviderType ? ServeStatusEnum.NO_SERVED.getStatus() : ServeStatusEnum.NO_ALLOCATION.getStatus();
        // 服务单来源类型,人工抢单来源抢单，值为1；机器抢单来源派单，值为2
        int ordersOriginType = isMatchine ? OrdersOriginType.DISPATCH : OrdersOriginType.SEIZE;
        ordersServe.setOrdersOriginType(ordersOriginType);
        ordersServe.setServeStatus(serveStatus);
        ordersServe.setServeProviderId(serveProviderId);
        ordersServe.setServeProviderType(serveProviderType);
        if(!ordersServeService.save(ordersServe)){
            return;
        }

        // 3.当前订单数量
        serveProviderSyncService.countServeTimesAndAcceptanceNum(serveProviderId, serveProviderType);

        String resourceStockRedisKey = String.format(ORDERS_RESOURCE_STOCK, RedisUtils.getCityIndex(ordersSeize.getCityCode()));
        Object stock = redisTemplate.opsForHash().get(resourceStockRedisKey, ordersSeize.getId());
        if (ObjectUtils.isNull(stock) || NumberUtils.parseInt(stock.toString()) <= 0) {
            ordersDispatchMapper.deleteById(ordersSeize.getId());
            ordersSeizeService.removeById(ordersSeize.getId());
            redisTemplate.opsForHash().delete(resourceStockRedisKey, ordersSeize.getId());
        }

        //状态机修改订单状态
//        OrderSnapshotDTO orderSnapshotDTO = OrderSnapshotDTO.builder()
//                .ordersStatus(OrderStatusEnum.NO_SERVE.getStatus()).build();
        Orders orders = ordersMapper.selectById(ordersSeize.getId());
        orderStateMachine.changeStatus(orders.getUserId(),String.valueOf(ordersSeize.getId()), OrderStatusChangeEventEnum.DISPATCH);

    }



    /**
     *
     *
     * @param serveItemIds  服务项
     * @param cityCode      城市编码
     * @param lon           当前服务人员或机构所在位置经度
     * @param lat           当前服务人员或机构所在纬度
     * @param distanceLimit 抢单距离限制
     * @return 抢单id列表
     */





}
