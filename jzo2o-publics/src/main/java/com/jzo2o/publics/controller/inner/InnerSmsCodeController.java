package com.jzo2o.publics.controller.inner;

import com.jzo2o.api.publics.SmsCodeApi;
import com.jzo2o.api.publics.dto.response.BooleanResDTO;
import com.jzo2o.common.enums.SmsBussinessTypeEnum;
import com.jzo2o.publics.service.ISmsCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 验证码
 *
 * @author itcast
 * @create 2023/9/24 09:48
 **/
@RestController
@RequestMapping("/inner/sms-code")
@Api(tags = "内部接口 - 验证码相关接口")
public class InnerSmsCodeController implements SmsCodeApi {
    @Resource
    private ISmsCodeService smsCodeService;

    @Override
    @GetMapping("/verify")
    @ApiOperation("校验短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "验证手机号", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "bussinessType", value = "业务类型", required = true, dataTypeClass = SmsBussinessTypeEnum.class),
            @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true, dataTypeClass = String.class)
    })
    public BooleanResDTO verify(@RequestParam("phone") String phone,
                                @RequestParam("bussinessType") SmsBussinessTypeEnum bussinessType,
                                @RequestParam("verifyCode") String verifyCode) {
        return new BooleanResDTO(smsCodeService.verify(phone, bussinessType, verifyCode));
    }
}
