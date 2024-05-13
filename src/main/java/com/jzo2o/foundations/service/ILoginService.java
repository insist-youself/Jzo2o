package com.jzo2o.foundations.service;

import com.jzo2o.foundations.model.dto.request.LoginReqDTO;

/**
 * 登录相关业务
 * @author itcast
 */
public interface ILoginService {
    /**
     * 运营员登录
     *
     * @param loginReqDTO 运营人员登录请求模型
     * @return token
     */
    String login(LoginReqDTO loginReqDTO);
}
