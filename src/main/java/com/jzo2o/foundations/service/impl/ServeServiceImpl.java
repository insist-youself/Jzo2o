package com.jzo2o.foundations.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.foundations.dto.response.ServeAggregationResDTO;
import com.jzo2o.common.enums.EnableStatusEnum;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.*;
import com.jzo2o.foundations.model.domain.*;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.HomeService;
import com.jzo2o.foundations.service.IServeService;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
@Service
public class ServeServiceImpl extends ServiceImpl<ServeMapper, Serve> implements IServeService {
    @Resource
    private ServeTypeMapper serveTypeMapper;
    @Resource
    private RegionMapper regionMapper;
    @Resource
    private ServeSyncMapper serveSyncMapper;
    @Resource
    private ServeItemMapper serveItemMapper;
    @Resource
    private HomeService homeService;

    /**
     * 批量新增
     * @param serveUpsertReqDTOList 批量新增数据
     */
    @Override
    @Transactional
    public void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList) {
        for (ServeUpsertReqDTO serveUpsertReqDTO : serveUpsertReqDTOList) {
            //1.校验服务项是否为启用状态，不是启用状态不能新增
            ServeItem serveItem = serveItemMapper.selectById(serveUpsertReqDTO.getServeItemId());
            if(!(serveItem.getActiveStatus() == FoundationStatusEnum.ENABLE.getStatus())){
                throw new ForbiddenOperationException("该服务未启用无法添加到区域下使用");
            }

            //2.校验是否重复新增
            LambdaQueryWrapper<Serve> queryWrapper = Wrappers.<Serve>lambdaQuery()
                    .eq(Serve::getRegionId, serveUpsertReqDTO.getRegionId())
                    .eq(Serve::getServeItemId, serveUpsertReqDTO.getServeItemId());
            Integer count = baseMapper.selectCount(queryWrapper);
            if(count>0){
                throw new ForbiddenOperationException("服务存在重复新增");
            }

            //3.新增服务
            Serve serve = BeanUtil.toBean(serveUpsertReqDTO, Serve.class);
            Region region = regionMapper.selectById(serveUpsertReqDTO.getRegionId());
            serve.setCityCode(region.getCityCode());
            baseMapper.insert(serve);
        }
    }

    /**
     * 服务修改
     *
     * @param id    服务id
     * @param price 价格
     * @return 服务
     */
    @Override
    @Transactional
    @CachePut(value = RedisConstants.CacheName.SERVE, key = "#id", unless = "#result.saleStatus != 2", cacheManager = RedisConstants.CacheManager.ONE_DAY)
    public Serve update(Long id, BigDecimal price) {
        //1.更新服务价格
        LambdaUpdateWrapper<Serve> updateWrapper = Wrappers.<Serve>lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getPrice, price);
        super.update(updateWrapper);

        //2.同步数据
        ServeSync serveSync = new ServeSync();
        serveSync.setId(id);
        serveSync.setPrice(price);
        serveSyncMapper.updateById(serveSync);

        //用于缓存更新
        return baseMapper.selectById(id);
    }

    /**
     * 服务设置热门/取消
     *
     * @param id   服务id
     * @param flag 是否为热门，0：非热门，1：热门
     */
    @Override
    @Transactional
    public void changeHotStatus(Long id, Integer flag) {
        //1.设置热门
        LambdaUpdateWrapper<Serve> updateWrapper = Wrappers.<Serve>lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getIsHot, flag)
                .set(Serve::getHotTimeStamp, System.currentTimeMillis());
        super.update(updateWrapper);

        //2.同步数据
        ServeSync serveSync = new ServeSync();
        serveSync.setId(id);
        serveSync.setIsHot(flag);
        serveSync.setHotTimeStamp(System.currentTimeMillis());
        serveSyncMapper.updateById(serveSync);
    }

    /**
     * 根据区域id和售卖状态查询关联服务数量
     *
     * @param regionId   区域id
     * @param saleStatus 售卖状态，0：草稿，1下架，2上架。可传null，即查询所有状态
     * @return 服务数量
     */
    @Override
    public int queryServeCountByRegionIdAndSaleStatus(Long regionId, Integer saleStatus) {
        LambdaQueryWrapper<Serve> queryWrapper = Wrappers.<Serve>lambdaQuery()
                .eq(Serve::getRegionId, regionId)
                .eq(ObjectUtil.isNotEmpty(saleStatus), Serve::getSaleStatus, saleStatus);
        return baseMapper.selectCount(queryWrapper);
    }
    /**
     * 根据服务项id和售卖状态查询关联服务数量
     *
     * @param  serveItemId  服务项id
     * @param saleStatus 售卖状态，0：草稿，1下架，2上架。可传null，即查询所有状态
     * @return 服务数量
     */
    @Override
    public int queryServeCountByServeItemIdAndSaleStatus(Long serveItemId, Integer saleStatus) {
        LambdaQueryWrapper<Serve> queryWrapper = Wrappers.<Serve>lambdaQuery()
                .eq(Serve::getServeItemId, serveItemId)
                .eq(ObjectUtil.isNotEmpty(saleStatus), Serve::getSaleStatus, saleStatus);
        return baseMapper.selectCount(queryWrapper);
    }

    /**
     * 分页查询
     *
     * @param servePageQueryReqDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO) {
        return PageHelperUtils.selectPage(servePageQueryReqDTO, () -> baseMapper.queryServeListByRegionId(servePageQueryReqDTO.getRegionId()));
    }

    /**
     * 删除服务
     *
     * @param id 服务id
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        //草稿状态方可删除
        if (!(serve.getSaleStatus()==FoundationStatusEnum.INIT.getStatus())) {
            throw new ForbiddenOperationException("草稿状态方可删除");
        }

        //删除服务
        baseMapper.deleteById(id);

        //同步数据
        serveSyncMapper.deleteById(id);
    }

    /**
     * 根据城市编码查询服务项id列表
     *
     * @param cityCode 城市编码
     * @return 服务项id列表
     */
    @Override
    public List<Long> queryServeItemIdListByCityCode(String cityCode) {
        LambdaQueryWrapper<Serve> queryWrapper = Wrappers.<Serve>lambdaQuery().eq(Serve::getCityCode, cityCode).select(Serve::getServeItemId);
        List<Serve> serveList = baseMapper.selectList(queryWrapper);
        return serveList.stream().map(Serve::getServeItemId).distinct().collect(Collectors.toList());
    }

    /**
     * 查询热门服务列表
     *
     * @return 热门服务列表
     */
    @Override
    public List<Serve> queryHotAndOnSaleServeList() {
        LambdaQueryWrapper<Serve> queryWrapper = Wrappers.<Serve>lambdaQuery()
                .eq(Serve::getIsHot, EnableStatusEnum.ENABLE.getStatus())
                .eq(Serve::getSaleStatus, FoundationStatusEnum.ENABLE.getStatus());
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据id查询服务详情
     *
     * @param id 服务id
     * @return 服务详情
     */
    @Override
    public ServeAggregationSimpleResDTO findDetailById(Long id) {
        //1.查询服务信息
        Serve serve = homeService.queryServeByIdCache(id);

        //2.查询服务项信息
        ServeItem serveItem = homeService.queryServeItemByIdCache(serve.getServeItemId());

        //3.封装数据
        ServeAggregationSimpleResDTO serveAggregationSimpleResDTO = BeanUtil.toBean(serve, ServeAggregationSimpleResDTO.class);
        serveAggregationSimpleResDTO.setServeItemName(serveItem.getName());
        serveAggregationSimpleResDTO.setServeItemImg(serveItem.getImg());
        serveAggregationSimpleResDTO.setDetailImg(serveItem.getDetailImg());
        serveAggregationSimpleResDTO.setUnit(serveItem.getUnit());
        return serveAggregationSimpleResDTO;
    }

    /**
     * 根据区域id查询热门服务列表
     *
     * @param regionId 区域id
     * @return 热门服务列表
     */
    @Override
    public List<ServeAggregationSimpleResDTO> findHotServeListByRegionId(Long regionId) {
        return baseMapper.findHotServeListByRegionId(regionId);
    }

    /**
     * 根据区域id查询服务类型列表
     *
     * @param regionId 区域id
     * @return 服务类型列表
     */
    @Override
    public List<ServeAggregationTypeSimpleResDTO> findServeTypeListByRegionId(Long regionId) {
        return baseMapper.findServeTypeListByRegionId(regionId);
    }


    /**
     * 上架
     *
     * @param id         服务id
     */
    @Override
    @Transactional
    @CachePut(value = RedisConstants.CacheName.SERVE, key = "#id",  cacheManager = RedisConstants.CacheManager.ONE_DAY)
    public Serve onSale(Long id){
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        //上架状态
        Integer saleStatus = serve.getSaleStatus();
        //草稿或下架状态方可上架
        if (!(saleStatus==FoundationStatusEnum.INIT.getStatus() || saleStatus==FoundationStatusEnum.DISABLE.getStatus())) {
            throw new ForbiddenOperationException("草稿或下架状态方可上架");
        }
        //服务项id
        Long serveItemId = serve.getServeItemId();
        ServeItem serveItem = serveItemMapper.selectById(serveItemId);
        if(ObjectUtil.isNull(serveItem)){
            throw new ForbiddenOperationException("所属服务项不存在");
        }
        //服务项的启用状态
        Integer activeStatus = serveItem.getActiveStatus();
        //服务项为启用状态方可上架
        if (!(FoundationStatusEnum.ENABLE.getStatus()==activeStatus)) {
            throw new ForbiddenOperationException("服务项为启用状态方可上架");
         }

       //更新上架状态
        LambdaUpdateWrapper<Serve> updateWrapper = Wrappers.<Serve>lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getSaleStatus, FoundationStatusEnum.ENABLE.getStatus());
        update(updateWrapper);
        //添加同步表
        addServeSync(id);
        return baseMapper.selectById(id);

    }
    /**
     * 下架
     *
     * @param id         服务id
     */
    @Override
    @Transactional
    @CacheEvict(value = RedisConstants.CacheName.SERVE, key = "#id", beforeInvocation = true)
    public Serve offSale(Long id){
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        //上架状态
        Integer saleStatus = serve.getSaleStatus();
        //上架状态方可下架
        if (!(saleStatus==FoundationStatusEnum.ENABLE.getStatus())) {
            throw new ForbiddenOperationException("上架状态方可下架");
        }
        //更新下架状态
        LambdaUpdateWrapper<Serve> updateWrapper = Wrappers.<Serve>lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getSaleStatus, FoundationStatusEnum.DISABLE.getStatus());
        update(updateWrapper);
        //删除同步表的数据
        serveSyncMapper.deleteById(id);
        return baseMapper.selectById(id);
    }

    /**
     * 新增服务同步数据
     *
     * @param serveId 服务id
     */
    private void addServeSync(Long serveId) {
        //服务信息
        Serve serve = baseMapper.selectById(serveId);
        //区域信息
        Region region = regionMapper.selectById(serve.getRegionId());
        //服务项信息
        ServeItem serveItem = serveItemMapper.selectById(serve.getServeItemId());
        //服务类型
        ServeType serveType = serveTypeMapper.selectById(serveItem.getServeTypeId());

        ServeSync serveSync = new ServeSync();
        serveSync.setServeTypeId(serveType.getId());
        serveSync.setServeTypeName(serveType.getName());
        serveSync.setServeTypeIcon(serveType.getServeTypeIcon());
        serveSync.setServeTypeImg(serveType.getImg());
        serveSync.setServeTypeSortNum(serveType.getSortNum());

        serveSync.setServeItemId(serveItem.getId());
        serveSync.setServeItemIcon(serveItem.getServeItemIcon());
        serveSync.setServeItemName(serveItem.getName());
        serveSync.setServeItemImg(serveItem.getImg());
        serveSync.setServeItemSortNum(serveItem.getSortNum());
        serveSync.setUnit(serveItem.getUnit());
        serveSync.setDetailImg(serveItem.getDetailImg());
        serveSync.setPrice(serve.getPrice());

        serveSync.setCityCode(region.getCityCode());
        serveSync.setId(serve.getId());
        serveSync.setIsHot(serve.getIsHot());
        serveSyncMapper.insert(serveSync);
    }

    /**
     * 根据id查询详情
     *
     * @param id 服务id
     * @return 服务详情
     */
    @Override
    public ServeAggregationResDTO findServeDetailById(Long id) {
        return baseMapper.findServeDetailById(id);
    }


}
