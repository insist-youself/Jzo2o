package com.jzo2o.api.customer;

import com.jzo2o.api.customer.dto.response.ServeProviderResDTO;
import com.jzo2o.api.customer.dto.response.ServeProviderSimpleResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 内部接口 - 服务人员/机构相关接口
 *
 * @author itcast
 */
@FeignClient(contextId = "jzo2o-customer", value = "jzo2o-customer", path = "/customer/inner/serve-provider", qualifiers = "customerServeProviderApi")
public interface ServeProviderApi {

    @GetMapping("/{id}")
    ServeProviderResDTO getDetail(@PathVariable("id") Long id);

    /**
     * 批量获取服务人员/机构所在城市编码
     * @param ids
     * @return
     */
    @GetMapping("/batchCityCode")
    Map<Long, String> batchCityCode(@RequestParam("ids") List<Long> ids);

    /**
     * 根据服务人员id或机构id获取服务人员或机构的类型
     * @param ids 服务人员或机构id列表
     * @return 服务人员或机构id和对应的服务人员或机构类型
     */
    @GetMapping("/batchGetProviderType")
    Map<Long, Integer> batchGetProviderType(@RequestParam("ids") List<Long> ids);

    /**
     * 批量获取服务人员/机构信息
     * @param ids id列表
     * @return 批量获取服务
     */
    @GetMapping("/batchGet")
    List<ServeProviderSimpleResDTO> batchGet(@RequestParam("ids") List<Long> ids);
}
