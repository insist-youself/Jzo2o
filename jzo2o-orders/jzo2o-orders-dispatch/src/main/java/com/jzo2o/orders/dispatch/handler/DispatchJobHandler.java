package com.jzo2o.orders.dispatch.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.orders.base.constants.RedisConstants;
import com.jzo2o.orders.base.mapper.OrdersDispatchMapper;
import com.jzo2o.orders.base.model.domain.OrdersDispatch;
import com.jzo2o.orders.dispatch.service.IOrdersDispatchService;
import com.jzo2o.redis.annotations.Lock;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.Executor;

import static com.jzo2o.orders.base.constants.RedisConstants.RedisKey.DISPATCH_LIST;

/**
 * 派单分发xxl-job定时任务
 */
@Component
@Slf4j
public class DispatchJobHandler {


    @Resource
    private RedisTemplate redisTemplate;

    @Resource(name = "dispatchExecutor")
    private Executor dispatchExecutor;


//    @Resource
//    private DispatchDistributeServiceImpl owner;

    @Resource
    private IOrdersDispatchService ordersDispatchService;

    @Resource
    private OrdersDispatchMapper ordersDispatchMapper;
    /**
     * 派单分发任务
     */
    @XxlJob("dispatch")
    public void dispatchDistributeJob(){
        while (true) {
            Set<Long> ordersDispatchIds = redisTemplate.opsForZSet().rangeByScore(DISPATCH_LIST, 0, DateUtils.getCurrentTime(), 0, 100);
            log.info("ordersDispatchIds:{}", ordersDispatchIds);
            if (CollUtils.isEmpty(ordersDispatchIds)) {
                log.debug("当前没有可以派单数据");
                return;
            }

            for (Long ordersDispatchId : ordersDispatchIds) {
                dispatch(ordersDispatchId);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //由于一个订单3分钟处理一次，所以加锁控制3分钟内只加入线程池一次
    @Lock(formatter = RedisConstants.RedisFormatter.JSONDISPATCHLIST,time = 180)
    public void dispatch(Long id) {
        dispatchExecutor.execute(() -> {
            ordersDispatchService.dispatch(id);
        });
    }


    /**
     * 到达服务预约时间删除派单池信息
     */
    @XxlJob("arriveServeStartTimeStopDispatchJob")
    public void arriveServeStartTimeStopDispatchJob() {
        LambdaQueryWrapper<OrdersDispatch> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.le(OrdersDispatch::getServeStartTime, DateUtils.now());
        ordersDispatchMapper.delete(lambdaQueryWrapper);
    }

}
