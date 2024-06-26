package com.jzo2o.api.publics;

import com.jzo2o.api.publics.dto.response.OpenIdResDTO;
import com.jzo2o.api.publics.dto.response.PhoneResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author itcast
 */
@FeignClient(contextId = "jzo2o-publics", value = "jzo2o-publics", path = "/publics/inner/wechat")
public interface WechatApi {

    @GetMapping("/getOpenId")
    OpenIdResDTO getOpenId(@RequestParam("code") String code);

    @GetMapping("/getPhone")
    PhoneResDTO getPhone(@RequestParam("code") String code);
}
