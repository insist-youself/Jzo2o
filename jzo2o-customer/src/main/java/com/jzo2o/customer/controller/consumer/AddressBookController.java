package com.jzo2o.customer.controller.consumer;


import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.jzo2o.customer.service.IAddressBookService;
import com.jzo2o.mvc.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 地址薄 前端控制器
 * </p>
 *
 * @author itcast
 * @since 2023-07-06
 */
@RestController("consumerAddressBookController")
@RequestMapping("/consumer/address-book")
@Api(tags = "用户端 - 地址薄相关接口")
public class AddressBookController {
    @Resource
    private IAddressBookService addressBookService;

    @PostMapping
    @ApiOperation("地址薄新增")
    public void add(@RequestBody AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        addressBookService.add(addressBookUpsertReqDTO);
    }

    @PutMapping("/{id}")
    @ApiOperation("地址薄修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地址薄id", required = true, dataTypeClass = Long.class)
    })
    public void update(@NotNull(message = "id不能为空") @PathVariable("id") Long id,
                       @RequestBody AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        addressBookService.update(id, addressBookUpsertReqDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation("地址薄详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地址薄id", required = true, dataTypeClass = Long.class)
    })
    public AddressBookResDTO detail(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return BeanUtil.toBean(addressBook, AddressBookResDTO.class);
    }

    @PutMapping("/default")
    @ApiOperation("地址薄设为默认/取消默认")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地址薄id", required = true, dataTypeClass = Long.class),
            @ApiImplicitParam(name = "flag", value = "是否为默认地址，0：否，1：是", required = true, dataTypeClass = Integer.class)
    })
    public void updateDefaultStatus(@NotNull(message = "id不能为空") @RequestParam("id") Long id,
                                    @NotNull(message = "状态值不能为空") @RequestParam("flag") Integer flag) {
        //当前登录用户id
        Long userId = UserContext.currentUserId();
        addressBookService.updateDefaultStatus(userId,id, flag);
    }

    @DeleteMapping("/batch")
    @ApiOperation("地址薄批量删除")
    @ApiImplicitParam(name = "ids", value = "地址薄id列表", required = true, dataTypeClass = List.class)
    public void logicallyDelete(@NotNull(message = "id列表不能为空") @RequestBody List<Long> ids) {
        addressBookService.removeByIds(ids);
    }

    @GetMapping("/page")
    @ApiOperation("地址薄分页查询")
    public PageResult<AddressBookResDTO> page(AddressBookPageQueryReqDTO addressBookPageQueryReqDTO) {
        return addressBookService.page(addressBookPageQueryReqDTO);
    }

    @GetMapping("/defaultAddress")
    @ApiOperation("获取默认地址")
    public AddressBookResDTO defaultAddress() {
        return addressBookService.defaultAddress();
    }
}
