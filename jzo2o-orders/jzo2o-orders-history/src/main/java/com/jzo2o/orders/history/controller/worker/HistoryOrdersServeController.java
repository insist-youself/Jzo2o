package com.jzo2o.orders.history.controller.worker;

import com.jzo2o.orders.history.model.dto.request.HistoryOrdersServeListQueryReqDTO;
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
import java.util.List;

@RestController("workerHistoryOrdersServeController")
@RequestMapping("/worker/orders")
@Api(tags = "服务端-历史订单相关接口")
public class HistoryOrdersServeController {

    @Resource
    private IHistoryOrdersServeService historyOrdersServeService;

    @ApiOperation("列表查询服务订单")
    @GetMapping("/list")
    public List<HistoryOrdersServeResDTO> queryForList(HistoryOrdersServeListQueryReqDTO historyOrdersServeListQueryReqDTO) {
        return historyOrdersServeService.queryForList(historyOrdersServeListQueryReqDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取订单详情")
    @ApiImplicitParam(name = "id", value = "服务单id", required = true, dataTypeClass = Long.class)
    public HistoryOrdersServeDetailResDTO getDetail(@PathVariable(value = "id",required = false) Long id) {
        return historyOrdersServeService.queryDetailById(id);
    }
}
