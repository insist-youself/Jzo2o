package com.jzo2o.api.publics;

import com.jzo2o.api.publics.dto.response.BooleanResDTO;
import com.jzo2o.common.enums.SmsBussinessTypeEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author itcast
 */
@FeignClient(contextId = "jzo2o-publics", value = "jzo2o-publics", path = "/publics/inner/sms-code")
public interface SmsCodeApi {

    /**
     * 校验短信验证码
     *
     * @param phone         验证手机号
     * @param bussinessType 业务类型
     * @param verifyCode    验证码
     * @return 验证结果
     */
    @GetMapping("/verify")
    BooleanResDTO verify(@RequestParam("phone") String phone,
                         @RequestParam("bussinessType") SmsBussinessTypeEnum bussinessType,
                         @RequestParam("verifyCode") String verifyCode);
}
