package com.jzo2o.customer.controller.consumer;

import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.jzo2o.customer.service.IAddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author linger
 * @date 2024/5/17 17:14
 */
@RestController("consumerAddressBookControllor")
@RequestMapping("/consumer/address-book")
@Api(tags = "用户端 - 地址簿相关接口")
public class AddressBookControllor {

    @Resource
    private IAddressBookService addressBookService;

    @PostMapping
    @ApiOperation("新增地址簿")
    public void add(@RequestBody AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        addressBookService.add(addressBookUpsertReqDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation("地址薄详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地址簿id", required = true, dataTypeClass = Long.class)
    })
    public AddressBookResDTO findById(@PathVariable("id") Long id) {
        return addressBookService.findById(id);
    }


    @GetMapping("/page")
    @ApiOperation("地址薄分页查询")
    public PageResult<AddressBookResDTO> page(AddressBookPageQueryReqDTO addressBookPageQueryReqDTO) {
       return addressBookService.page(addressBookPageQueryReqDTO);
    }


    @PutMapping("/{id}")
    @ApiOperation("地址薄修改")
    public void updateById(@PathVariable("id") Long id, @RequestBody AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        addressBookService.updateById(id,addressBookUpsertReqDTO);
    }

    @DeleteMapping("/batch")
    @ApiOperation("地址簿批量删除")
    public void batchDelete(@RequestBody List<Long> ids) {
        addressBookService.batchDelete(ids);
    }

    @PutMapping("/default")
    @ApiOperation("设置/取消默认地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flag", value = "是否为默认地址，0：否，1：是", required = true, dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "id", value = "地址簿id", required = true, dataTypeClass = Long.class)
    })
    public void putDefault(@RequestParam("id") Long id, @RequestParam("flag") Integer flag) {
        addressBookService.putDefault(flag, id);
    }


    @GetMapping("/defaultAddress")
    @ApiOperation("获取默认地址")
    public AddressBookResDTO defaultAddress() {
        return addressBookService.defaultAddress();
    }
}
