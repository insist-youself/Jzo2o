package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.api.publics.MapApi;
import com.jzo2o.api.publics.dto.response.LocationResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.NumberUtils;
import com.jzo2o.common.utils.StringUtils;
import com.jzo2o.customer.mapper.AddressBookMapper;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.jzo2o.customer.service.IAddressBookService;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.mysql.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 地址薄 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-07-06
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {
    @Resource
    private AddressBookMapper addressBookMapper;
    @Resource
    private MapApi mapApi;

    /**
     * 地址薄新增
     *
     * @param addressBookUpsertReqDTO 插入更新地址薄
     */
    @Override
    public void add(AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        //当前用户id
        Long userId = UserContext.currentUserId();
        //如果新增地址设为默认，取消其他默认地址
        if (1 == addressBookUpsertReqDTO.getIsDefault()) {
            cancelDefault(userId);
        }

        AddressBook addressBook = BeanUtil.toBean(addressBookUpsertReqDTO, AddressBook.class);
        addressBook.setUserId(userId);

        //组装详细地址
        String completeAddress = addressBookUpsertReqDTO.getProvince() +
                addressBookUpsertReqDTO.getCity() +
                addressBookUpsertReqDTO.getCounty() +
                addressBookUpsertReqDTO.getAddress();

        //如果请求体中没有经纬度，需要调用第三方api根据详细地址获取经纬度
        if(ObjectUtil.isEmpty(addressBookUpsertReqDTO.getLocation())){
            //远程请求高德获取经纬度
            LocationResDTO locationDto = mapApi.getLocationByAddress(completeAddress);
            //经纬度(字符串格式：经度,纬度),经度在前，纬度在后
            String location = locationDto.getLocation();
            addressBookUpsertReqDTO.setLocation(location);
        }

        if(StringUtils.isNotEmpty(addressBookUpsertReqDTO.getLocation())) {
            // 经度
            addressBook.setLon(NumberUtils.parseDouble(addressBookUpsertReqDTO.getLocation().split(",")[0]));
            // 纬度
            addressBook.setLat(NumberUtils.parseDouble(addressBookUpsertReqDTO.getLocation().split(",")[1]));
        }
        addressBookMapper.insert(addressBook);
    }

    /**
     * 地址薄修改
     *
     * @param id                      地址薄id
     * @param addressBookUpsertReqDTO 插入更新地址薄
     */
    @Override
    @Transactional
    public void update(Long id, AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        if (1 == addressBookUpsertReqDTO.getIsDefault()) {
            cancelDefault(UserContext.currentUserId());
        }

        AddressBook addressBook = BeanUtil.toBean(addressBookUpsertReqDTO, AddressBook.class);
        addressBook.setId(id);

        //调用第三方，根据地址获取经纬度坐标
        String completeAddress = addressBookUpsertReqDTO.getProvince() +
                addressBookUpsertReqDTO.getCity() +
                addressBookUpsertReqDTO.getCounty() +
                addressBookUpsertReqDTO.getAddress();
        //远程请求高德获取经纬度
        LocationResDTO locationDto = mapApi.getLocationByAddress(completeAddress);
        //经纬度(字符串格式：经度,纬度),经度在前，纬度在后
        String location = locationDto.getLocation();
        if(StringUtils.isNotEmpty(location)) {
            // 经度
            addressBook.setLon(NumberUtils.parseDouble(locationDto.getLocation().split(",")[0]));
            // 纬度
            addressBook.setLat(NumberUtils.parseDouble(locationDto.getLocation().split(",")[1]));
        }
        addressBookMapper.updateById(addressBook);
    }

    /**
     * 取消默认
     *
     * @param userId 用户id
     */
    private void cancelDefault(Long userId) {
        LambdaUpdateWrapper<AddressBook> updateWrapper = Wrappers.<AddressBook>lambdaUpdate()
                .eq(AddressBook::getUserId, userId)
                .set(AddressBook::getIsDefault, 0);
        super.update(updateWrapper);
    }

    /**
     * 地址薄设为默认/取消默认
     *
     * @param userId 用户id
     * @param id   地址薄id
     * @param flag 是否为默认地址，0：否，1：是
     */
    @Override
    public void updateDefaultStatus(Long userId,Long id, Integer flag) {
        if (1 == flag) {
            //如果设默认地址，先把其他地址取消默认
            cancelDefault(userId);
        }

        AddressBook addressBook = new AddressBook();
        addressBook.setId(id);
        addressBook.setIsDefault(flag);
        addressBookMapper.updateById(addressBook);
    }

    /**
     * 分页查询
     *
     * @param addressBookPageQueryReqDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<AddressBookResDTO> page(AddressBookPageQueryReqDTO addressBookPageQueryReqDTO) {
        Page<AddressBook> page = PageUtils.parsePageQuery(addressBookPageQueryReqDTO, AddressBook.class);

        LambdaQueryWrapper<AddressBook> queryWrapper = Wrappers.<AddressBook>lambdaQuery().eq(AddressBook::getUserId, UserContext.currentUserId());
        Page<AddressBook> serveTypePage = addressBookMapper.selectPage(page, queryWrapper);
        return PageUtils.toPage(serveTypePage, AddressBookResDTO.class);
    }

    /**
     * 获取默认地址
     *
     * @return 默认地址
     */
    @Override
    public AddressBookResDTO defaultAddress() {
        LambdaQueryWrapper<AddressBook> queryWrapper = Wrappers.<AddressBook>lambdaQuery()
                .eq(AddressBook::getUserId, UserContext.currentUserId())
                .eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookMapper.selectOne(queryWrapper);
        return BeanUtil.toBean(addressBook, AddressBookResDTO.class);
    }

    @Override
    public List<AddressBookResDTO> getByUserIdAndCity(Long userId, String city) {

        List<AddressBook> addressBooks = lambdaQuery()
                .eq(AddressBook::getUserId, userId)
                .eq(AddressBook::getCity, city)
                .list();
        if(CollUtils.isEmpty(addressBooks)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyToList(addressBooks, AddressBookResDTO.class);
    }
}
