package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.jzo2o.api.publics.SmsCodeApi;
import com.jzo2o.api.publics.WechatApi;
import com.jzo2o.api.publics.dto.response.OpenIdResDTO;
import com.jzo2o.common.constants.CommonStatusConstants;
import com.jzo2o.common.constants.ErrorInfo;
import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.enums.SmsBussinessTypeEnum;
import com.jzo2o.common.expcetions.BadRequestException;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.utils.JwtTool;
import com.jzo2o.common.utils.StringUtils;
import com.jzo2o.customer.model.domain.CommonUser;
import com.jzo2o.customer.model.domain.ServeProvider;
import com.jzo2o.customer.model.dto.request.LoginForCustomerReqDTO;
import com.jzo2o.customer.model.dto.request.LoginForWorkReqDTO;
import com.jzo2o.customer.model.dto.response.LoginResDTO;
import com.jzo2o.customer.service.ICommonUserService;
import com.jzo2o.customer.service.ILoginService;
import com.jzo2o.customer.service.IServeProviderService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ILoginServiceImpl implements ILoginService {

    @Resource
    private ICommonUserService commonUserService;

    @Resource
    private JwtTool jwtTool;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private IServeProviderService serveProviderService;
    @Resource
    private WechatApi wechatApi;
    @Resource
    private SmsCodeApi smsCodeApi;



    @Override
    public LoginResDTO loginForPassword(LoginForWorkReqDTO loginForWorkReqDTO) {
        // 1.数据校验
        if(StringUtils.isEmpty(loginForWorkReqDTO.getPassword())) {
            throw new BadRequestException("请输入密码");
        }

        // 2.登录校验
        // 2.1.根据手机号和用户类型获取服务人员或机构信息
        ServeProvider serveProvider = serveProviderService.findByPhoneAndType(loginForWorkReqDTO.getPhone(), loginForWorkReqDTO.getUserType());
        // 账号禁用校验
        if(serveProvider != null && CommonStatusConstants.USER_STATUS_FREEZE == serveProvider.getStatus()) {
            throw new CommonException(ErrorInfo.Code.ACCOUNT_FREEZED, serveProvider.getAccountLockReason());
        }
        //密码校验
        if(serveProvider == null || !passwordEncoder.matches(loginForWorkReqDTO.getPassword(), serveProvider.getPassword())){
            throw new BadRequestException("账号或密码错误，请重新输入");
        }


        // 3.生成登录token
        String token = jwtTool.createToken(serveProvider.getId(), serveProvider.getName(), serveProvider.getAvatar(), loginForWorkReqDTO.getUserType());
        return new LoginResDTO(token);

    }

    @Override
    public LoginResDTO loginForVerify(LoginForWorkReqDTO loginForWorkReqDTO) {

        // 数据校验
        if(StringUtils.isEmpty(loginForWorkReqDTO.getVeriryCode())){
            throw new BadRequestException("验证码错误，请重新获取");
        }
        //远程调用publics服务校验验证码是否正确
        boolean verifyResult = smsCodeApi.verify(loginForWorkReqDTO.getPhone(), SmsBussinessTypeEnum.SERVE_STAFF_LOGIN, loginForWorkReqDTO.getVeriryCode()).getIsSuccess();
        if(!verifyResult) {
            throw new BadRequestException("验证码错误，请重新获取");
        }
        // 登录校验
        // 根据手机号和用户类型获取服务人员或机构信息
        ServeProvider serveProvider = serveProviderService.findByPhoneAndType(loginForWorkReqDTO.getPhone(), loginForWorkReqDTO.getUserType());
        // 账号禁用校验
        if(serveProvider != null && CommonStatusConstants.USER_STATUS_FREEZE == serveProvider.getStatus()) {
            throw new CommonException(ErrorInfo.Code.ACCOUNT_FREEZED, serveProvider.getAccountLockReason());
        }
        // 自动注册
        if(serveProvider == null) {
            serveProvider = serveProviderService.add(loginForWorkReqDTO.getPhone(), UserType.WORKER, null);
        }

        // 生成登录token
        String token = jwtTool.createToken(serveProvider.getId(), serveProvider.getName(), serveProvider.getAvatar(), loginForWorkReqDTO.getUserType());
        return new LoginResDTO(token);
    }

    @Override
    public LoginResDTO loginForCommonUser(LoginForCustomerReqDTO loginForCustomerReqDTO) {
        // code换openId
        OpenIdResDTO openIdResDTO = wechatApi.getOpenId(loginForCustomerReqDTO.getCode());
        if(ObjectUtil.isEmpty(openIdResDTO) || ObjectUtil.isEmpty(openIdResDTO.getOpenId())){
            // openid申请失败
            throw new CommonException(ErrorInfo.Code.LOGIN_TIMEOUT, ErrorInfo.Msg.REQUEST_FAILD);
        }
        CommonUser commonUser = commonUserService.findByOpenId(openIdResDTO.getOpenId());
        //如果未从数据库查到，需要新增数据
        if (ObjectUtil.isEmpty(commonUser)) {
            commonUser = BeanUtil.toBean(loginForCustomerReqDTO, CommonUser.class);
            long snowflakeNextId = IdUtil.getSnowflakeNextId();
            commonUser.setId(snowflakeNextId);
            commonUser.setOpenId(openIdResDTO.getOpenId());
            commonUser.setNickname("普通用户"+ RandomUtil.randomInt(10000,99999));
            commonUserService.save(commonUser);
        }else if(CommonStatusConstants.USER_STATUS_FREEZE == commonUser.getStatus()) {
            // 被冻结
            throw new CommonException(ErrorInfo.Code.ACCOUNT_FREEZED, commonUser.getAccountLockReason());
        }

        //构建token
        String token = jwtTool.createToken(commonUser.getId(), commonUser.getNickname(), commonUser.getAvatar(), UserType.C_USER);
        return new LoginResDTO(token);
    }
}
