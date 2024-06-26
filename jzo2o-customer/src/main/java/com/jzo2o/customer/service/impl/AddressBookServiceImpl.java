package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.api.publics.MapApi;
import com.jzo2o.api.publics.dto.response.LocationResDTO;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.*;
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

    @Resource
    private MapApi mapApi;

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

    /**
     * 地址薄新增
     *
     * @param addressBookUpsertReqDTO 插入更新地址薄
     */
    @Override
    @Transactional
    public AddressBook add(AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        Long userId = UserContext.currentUserId();
        if(ObjectUtils.isNull(addressBookUpsertReqDTO)) {
            throw new ForbiddenOperationException("新增地址簿不能为 null");
        }
        // 1. 如果该地址簿设置为默认地址，则该用户其他地址要设为非默认
        if (1 == addressBookUpsertReqDTO.getIsDefault()) {
            cancelDefault(userId);
        }
        AddressBook addressBook = BeanUtil
                .copyProperties(addressBookUpsertReqDTO, AddressBook.class);
        addressBook.setUserId(userId);
        // 2. 拼装具体地址
        String completeAddress = addressBookUpsertReqDTO.getProvince()
                + addressBookUpsertReqDTO.getCity()
                + addressBookUpsertReqDTO.getCounty()
                + addressBookUpsertReqDTO.getAddress();
        // 3. 如果传入参数没有经纬度，则调用高德地图API接口去获取
        if(ObjectUtils.isEmpty(addressBookUpsertReqDTO.getLocation())) {
            LocationResDTO locationByAddress = mapApi.getLocationByAddress(completeAddress);
            String location = locationByAddress.getLocation();
            addressBookUpsertReqDTO.setLocation(location);
        }
        if(StringUtils.isNotEmpty(addressBookUpsertReqDTO.getLocation())) {
            // 经度
            addressBook.setLon(NumberUtils.parseDouble(addressBookUpsertReqDTO.getLocation().split(",")[0]));
            // 纬度
            addressBook.setLat(NumberUtils.parseDouble(addressBookUpsertReqDTO.getLocation().split(",")[1]));
        }
        int insert = baseMapper.insert(addressBook);
        return baseMapper.selectById(insert);
    }

    private void cancelDefault(Long userId) {
        boolean update = lambdaUpdate()
                .eq(AddressBook::getUserId, userId)
                .set(AddressBook::getIsDefault, 0)
                .update();
        if(!update) {
            throw new CommonException("更改其他地址为非默认地址失败");
        }
    }

    @Override
    public AddressBookResDTO findById(Long id) {
        // 1. 查询对应地址簿是否存在
        AddressBook addressBook = baseMapper.selectById(id);
        if(ObjectUtils.isNull(addressBook)) {
            throw new ForbiddenOperationException("对应地址簿不存在");
        }
        // 2，类型转换操作
        AddressBookResDTO addressBookResDTO = BeanUtil.copyProperties(addressBook, AddressBookResDTO.class);
        return addressBookResDTO;
    }

    @Override
    public PageResult<AddressBookResDTO> page(AddressBookPageQueryReqDTO addressBookPageQueryReqDTO) {
        Page<AddressBook> page = PageUtils.parsePageQuery(addressBookPageQueryReqDTO, AddressBook.class);
        LambdaQueryWrapper<AddressBook> queryWrapper = Wrappers.<AddressBook>lambdaQuery()
                .eq(AddressBook::getUserId, UserContext.currentUserId());
        Page<AddressBook> serveTypePage = baseMapper.selectPage(page, queryWrapper);
        return PageUtils.toPage(serveTypePage, AddressBookResDTO.class);
    }

    @Override
    public void updateById(Long id, AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        AddressBook addressBook1 = baseMapper.selectById(id);
        if(ObjectUtils.isNull(addressBookUpsertReqDTO) || ObjectUtils.isNull(addressBook1)) {
            throw new ForbiddenOperationException("请求数据错误");
        }
        AddressBook addressBook = BeanUtil
                .copyProperties(addressBookUpsertReqDTO, AddressBook.class);
        addressBook.setId(id);
        boolean update = updateById(addressBook);
        if(!update) {
            throw new CommonException("更新失败");
        }
    }

    @Override
    public void batchDelete(List<Long> ids) {
        if(CollectionUtil.isEmpty(ids)) {
            throw new ForbiddenOperationException("请求数据为空");
        }
        ids.forEach(id -> {
            AddressBook addressBook = baseMapper.selectById(id);
            if(ObjectUtils.isNull(addressBook)) {
                throw new ForbiddenOperationException("删除数据不存在");
            }
            baseMapper.deleteById(id);
        });
    }

    @Override
    public void putDefault(Integer flag, Long id) {
        AddressBook addressBook1 = baseMapper.selectById(id);
        // 1. 检查当前id的地址簿是否存在
        if(ObjectUtils.isNull(addressBook1)) {
            throw new ForbiddenOperationException("该地址簿不存在");
        }
        Long userId = UserContext.currentUserId();
        // 2. 如果flag为1, 则先设置其他地址为非默认地址
        if(flag == 1) {
            cancelDefault(userId);
        }
        // 3. 更新对应地址簿的地址
        AddressBook addressBook = new AddressBook();
        addressBook.setId(id);
        addressBook.setIsDefault(flag);
        baseMapper.updateById(addressBook);
    }

    @Override
    public AddressBookResDTO defaultAddress() {
        AddressBook addressBook = lambdaQuery()
                .eq(AddressBook::getUserId,UserContext.currentUserId())
                .eq(AddressBook::getIsDefault, 1)
                .one();
        return BeanUtil.copyProperties(addressBook, AddressBookResDTO.class);
    }
}
