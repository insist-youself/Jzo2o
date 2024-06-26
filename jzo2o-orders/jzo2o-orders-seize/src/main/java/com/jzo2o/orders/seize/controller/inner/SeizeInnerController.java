package com.jzo2o.orders.seize.controller.inner;

import com.jzo2o.api.orders.OrdersSeizeApi;
import com.jzo2o.api.orders.dto.request.OrderSeizeReqDTO;
import com.jzo2o.orders.seize.service.IOrdersSeizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "抢单内部相关接口")
@Slf4j
@RequestMapping("/inner/seize")
public class SeizeInnerController implements OrdersSeizeApi {

    @Resource
    private IOrdersSeizeService ordersSeizeService;

    @Override
    @ApiOperation("机器抢单接口")
    @PostMapping
    public void machineSeize(@RequestBody OrderSeizeReqDTO orderSeizeReqDTO) {
        ordersSeizeService.seize(orderSeizeReqDTO.getSeizeId(), orderSeizeReqDTO.getServeProviderId(), orderSeizeReqDTO.getServeProviderType(), true);
    }
}
