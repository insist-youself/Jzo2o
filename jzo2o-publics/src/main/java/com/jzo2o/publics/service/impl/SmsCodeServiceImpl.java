package com.jzo2o.publics.service.impl;

import com.jzo2o.common.constants.CommonRedisConstants;
import com.jzo2o.common.enums.SmsBussinessTypeEnum;
import com.jzo2o.common.utils.StringUtils;
import com.jzo2o.publics.model.dto.request.SmsCodeSendReqDTO;
import com.jzo2o.publics.service.ISmsCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SmsCodeServiceImpl implements ISmsCodeService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void smsCodeSend(SmsCodeSendReqDTO smsCodeSendReqDTO) {
        if(StringUtils.isEmpty(smsCodeSendReqDTO.getPhone()) || StringUtils.isEmpty(smsCodeSendReqDTO.getBussinessType())) {
            log.debug("不能发送短信验证码，phone:{},bussinessType:{}", smsCodeSendReqDTO.getPhone(), smsCodeSendReqDTO.getBussinessType());
            return;
        }
        String redisKey = String.format(CommonRedisConstants.RedisKey.VERIFY_CODE, smsCodeSendReqDTO.getPhone(), smsCodeSendReqDTO.getBussinessType());
        // 取6位随机数
//        String verifyCode = (int)(Math.random() * 1000000) + "";
        String verifyCode = "123456";//为方便测试固定为123456
        log.info("向手机号{}发送验证码{}",smsCodeSendReqDTO.getPhone(),verifyCode);
        //todo调用短信平台接口向指定手机发验证码...
        // 短信验证码有效期5分钟
        redisTemplate.opsForValue().set(redisKey, verifyCode, 300, TimeUnit.SECONDS);
    }

    @Override
    public boolean verify(String phone, SmsBussinessTypeEnum bussinessType, String verifyCode) {
        // 1.验证前准备
        String redisKey = String.format(CommonRedisConstants.RedisKey.VERIFY_CODE, phone, bussinessType.getType());
        String verifyCodeInRedis = redisTemplate.opsForValue().get(redisKey);

        // 2.短验验证，验证通过后删除code，code只能使用一次
        boolean verifyResult = StringUtils.isNotEmpty(verifyCode) && verifyCode.equals(verifyCodeInRedis);
        if(verifyResult) {
            redisTemplate.delete(redisKey);
        }
        return verifyResult;
    }
}
