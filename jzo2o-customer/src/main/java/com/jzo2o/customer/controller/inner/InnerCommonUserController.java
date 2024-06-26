package com.jzo2o.customer.controller.inner;

import com.jzo2o.api.customer.CommonUserApi;
import com.jzo2o.api.customer.dto.response.CommonUserResDTO;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.customer.service.ICommonUserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 内部接口-普通用户相关接口
 *
 * @author itcast
 * @create 2023/7/7 19:34
 **/
@RestController
@RequestMapping("inner/common-user")
@Api(tags = "内部接口 - 普通用户相关接口")
public class InnerCommonUserController implements CommonUserApi {
    @Resource
    private ICommonUserService commonUserService;

    @Override
    public CommonUserResDTO findById(Long id) {
        return BeanUtils.toBean(commonUserService.getById(id), CommonUserResDTO.class);
    }
}
