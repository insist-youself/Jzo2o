package com.jzo2o.orders.history.controller.agency;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.orders.history.model.dto.request.HistoryOrdersServePageQueryReqDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersServeDetailResDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersServeResDTO;
import com.jzo2o.orders.history.service.IHistoryOrdersServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("agencyHistoryOrdersServeController")
@RequestMapping("/agency/orders")
@Api(tags = "机构端-历史订单相关接口")
public class HistoryOrdersServeController {

    @Resource
    private IHistoryOrdersServeService historyOrdersServeService;

    @GetMapping("/page")
    @ApiOperation("分页查询机构端历史订单")
    public PageResult<HistoryOrdersServeResDTO> queryForPage(HistoryOrdersServePageQueryReqDTO historyOrdersServePageQueryReqDTO) {
        return historyOrdersServeService.queryForPage(historyOrdersServePageQueryReqDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取订单详情")
    @ApiImplicitParam(name = "id", value = "服务单id", required = true, dataTypeClass = Long.class)
    public HistoryOrdersServeDetailResDTO getDetail(@PathVariable(value = "id",required = false) Long id) {
        return historyOrdersServeService.queryDetailById(id);
    }

}
