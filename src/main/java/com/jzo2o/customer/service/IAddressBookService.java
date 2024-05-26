package com.jzo2o.customer.service;

import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * 地址薄新增
     *
     * @param addressBookUpsertReqDTO 插入更新地址薄
     */
    void add(AddressBookUpsertReqDTO addressBookUpsertReqDTO);

    /**
     * 地址薄修改
     *
     * @param id                      地址薄id
     * @param addressBookUpsertReqDTO 插入更新地址薄
     */
    void update(Long id, AddressBookUpsertReqDTO addressBookUpsertReqDTO);

    /**
     * 地址薄设为默认/取消默认
     *
     * @param userId   用户id
     * @param id   地址薄id
     * @param flag 是否为默认地址，0：否，1：是
     */
    void updateDefaultStatus(Long userId,Long id, Integer flag);

    /**
     * 分页查询
     *
     * @param addressBookPageQueryReqDTO 查询条件
     * @return 分页结果
     */
    PageResult<AddressBookResDTO> page(AddressBookPageQueryReqDTO addressBookPageQueryReqDTO);

    /**
     * 获取默认地址
     *
     * @return 默认地址
     */
    AddressBookResDTO defaultAddress();

    /**
     * 根据用户id和城市编码获取地址
     *
     * @param userId 用户id
     * @param cityCode 城市编码
     * @return 地址编码
     */
    List<AddressBookResDTO> getByUserIdAndCity(Long userId, String cityCode);
}
