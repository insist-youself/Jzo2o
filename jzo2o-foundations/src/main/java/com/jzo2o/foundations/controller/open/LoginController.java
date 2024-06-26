package com.jzo2o.foundations.controller.open;

import com.jzo2o.foundations.model.dto.request.LoginReqDTO;
import com.jzo2o.foundations.model.dto.response.LoginResDTO;
import com.jzo2o.foundations.service.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author itcast
 */
@RestController("openLoginController")
@RequestMapping("/open/login")
@Api(tags = "白名单接口 - 运营人员登录相关接口")
public class LoginController {

    @Resource
    private ILoginService loginService;

    @PostMapping
    @ApiOperation("运营人员登录")
    public LoginResDTO login(@RequestBody LoginReqDTO loginReqDTO) {
        String token = loginService.login(loginReqDTO);
        return new LoginResDTO(token);
    }
}
