package com.jzo2o.orders.manager.service.impl;

import com.jzo2o.orders.base.mapper.OrdersDispatchMapper;
import com.jzo2o.orders.base.mapper.OrdersSeizeMapper;
import com.jzo2o.orders.base.utils.RedisUtils;
import com.jzo2o.orders.manager.service.ISeizeDispatchService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.jzo2o.orders.base.constants.RedisConstants.RedisKey.ORDERS_RESOURCE_STOCK;

@Service
public class SeizeDispatchServiceImpl implements ISeizeDispatchService {


    @Resource
    private RedisTemplate redisTemplate;


    @Resource
    private OrdersSeizeMapper ordersSeizeMapper;
    @Resource
    private OrdersDispatchMapper ordersDispatchMapper;


    @Override
    public void clearSeizeDispatchPool(String cityCode, Long id) {
        String resourceStockRedisKey = String.format(ORDERS_RESOURCE_STOCK, RedisUtils.getCityIndex(cityCode));
        ordersDispatchMapper.deleteById(id);
        ordersSeizeMapper.deleteById(id);
        redisTemplate.opsForHash().delete(resourceStockRedisKey, id);
    }

}
