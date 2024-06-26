package com.jzo2o.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;

import java.util.List;

/**
 * <p>
 * 地址薄 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-07-06
 */
public interface IAddressBookService extends IService<AddressBook> {

    /**
     * 根据用户id和城市编码获取地址
     *
     * @param userId 用户id
     * @param cityCode 城市编码
     * @return 地址编码
     */
    List<AddressBookResDTO> getByUserIdAndCity(Long userId, String cityCode);

    /**
     * 新增地址簿接口
     *
     * @param addressBookUpsertReqDTO
     * @return
     */
    AddressBook add(AddressBookUpsertReqDTO addressBookUpsertReqDTO);


    /**
     * 查询地址薄详情
     *
     * @param id
     * @return
     */
    AddressBookResDTO findById(Long id);

    /**
     * 地址薄分页查询
     * @return
     */
    PageResult<AddressBookResDTO> page(AddressBookPageQueryReqDTO addressBookPageQueryReqDTO);

    /**
     * 地址薄修改
     * @param id
     * @param addressBookUpsertReqDTO
     */
    void updateById(Long id, AddressBookUpsertReqDTO addressBookUpsertReqDTO);


    /**
     *  地址簿批量删除
     * @param ids
     */
    void batchDelete(List<Long> ids);


    /**
     * 设置/取消默认地址
     * @param flag
     * @param id
     */
    void putDefault(Integer flag, Long id);

    /**
     * 获取默认地址
     * @return
     */
    AddressBookResDTO defaultAddress();
}
