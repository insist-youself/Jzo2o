package com.jzo2o.orders.base.service.impl;

import com.jzo2o.api.foundations.RegionApi;
import com.jzo2o.api.foundations.ServeApi;
import com.jzo2o.api.foundations.dto.response.ConfigRegionInnerResDTO;
import com.jzo2o.api.foundations.dto.response.ServeAggregationResDTO;
import com.jzo2o.common.utils.*;
import com.jzo2o.orders.base.mapper.OrdersDispatchMapper;
import com.jzo2o.orders.base.mapper.OrdersSeizeMapper;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.base.model.domain.OrdersDispatch;
import com.jzo2o.orders.base.model.domain.OrdersSeize;
import com.jzo2o.orders.base.service.IOrdersDiversionCommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;

@Service
@Slf4j
public class OrdersDiversionCommonServiceImpl implements IOrdersDiversionCommonService {


    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    @Resource
    private RegionApi regionApi;

    @Resource
    private ServeApi serveApi;

    @Resource
    private OrdersDiversionCommonServiceImpl owner;

    @Resource
    private OrdersSeizeMapper ordersSeizeMapper;
    @Resource
    private OrdersDispatchMapper ordersDispatchMapper;

    @Override
    public void diversion(Orders orders) {
        log.debug("订单分流，id:{}",orders.getId());
        // 1.当前时间已超过服务预约时间则不再分流
        if (orders.getServeStartTime().compareTo(DateUtils.now()) < 0) {
            log.debug("订单{}当前时间已超过服务预约时间则不再分流",orders.getId());
            return;
        }
        ConfigRegionInnerResDTO configRegion = regionApi.findConfigRegionByCityCode(orders.getCityCode());
        ServeAggregationResDTO serveAggregationResDTO = serveApi.findById(orders.getServeId());
        //订单分流数据存储
        owner.diversionCommit(orders,configRegion,serveAggregationResDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void diversionCommit(Orders orders, ConfigRegionInnerResDTO configRegion, ServeAggregationResDTO serveAggregationResDTO) {
        //流间隔（单位分钟），即当前时间与服务预计开始时间的间隔
        Integer diversionInterval = configRegion.getDiversionInterval();

        //当前时间与服务预约时间的间隔
        Duration between = DateUtils.between(DateUtils.now(), orders.getServeStartTime());
        //服务类型名称
        String serveTypeName = ObjectUtils.get(serveAggregationResDTO, ServeAggregationResDTO::getServeTypeName);
        //服务类型id
        Long serveTypeId = ObjectUtils.get(serveAggregationResDTO, ServeAggregationResDTO::getServeTypeId);
        //服务项名称
        String serveItemName = ObjectUtils.get(serveAggregationResDTO, ServeAggregationResDTO::getServeItemName);
        //服务项图片
        String serveItemImg = ObjectUtils.get(serveAggregationResDTO, ServeAggregationResDTO::getServeItemImg);
        //用于排序,服务预约时间戳加订单号后5位
        long sortBy = DateUtils.toEpochMilli(orders.getServeStartTime()) + orders.getId() % 100000;
        OrdersSeize ordersSeize = OrdersSeize.builder()
                .id(orders.getId())
                .ordersAmount(orders.getRealPayAmount())
                .cityCode(orders.getCityCode())
                .serveTypeId(serveTypeId)
                .serveTypeName(serveTypeName)
                .serveItemId(orders.getServeItemId())
                .serveItemName(serveItemName)
                .serveItemImg(serveItemImg)
                .ordersAmount(orders.getRealPayAmount())
                .serveStartTime(orders.getServeStartTime())
                .serveAddress(orders.getServeAddress())
                .lon(orders.getLon())
                .lat(orders.getLat())
                .paySuccessTime(DateUtils.now())
                .paySuccessTime(orders.getPayTime())
                .sortBy(sortBy)
                .isTimeOut(BooleanUtils.toInt(between.toMinutes() < diversionInterval))
                .purNum(orders.getPurNum()).build();
        ordersSeizeMapper.insert(ordersSeize);
        //当前时间与服务预约时间的间隔 小于指定间隔则插入派单表
        if (between.toMinutes() < diversionInterval) {
            OrdersDispatch ordersDispatch = OrdersDispatch.builder()
                    .id(orders.getId())
                    .ordersAmount(orders.getRealPayAmount())
                    .cityCode(orders.getCityCode())
                    .serveTypeId(serveTypeId)
                    .serveTypeName(serveTypeName)
                    .serveItemId(orders.getServeItemId())
                    .serveItemName(serveItemName)
                    .serveItemImg(serveItemImg)
                    .ordersAmount(orders.getRealPayAmount())
                    .serveStartTime(orders.getServeStartTime())
                    .serveAddress(orders.getServeAddress())
                    .lon(orders.getLon())
                    .lat(orders.getLat())
                    .purNum(orders.getPurNum()).build();
            ordersDispatchMapper.insert(ordersDispatch);
        }
    }


}
