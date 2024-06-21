package com.jzo2o.orders.manager.controller.agency;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.orders.manager.model.dto.request.*;
import com.jzo2o.orders.manager.model.dto.response.OrdersServeDetailResDTO;
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

@RestController("orders-agency")
@Api(tags = "机构端-服务单相关接口")
@RequestMapping("/agency")
public class AgencyOrdersServeController {

    @Resource
    private IOrdersServeManagerService ordersServeManagerService;

    @GetMapping("/queryForPage")
    @ApiOperation("服务单分页列表，提供给机构端")
    public PageResult<OrdersServeResDTO> queryForPage(OrdersServePageQueryReqDTO ordersServePageQueryReqDTO) {
        return ordersServeManagerService.queryForPage(UserContext.currentUserId(), UserContext.currentUser().getUserType(), ordersServePageQueryReqDTO);
    }

    @PostMapping("/allocation")
    @ApiOperation("人员分配")
    public void allocation(@Validated @RequestBody OrdersServeAllocationReqDTO ordersServeAllocationReqDTO) {
        ordersServeManagerService.allocation(ordersServeAllocationReqDTO.getId(), UserContext.currentUserId(), ordersServeAllocationReqDTO.getInstitutionStaffId());
    }

    @DeleteMapping("/serve/{id}")
    @ApiOperation("机构端删除服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务单id", required = true, dataTypeClass = Long.class),
    })
    public void serveDelete(@PathVariable("id") Long id) {
        ordersServeManagerService.deleteServe(id, UserContext.currentUserId(), UserContext.currentUser().getUserType());
    }

    @PostMapping("/start")
    @ApiOperation("机构端开始服务")
    public void serveStart(@Validated @RequestBody ServeStartReqDTO serveStartReqDTO) {
        ordersServeManagerService.serveStart(serveStartReqDTO, UserContext.currentUserId());
    }

    @PostMapping("/finish")
    @ApiOperation("机构端完成服务")
    public void serveFinish(@Validated @RequestBody ServeFinishedReqDTO serveFinishedReqDTO) {
        ordersServeManagerService.serveFinished(serveFinishedReqDTO, UserContext.currentUserId(), UserContext.currentUser().getUserType());
    }

    @PostMapping("/cancel")
    @ApiOperation("机构端取消服务")
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
    @ApiOperation("用于机构端服务单状态数量,只统计待分配、待服务、待完成三种状态的订单")
    public OrdersServeStatusNumResDTO countServeStatusNum() {
        return ordersServeManagerService.countServeStatusNum(UserContext.currentUserId());
    }
}
