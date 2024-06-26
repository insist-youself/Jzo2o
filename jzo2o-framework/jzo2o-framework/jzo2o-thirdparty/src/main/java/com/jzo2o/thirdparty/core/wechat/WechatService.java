package com.jzo2o.thirdparty.core.wechat;


/**
 * @author itcast
 */
public interface WechatService {
    /**
     * 获取openid
     *
     * @param code 登录凭证
     * @return 唯一标识
     */
    String getOpenid(String code);

    /**
     * 获取手机号
     *
     * @param code 手机号凭证
     * @return 唯一标识
     */
    String getPhone(String code);
}
