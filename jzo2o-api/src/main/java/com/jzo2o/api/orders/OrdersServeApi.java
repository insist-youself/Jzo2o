package com.jzo2o.api.orders;

import com.jzo2o.api.orders.dto.response.InstitutionStaffServeCountResDTO;
import com.jzo2o.api.orders.dto.response.ServeProviderIdResDTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(contextId = "jzo2o-orders-manager", value = "jzo2o-orders-manager", path = "/orders-manager/inner/orders-serve")
public interface OrdersServeApi {

    /**
     * 根据订单id查询服务人员/机构id
     *
     * @param id 订单id
     * @return 服务人员/机构id
     */
    @GetMapping("/queryServeProviderIdByOrderId/{id}")
    ServeProviderIdResDTO queryServeProviderIdByOrderId(@PathVariable("id") Long id);

    /**
     * 根据机构服务人员id查询服务数量
     *
     * @param institutionStaffId 机构服务人员id
     * @return 服务数量
     */
    @GetMapping("/countByInstitutionStaffId")
    InstitutionStaffServeCountResDTO countByInstitutionStaffId(@RequestParam("institutionStaffId") Long institutionStaffId);
}
