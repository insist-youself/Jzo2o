package com.jzo2o.api.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 内部接口 - 服务技能相关接口
 *
 * @author itcast
 */
@FeignClient(contextId = "jzo2o-customer", value = "jzo2o-customer", path = "/customer/inner/serve-skill")
public interface ServeSkillApi {

    @GetMapping("/queryServeSkillListByServeProvider")
    List<Long> queryServeSkillListByServeProvider(@RequestParam("providerId") Long providerId,
                             @RequestParam("providerType") Integer providerType,
                             @RequestParam("cityCode") String cityCode);
}
