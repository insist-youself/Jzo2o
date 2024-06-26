package com.jzo2o.customer.controller.inner;

import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.service.IAddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author linger
 * @date 2024/5/26 21:24
 */
@RestController
@RequestMapping("inner/address-book")
@Api(tags = "内部接口 - 地址簿相关接口")
public class InnerAddressBookController {

    @Resource
    private IAddressBookService addressBookService;

    @GetMapping("/{id}")
    @ApiOperation("地址薄详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地址薄id", required = true, dataTypeClass = Long.class)
    })
    public AddressBookResDTO detail(@PathVariable("id") Long id){
        AddressBook addressBook = addressBookService.getById(id);
        AddressBookResDTO addressBookResDTO = BeanUtil.copyProperties(addressBook, AddressBookResDTO.class);
        return addressBookResDTO;
    }
}
