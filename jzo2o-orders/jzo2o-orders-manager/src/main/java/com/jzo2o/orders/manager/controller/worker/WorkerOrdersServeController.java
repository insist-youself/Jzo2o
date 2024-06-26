package com.jzo2o.orders.manager.controller.worker;

import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.orders.manager.model.dto.request.*;
import com.jzo2o.orders.manager.model.dto.response.OrdersServeDetailResDTO;
import com.jzo2o.orders.manager.model.dto.response.OrdersServeListResDTO;
import com.jzo2o.orders.manager.model.dto.response.OrdersServeResDTO;
import com.jzo2o.orders.manager.model.dto.response.OrdersServeStatusNumResDTO;
import com.jzo2o.orders.manager.service.IOrdersServeManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController("orders-worker")
@Api(tags = "服务端-服务单相关接口")
@RequestMapping("/worker")
public class WorkerOrdersServeController {

    @Resource
    private IOrdersServeManagerService ordersServeManagerService;
    
    @GetMapping("/queryForList")
    @ApiOperation("服务单列表，提供给服务端")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serveStatus", value = "服务状态，0：待分配，1：待服务，2：服务中，3：服务完成，4：已取消，5：被退单", required = true, dataTypeClass = Long.class),
            @ApiImplicitParam(name = "sortBy", value = "排序字段", required = true, dataTypeClass = Long.class)
    })
    public OrdersServeListResDTO queryForList(@RequestParam(value = "serveStatus",required = false) Integer serveStatus, @RequestParam(value = "sortBy",required = false) Long sortBy) {
        List<OrdersServeResDTO> ordersServeResDTOS = ordersServeManagerService.queryForList(UserContext.currentUserId(), serveStatus, sortBy);
        return new OrdersServeListResDTO(ordersServeResDTOS);
    }

    @DeleteMapping("/serve/{id}")
    @ApiOperation("服务端删除服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务单id", required = true, dataTypeClass = Long.class),
    })
    public void serveDelete(@PathVariable("id") Long id) {
        ordersServeManagerService.deleteServe(id, UserContext.currentUserId(), UserContext.currentUser().getUserType());
    }

    @PostMapping("/start")
    @ApiOperation("服务端开始服务")
    public void serveStart(@Validated @RequestBody ServeStartReqDTO serveStartReqDTO) {
        ordersServeManagerService.serveStart(serveStartReqDTO, UserContext.currentUserId());
    }

    @PostMapping("/finish")
    @ApiOperation("服务端完成服务")
    public void serveFinish(@Validated @RequestBody ServeFinishedReqDTO serveFinishedReqDTO) {
        ordersServeManagerService.serveFinished(serveFinishedReqDTO, UserContext.currentUserId(), UserContext.currentUser().getUserType());
    }

    @PostMapping("/cancel")
    @ApiOperation("取消服务")
    public void cancel(@RequestBody @Validated OrderServeCancelReqDTO orderServeCancelReqDTO) {
        ordersServeManagerService.cancelByProvider(orderServeCancelReqDTO, UserContext.currentUserId());
    }
    @GetMapping("/{id}")
    @ApiOperation("获取服务单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务单id", required = true, dataTypeClass = Long.class),
    })
    public OrdersServeDetailResDTO getDetail(@PathVariable("id") Long id) {
        return ordersServeManagerService.getDetail(id, UserContext.currentUserId());
    }

    @GetMapping("/status/num")
    @ApiOperation("服务端服务单状态数量,只统计待分配、待服务、待完成三种状态的订单")
    public OrdersServeStatusNumResDTO countServeStatusNum() {
        return ordersServeManagerService.countServeStatusNum(UserContext.currentUserId());
    }
}
