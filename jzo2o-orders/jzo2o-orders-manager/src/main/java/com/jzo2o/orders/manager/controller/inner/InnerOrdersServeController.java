package com.jzo2o.orders.manager.controller.inner;

import com.jzo2o.api.orders.OrdersServeApi;
//import com.jzo2o.api.orders.dto.response.InstitutionStaffServeCountResDTO;
import com.jzo2o.api.orders.dto.response.InstitutionStaffServeCountResDTO;
import com.jzo2o.api.orders.dto.response.ServeProviderIdResDTO;
import com.jzo2o.orders.base.model.domain.OrdersServe;
import com.jzo2o.orders.manager.service.IOrdersServeManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "内部接口 - 服务单相关接口")
@RequestMapping("/inner/orders-serve")
public class InnerOrdersServeController implements OrdersServeApi {

    @Resource
    private IOrdersServeManagerService ordersServeManagerService;

    @Override
    @GetMapping("/queryServeProviderIdByOrderId/{id}")
    @ApiOperation("根据订单id查询服务人员/机构id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataTypeClass = Long.class),
    })
    public ServeProviderIdResDTO queryServeProviderIdByOrderId(@PathVariable("id") Long id) {
        OrdersServe ordersServe = ordersServeManagerService.queryById(id);
        return new ServeProviderIdResDTO(ordersServe.getServeProviderId());
    }

    @Override
    @GetMapping("/countByInstitutionStaffId")
    @ApiOperation("根据机构服务人员查询服务数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "institutionStaffId", value = "机构服务人员id", required = true, dataTypeClass = Long.class),
    })
    public InstitutionStaffServeCountResDTO countByInstitutionStaffId(@RequestParam("institutionStaffId") Long institutionStaffId) {
        return ordersServeManagerService.countByInstitutionStaffId(institutionStaffId);
    }
}
