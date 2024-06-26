package com.jzo2o.orders.seize.handler;

import com.jzo2o.api.foundations.RegionApi;
import com.jzo2o.api.foundations.dto.response.ConfigRegionInnerResDTO;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.es.core.ElasticSearchTemplate;
import com.jzo2o.orders.base.constants.EsIndexConstants;
import com.jzo2o.orders.base.model.domain.OrdersDispatch;
import com.jzo2o.orders.base.model.domain.OrdersSeize;
import com.jzo2o.orders.base.utils.RedisUtils;
import com.jzo2o.orders.seize.model.dto.response.OrdersSeizeListResDTO;
import com.jzo2o.orders.seize.service.IOrdersDispatchService;
import com.jzo2o.orders.seize.service.IOrdersSeizeService;
import com.jzo2o.redis.annotations.Lock;
import com.jzo2o.redis.constants.RedisSyncQueueConstants;
import com.jzo2o.redis.sync.SyncManager;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.jzo2o.orders.base.constants.RedisConstants.RedisFormatter.SEIZE_TIME_OUT;
import static com.jzo2o.orders.base.constants.RedisConstants.RedisKey.ORDERS_RESOURCE_STOCK;
import static com.jzo2o.orders.base.constants.RedisConstants.RedisKey.ORERS_SEIZE_SYNC_QUEUE_NAME;

/**
 * 抢单xxl-job任务
 */
@Component
@Slf4j
public class SeizeJobHandler {

    @Resource
    private IOrdersSeizeService ordersSeizeService;



    @Resource(name = "seizeSyncExecutor")
    private Executor seizeSyncExecutor;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SyncManager syncManager;

    @Resource
    private RegionApi regionApi;

    @Resource
    private SeizeJobHandler owner;
    @Resource
    private ElasticSearchTemplate elasticSearchTemplate;

    @Resource
    private IOrdersDispatchService ordersDispatchService;

    /**
     * 当前时间距离服务预约时间间隔小于配置值时进入派单池
     */
    @XxlJob("seizeTimeoutIntoDispatchPoolJob")
    public void seizeTimeoutIntoDispatchPoolJob() {
        List<ConfigRegionInnerResDTO> configRegionInnerResDTOS = regionApi.findAll();
        for (ConfigRegionInnerResDTO configRegionInnerResDTO : configRegionInnerResDTOS) {
            try {
                //传入配置的下单时间距离服务预约时间间隔
                owner.seizeTimeoutIntoDispatchPool(configRegionInnerResDTO.getCityCode(), configRegionInnerResDTO.getDiversionInterval());

            } catch (Exception e) {
                log.error("抢单订单超时处理异常，e:", e);
            }
        }
    }

    /**
     *当前时间距离服务预约时间间隔小于配置值时进入派单池，以城市为单位进行处理
     * @param cityCode
     * @param timeoutInterval
     */
    @Transactional(rollbackFor = Exception.class)
    public void seizeTimeoutIntoDispatchPool(String cityCode, Integer timeoutInterval) {
        // 1.查询满足条件的且未处理的抢单列表
        List<OrdersSeize> ordersSeizes = ordersSeizeService.queryTimeoutSeizeOrders(cityCode, timeoutInterval);
        if (CollUtils.isEmpty(ordersSeizes)) {
            return;
        }

        // 2.修改抢单超时标记并，派单
        List<Long> ids = ordersSeizes.stream().map(OrdersSeize::getId).collect(Collectors.toList());
        List<OrdersDispatch> ordersDispatches = BeanUtils.copyToList(ordersSeizes, OrdersDispatch.class);
        //2.1.指定同步到派单池
        ordersDispatchService.saveOrUpdateBatch(ordersDispatches,100);
        // 2.2.标记订单抢单超时
        ordersSeizeService.batchTimeout(ids);
    }

    /**
     * 到达服务预约时间，终止抢单
     */
    @XxlJob("arriveServeStartTimeStopSeizeJob")
    public void arriveServeStartTimeStopSeizeJob() {
        // 1.到达服务预约时间的抢单信息
        List<OrdersSeize> ordersSeizes = ordersSeizeService.queryArriveServeStartTimeSeizeOrder();
        if (CollUtils.isNotEmpty(ordersSeizes)) {
            List<Long> ids = ordersSeizes.stream().map(OrdersSeize::getId).collect(Collectors.toList());
            // 2.批量删除抢单列表
            ordersSeizeService.batchDeleteByIds(ids);
            // 3.批量删除库存
            ordersSeizes.stream().collect(Collectors.groupingBy(OrdersSeize::getCityCode))//按城市编码对抢单记录分组
                    .forEach((cityCode, ordersSeizeList) -> {
                        //抢单库存redis key
                        String stockRedisKey = String.format(ORDERS_RESOURCE_STOCK, RedisUtils.getCityIndex(cityCode));
                        //取出抢单id列表
                        List<Long> idList = ordersSeizeList.stream().map(OrdersSeize::getId).collect(Collectors.toList());
                        //删除抢单库存中对应的抢单库存
                        redisTemplate.opsForHash().delete(stockRedisKey, idList);
                    });
        }


        //删除ES中的记录
        List<OrdersSeizeListResDTO.OrdersSeize> ordersSeizesFromEs = ordersSeizeService.queryArriveServeStartTimeSeizeOrderFromEs();
        if(ObjectUtils.isNotEmpty(ordersSeizesFromEs)){
            List<Long> fieldValues = CollUtils.getFieldValues(ordersSeizesFromEs, OrdersSeizeListResDTO.OrdersSeize::getId);
            elasticSearchTemplate.opsForDoc().batchDelete(EsIndexConstants.ORDERS_SEIZE,fieldValues);

        }

    }


    /**
     * 抢单成功同步任务
     */
    @XxlJob("seizeSyncJob")
    public void seizeSyncJob() {
        syncManager.start(ORERS_SEIZE_SYNC_QUEUE_NAME, RedisSyncQueueConstants.STORAGE_TYPE_HASH, RedisSyncQueueConstants.MODE_SINGLE);
    }

}
