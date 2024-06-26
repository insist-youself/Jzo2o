package com.jzo2o.orders.manager.controller.inner;

import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.api.orders.OrdersApi;
import com.jzo2o.api.orders.dto.response.OrderResDTO;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.manager.service.IOrdersManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author itcast
 */
@RestController
@Api(tags = "内部接口 - 订单相关接口")
@RequestMapping("/inner/orders")
public class InnerOrdersController implements OrdersApi {

    @Resource
    private IOrdersManagerService ordersManagerService;


    @Override
    @GetMapping("/{id}")
    @ApiOperation("根据订单id查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataTypeClass = Long.class),
    })
    public OrderResDTO queryById(@PathVariable("id") Long id) {
        Orders orders = ordersManagerService.queryById(id);
        return BeanUtil.toBean(orders, OrderResDTO.class);
    }

    @Override
    @GetMapping("queryByIds")
    @ApiOperation("根据订单id列表批量查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "订单id列表", required = true, dataTypeClass = Long.class),
    })
    public List<OrderResDTO> queryByIds(@RequestParam("ids") List<Long> ids) {
        List<Orders> ordersList = ordersManagerService.batchQuery(ids);
        return BeanUtil.copyToList(ordersList, OrderResDTO.class);
    }

    @Override
    @PutMapping("evaluate/{id}")
    @ApiOperation("根据订单id评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataTypeClass = Long.class),
    })
    public void evaluate(@PathVariable("id") Long id) {
        ordersManagerService.evaluationOrder(id);
    }
}
