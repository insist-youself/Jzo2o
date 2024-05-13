package com.jzo2o.foundations.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.foundations.dto.response.RegionSimpleResDTO;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.CityDirectoryMapper;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.model.domain.CityDirectory;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.dto.request.RegionPageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.RegionUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.RegionResDTO;
import com.jzo2o.foundations.service.IConfigRegionService;
import com.jzo2o.foundations.service.IRegionService;
import com.jzo2o.mysql.utils.PageUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 区域管理
 *
 * @author itcast
 * @create 2023/7/17 16:50
 **/
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {
    @Resource
    private IConfigRegionService configRegionService;
    @Resource
    private CityDirectoryMapper cityDirectoryMapper;


    /**
     * 区域新增
     *
     * @param regionUpsertReqDTO 插入更新区域
     */
    @Override
    @Transactional
    public void add(RegionUpsertReqDTO regionUpsertReqDTO) {
        //1.校验城市编码是否重复
        LambdaQueryWrapper<Region> queryWrapper = Wrappers.<Region>lambdaQuery().eq(Region::getCityCode, regionUpsertReqDTO.getCityCode());
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ForbiddenOperationException("城市提交重复");
        }

        //查询城市
        CityDirectory cityDirectory = cityDirectoryMapper.selectById(regionUpsertReqDTO.getCityCode());
        //查询城市的排序位
        int sotNum = cityDirectory.getSortNum();

        //2.新增区域
        Region region = BeanUtil.toBean(regionUpsertReqDTO, Region.class);
        region.setSortNum(sotNum);
        baseMapper.insert(region);

        //3.初始化区域配置
        configRegionService.init(region.getId(), region.getCityCode());
    }

    /**
     * 区域修改
     *
     * @param id           区域id
     * @param managerName  负责人姓名
     * @param managerPhone 负责人电话
     */
    @Override
    public void update(Long id, String managerName, String managerPhone) {
        Region region = new Region();
        region.setId(id);
        region.setManagerName(managerName);
        region.setManagerPhone(managerPhone);
        baseMapper.updateById(region);
    }

    /**
     * 区域删除
     *
     * @param id 区域id
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        //区域信息
        Region region = baseMapper.selectById(id);
        //启用状态
        Integer activeStatus = region.getActiveStatus();
        //草稿状态方可删除
        if (!(FoundationStatusEnum.INIT.getStatus() == activeStatus)) {
            throw new ForbiddenOperationException("草稿状态方可删除");
        }
        //删除
        baseMapper.deleteById(id);

    }

    /**
     * 分页查询
     *
     * @param regionPageQueryReqDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<RegionResDTO> page(RegionPageQueryReqDTO regionPageQueryReqDTO) {
        Page<Region> page = PageUtils.parsePageQuery(regionPageQueryReqDTO, Region.class);
        Page<Region> serveTypePage = baseMapper.selectPage(page, new QueryWrapper<>());
        return PageUtils.toPage(serveTypePage, RegionResDTO.class);
    }

    /**
     * 已开通服务区域列表
     *
     * @return 区域列表
     */
    @Override
    public List<RegionSimpleResDTO> queryActiveRegionList() {
        LambdaQueryWrapper<Region> queryWrapper = Wrappers.<Region>lambdaQuery()
                .eq(Region::getActiveStatus, FoundationStatusEnum.ENABLE.getStatus())
                .orderByAsc(Region::getSortNum);
        List<Region> regionList = baseMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(regionList, RegionSimpleResDTO.class);
    }

    /**
     * 区域启用
     *
     * @param id 区域id
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = RedisConstants.CacheName.JZ_CACHE, key = "'ACTIVE_REGIONS'", beforeInvocation = true),
            @CacheEvict(value = RedisConstants.CacheName.SERVE_ICON, key = "#id", beforeInvocation = true),
            @CacheEvict(value = RedisConstants.CacheName.HOT_SERVE, key = "#id", beforeInvocation = true),
            @CacheEvict(value = RedisConstants.CacheName.SERVE_TYPE, key = "#id", beforeInvocation = true)
    })
    public void active(Long id) {
        //区域信息
        Region region = baseMapper.selectById(id);
        //启用状态
        Integer activeStatus = region.getActiveStatus();
        //草稿或禁用状态方可启用
        if (!(FoundationStatusEnum.INIT.getStatus() == activeStatus || FoundationStatusEnum.DISABLE.getStatus() == activeStatus)) {
            throw new ForbiddenOperationException("草稿或禁用状态方可启用");
        }
        //如果需要启用区域，需要校验该区域下是否有上架的服务
        //todo

        //更新启用状态
        LambdaUpdateWrapper<Region> updateWrapper = Wrappers.<Region>lambdaUpdate()
                .eq(Region::getId, id)
                .set(Region::getActiveStatus, FoundationStatusEnum.ENABLE.getStatus());
        update(updateWrapper);

        //3.如果是启用操作，刷新缓存：启用区域列表、首页图标、热门服务、服务类型
        // todo
    }

    /**
     * 区域禁用
     *
     * @param id 区域id
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = RedisConstants.CacheName.JZ_CACHE, key = "'ACTIVE_REGIONS'", beforeInvocation = true),
            @CacheEvict(value = RedisConstants.CacheName.SERVE_ICON, key = "#id", beforeInvocation = true),
            @CacheEvict(value = RedisConstants.CacheName.HOT_SERVE, key = "#id", beforeInvocation = true),
            @CacheEvict(value = RedisConstants.CacheName.SERVE_TYPE, key = "#id", beforeInvocation = true)
    })
    public void deactivate(Long id) {
        //区域信息
        Region region = baseMapper.selectById(id);
        //启用状态
        Integer activeStatus = region.getActiveStatus();
        //启用状态方可禁用
        if (!(FoundationStatusEnum.ENABLE.getStatus() == activeStatus)) {
            throw new ForbiddenOperationException("启用状态方可禁用");
        }

        //1.如果禁用区域下有上架的服务则无法禁用
        //todo
//        int count = serveService.queryServeCountByRegionIdAndSaleStatus(id, FoundationStatusEnum.ENABLE.getStatus());
//        if (count > 0) {
//            throw new ForbiddenOperationException("区域下有上架的服务无法禁用");
//        }

        //更新禁用状态
        LambdaUpdateWrapper<Region> updateWrapper = Wrappers.<Region>lambdaUpdate()
                .eq(Region::getId, id)
                .set(Region::getActiveStatus, FoundationStatusEnum.DISABLE.getStatus());
        update(updateWrapper);
    }

    /**
     * 已开通服务区域列表
     *
     * @return 区域简略列表
     */
    @Override
    @Cacheable(value = RedisConstants.CacheName.JZ_CACHE, key = "'ACTIVE_REGIONS'", cacheManager = RedisConstants.CacheManager.FOREVER)
    public List<RegionSimpleResDTO> queryActiveRegionListCache() {
        return queryActiveRegionList();
    }

}
