package com.jzo2o.publics.controller.outer;

import com.jzo2o.publics.model.dto.request.SmsCodeSendReqDTO;
import com.jzo2o.publics.service.ISmsCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 验证码
 *
 * @author itcast
 * @create 2023/9/24 09:48
 **/
@RestController
@RequestMapping("/sms-code")
@Api(tags = "验证码相关接口")
public class SmsCodeController {
    @Resource
    private ISmsCodeService smsCodeService;

    @PostMapping("/send")
    @ApiOperation("发送短信验证码")
    public void smsCodeSend(@RequestBody SmsCodeSendReqDTO smsCodeSendReqDTO) {
        smsCodeService.smsCodeSend(smsCodeSendReqDTO);
    }
}
