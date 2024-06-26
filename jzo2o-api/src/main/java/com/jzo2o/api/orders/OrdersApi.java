package com.jzo2o.api.orders;

import com.jzo2o.api.orders.dto.response.OrderResDTO;
import com.jzo2o.utils.MyQueryMapEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 内部接口 - 订单相关接口
 *
 * @author itcast
 */
@FeignClient(contextId = "jzo2o-orders-manager", value = "jzo2o-orders-manager", path = "/orders-manager/inner/orders", configuration = MyQueryMapEncoder.class)
public interface OrdersApi {


    /**
     * 根据id查询订单详情
     *
     * @param id 订单id
     * @return 订单详情
     */
    @GetMapping("/{id}")
    OrderResDTO queryById(@PathVariable("id") Long id);

    /**
     * 根据订单id列表批量查询
     *
     * @param ids 订单id列表
     * @return 订单列表
     */
    @GetMapping("queryByIds")
    List<OrderResDTO> queryByIds(@RequestParam("ids") List<Long> ids);

    /**
     * 根据订单id评价
     *
     * @param id 订单id
     */
    @PutMapping("evaluate/{id}")
    void evaluate(@PathVariable("id") Long id);
}
