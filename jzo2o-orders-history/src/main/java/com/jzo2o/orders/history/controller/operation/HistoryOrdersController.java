package com.jzo2o.orders.history.controller.operation;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.orders.history.model.dto.request.HistoryOrdersPageQueryReqDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersDetailResDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersPageResDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersServeDetailResDTO;
import com.jzo2o.orders.history.service.IHistoryOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "运营端-历史订单相关接口")
@RestController("operationHistoryOrdersController")
@RequestMapping("/operation/orders")
public class HistoryOrdersController {

    @Resource
    private IHistoryOrdersService historyOrdersService;

    @GetMapping("/page")
    @ApiOperation("运营端分页查询订单列表")
    public PageResult<HistoryOrdersPageResDTO> queryForPage(HistoryOrdersPageQueryReqDTO historyOrdersPageQueryReqDTO) {
        return historyOrdersService.queryForPage(historyOrdersPageQueryReqDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation("查询订单明细")
    @ApiImplicitParam(name = "id", value = "订单id", required = true, dataTypeClass = Long.class)
    public HistoryOrdersDetailResDTO queryById(@PathVariable(name = "id") Long id) {
        return historyOrdersService.getDetailById(id);
    }
}
