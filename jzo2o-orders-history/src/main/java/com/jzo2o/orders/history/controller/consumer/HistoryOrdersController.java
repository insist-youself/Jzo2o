package com.jzo2o.orders.history.controller.consumer;

import com.jzo2o.orders.history.model.dto.request.HistoryOrdersListQueryReqDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersDetailResDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersListResDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersPageResDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersServeDetailResDTO;
import com.jzo2o.orders.history.service.IHistoryOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户端-历史订单相关接口")
@RestController("consumerHistoryOrdersController")
@RequestMapping("/consumer/orders")
public class HistoryOrdersController {

    @Resource
    private IHistoryOrdersService historyOrdersService;

    @ApiOperation("滚动式查询订单id列表")
    @GetMapping("/list")
    public List<HistoryOrdersListResDTO> queryForList(HistoryOrdersListQueryReqDTO historyOrdersListQueryReqDTO) {
        return historyOrdersService.queryUserOrderForList(historyOrdersListQueryReqDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation("查询订单明细")
    @ApiImplicitParam(name = "id", value = "订单id", required = true, dataTypeClass = Long.class)
    public HistoryOrdersDetailResDTO queryById(@PathVariable(name = "id") Long id) {
        return historyOrdersService.getDetailById(id);
    }

}
