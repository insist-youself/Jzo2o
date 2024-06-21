package com.jzo2o.orders.manager.controller.operation;

import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.api.orders.dto.request.OrderCancelReqDTO;
import com.jzo2o.api.orders.dto.response.OrderSimpleResDTO;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.orders.manager.model.dto.request.OrderPageQueryReqDTO;
import com.jzo2o.orders.manager.model.dto.OrderCancelDTO;
import com.jzo2o.orders.manager.model.dto.response.OperationOrdersDetailResDTO;
import com.jzo2o.orders.manager.service.IOrdersManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author itcast
 */
@RestController("operationOrdersController")
@Api(tags = "运营端-订单相关接口")
@RequestMapping("/operation/orders")
public class OperationOrdersController {
    @Resource
    private IOrdersManagerService ordersManagerService;

    @GetMapping("/page")
    @ApiOperation("订单分页查询")
    public PageResult<OrderSimpleResDTO> page(OrderPageQueryReqDTO orderPageQueryReqDTO) {
        return ordersManagerService.operationPageQuery(orderPageQueryReqDTO);
    }

    @GetMapping("/aggregation/{id}")
    @ApiOperation("订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataTypeClass = Long.class)
    })
    public OperationOrdersDetailResDTO aggregation(@PathVariable("id") Long id) {
        return ordersManagerService.aggregation(id);
    }

    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public void cancel(@RequestBody OrderCancelReqDTO orderCancelReqDTO) {
        OrderCancelDTO orderCancelDTO = BeanUtil.toBean(orderCancelReqDTO, OrderCancelDTO.class);
        CurrentUserInfo currentUserInfo = UserContext.currentUser();
        orderCancelDTO.setCurrentUserId(currentUserInfo.getId());
        orderCancelDTO.setCurrentUserName(currentUserInfo.getName());
        orderCancelDTO.setCurrentUserType(currentUserInfo.getUserType());
        ordersManagerService.cancel(orderCancelDTO);
    }
}
