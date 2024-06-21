package com.jzo2o.orders.seize.handler;

import cn.hutool.json.JSONArray;
import com.jzo2o.api.customer.ServeProviderApi;
import com.jzo2o.api.customer.dto.response.ServeProviderResDTO;
import com.jzo2o.common.utils.JsonUtils;
import com.jzo2o.common.utils.NumberUtils;
import com.jzo2o.orders.base.model.domain.OrdersSeize;
import com.jzo2o.orders.seize.service.IOrdersSeizeService;
import com.jzo2o.redis.handler.SyncProcessHandler;
import com.jzo2o.redis.model.SyncMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 抢单成功同步任务
 */
@Component("ORDERS:SEIZE:SYNC")
@Slf4j
public class SeizeSyncProcessHandler implements SyncProcessHandler<Object> {

    @Resource
    private IOrdersSeizeService ordersSeizeService;


    @Resource
    private ServeProviderApi serveProviderApi;

    @Override
    public void batchProcess(List<SyncMessage<Object>> multiData) {
        throw new RuntimeException("不支持批量处理");
    }

    @Override
    public void singleProcess(SyncMessage<Object> singleData) {
        log.info("抢单结果同步开始 id ： {}",singleData.getKey());
        // 抢单信息放在value中，内容格式：[serveProviderId,serveProviderType,isMatchine（0，表示人工抢单，1：表示机器抢单）]
        JSONArray seizeResult = JsonUtils.parseArray(singleData.getValue());
        // 服务人员或机构id
        Long serveProviderId = seizeResult.getLong(0);
        // 用户类型
        Integer serveProviderType = seizeResult.getInt(1);
        // 是否是机器抢单
        boolean isMatchine = seizeResult.getBool(2);

        // 抢单id
        Long seizeId = NumberUtils.parseLong(singleData.getKey());
        // 抢单不在无需继续处理
        OrdersSeize ordersSeize = ordersSeizeService.getById(seizeId);
        if (ordersSeize == null) {
            return;
        }

        // 处理抢单结果
        ordersSeizeService.seizeOrdersSuccess(ordersSeize, serveProviderId, serveProviderType, isMatchine);
        log.info("抢单结果同步结束 id ： {}",singleData.getKey());
    }
}
