package com.jzo2o.orders.history.controller.inner;

import com.jzo2o.api.orders.OrdersHistoryApi;
import com.jzo2o.orders.history.service.IHistoryOrdersServeService;
import com.jzo2o.orders.history.service.IHistoryOrdersService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户端-历史订单相关接口")
@RestController("innerHistoryOrdersController")
@RequestMapping("/inner/orders")
public class OrdersHistoryInnerController implements OrdersHistoryApi {

    @Resource
    private IHistoryOrdersService historyOrdersService;

    @Resource
    private IHistoryOrdersServeService historyOrdersServeService;

    @Override
    @GetMapping("/batchGetSyncedOrdersIds")
    public List<Long> batchGetSyncedOrdersIds(@RequestParam("ids") List<Long> ids) {
        return historyOrdersService.queryExistIdsByIds(ids);
    }

    @Override
    @GetMapping("/batchGetSyncedOrdersServeIds")
    public List<Long> batchGetSyncedOrdersServeIds(@RequestParam("ids") List<Long> ids) {
        return historyOrdersServeService.queryExistIdsByIds(ids);
    }
}
