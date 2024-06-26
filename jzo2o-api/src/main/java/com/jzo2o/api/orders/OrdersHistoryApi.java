package com.jzo2o.api.orders;

import com.jzo2o.utils.MyQueryMapEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "jzo2o-orders-history", value = "jzo2o-orders-history", path = "/orders-history/inner/orders", configuration = MyQueryMapEncoder.class)
public interface OrdersHistoryApi {

    /**
     * 批量获取已经同步的历史订单id列表
     * @param ids 查询的订单id列表
     * @return 已经同步完成的订单id列表
     */
    @GetMapping("/batchGetSyncedOrdersIds")
    List<Long> batchGetSyncedOrdersIds(@RequestParam("ids") List<Long> ids);

    /**
     * 批量获取已经同步的历史服务单id列表
     * @param ids 查询的服务单id列表
     * @return 已经同步完成的服务单id列表
     */
    @GetMapping("/batchGetSyncedOrdersServeIds")
    List<Long> batchGetSyncedOrdersServeIds(@RequestParam("ids") List<Long> ids);
}
