package com.jzo2o.publics.service;

import com.jzo2o.common.enums.SmsBussinessTypeEnum;
import com.jzo2o.publics.model.dto.request.SmsCodeSendReqDTO;

public interface ISmsCodeService {

    /**
     * 发送短信验证码
     * @param smsCodeSendReqDTO
     */
    void smsCodeSend(SmsCodeSendReqDTO smsCodeSendReqDTO);

    /**
     * 校验短信验证码
     * @param phone 验证手机号
     * @param bussinessType 业务类型
     * @return 验证结果
     */
    boolean verify(String phone, SmsBussinessTypeEnum bussinessType, String verifyCode);
}
