package com.jzo2o.publics.controller.inner;

import com.jzo2o.api.publics.WechatApi;
import com.jzo2o.api.publics.dto.response.OpenIdResDTO;
import com.jzo2o.api.publics.dto.response.PhoneResDTO;
import com.jzo2o.thirdparty.core.wechat.WechatService;
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
 * 微信服务
 *
 * @author itcast
 * @create 2023/8/23 19:31
 **/
@RestController
@RequestMapping("/inner/wechat")
@Api(tags = "内部接口 - 微信服务相关接口")
public class InnerWechatController implements WechatApi {

    @Resource
    private WechatService wechatService;

    @Override
    @GetMapping("/getOpenId")
    @ApiOperation("获取openId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "登录凭证", required = true, dataTypeClass = String.class)
    })
    public OpenIdResDTO getOpenId(@RequestParam("code") String code) {
        String openId = wechatService.getOpenid(code);
        return new OpenIdResDTO(openId);
    }

    @Override
    @GetMapping("/getPhone")
    @ApiOperation("获取手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "手机号凭证", required = true, dataTypeClass = String.class)
    })
    public PhoneResDTO getPhone(@RequestParam("code") String code) {
        String phone = wechatService.getPhone(code);
        return new PhoneResDTO(phone);
    }
}
