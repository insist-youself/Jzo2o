package com.jzo2o.orders.seize.controller.worker;

import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.orders.seize.model.dto.request.OrdersSeizeReqDTO;
import com.jzo2o.orders.seize.model.dto.request.OrdersSerizeListReqDTO;
import com.jzo2o.orders.seize.model.dto.response.OrdersSeizeListResDTO;
import com.jzo2o.orders.seize.service.IOrdersSeizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(tags = "服务端 - 抢单相关接口")
@RequestMapping("/worker")
@Slf4j
public class WorkerOrdersSeizeController {

    @Resource
    private IOrdersSeizeService ordersSeizeService;

    @GetMapping("")
    @ApiOperation("服务端抢单列表")
    public OrdersSeizeListResDTO queryForList(OrdersSerizeListReqDTO ordersSerizeListReqDTO) {
        return ordersSeizeService.queryForList(ordersSerizeListReqDTO);
    }

    @PostMapping("")
    @ApiOperation("服务端抢单")
    public void seize(@RequestBody OrdersSeizeReqDTO ordersSeizeReqDTO) {
        ordersSeizeService.seize(ordersSeizeReqDTO.getId(), UserContext.currentUserId(), UserContext.currentUser().getUserType(), false);
    }
}
