package com.jzo2o.foundations.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.jzo2o.api.foundations.dto.response.RegionSimpleResDTO;
import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import com.jzo2o.foundations.model.dto.response.ServeSimpleResDTO;
import com.jzo2o.foundations.service.HomeService;
import com.jzo2o.foundations.service.IRegionService;
import com.jzo2o.foundations.service.IServeItemService;
import com.jzo2o.foundations.service.IServeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 首页查询相关功能
 *
 * @author itcast
 * @create 2023/8/21 10:57
 **/
@Slf4j
@Service
public class HomeServiceImpl implements HomeService {
    @Resource
    private IRegionService regionService;
    @Resource
    private IServeService serveService;
    @Resource
    private ServeMapper serveMapper;
    @Resource
    private IServeItemService serveItemService;
    //解决springCache同级方法调用失效问题
    @Resource
    private HomeService homeService;


    /**
     * 已开通服务区域列表
     *
     * @return 区域简略列表
     */
    @Override
    @Cacheable(value = RedisConstants.CacheName.JZ_CACHE, key = "'ACTIVE_REGIONS'", cacheManager = RedisConstants.CacheManager.FOREVER)
    public List<RegionSimpleResDTO> queryActiveRegionListCache() {
        return regionService.queryActiveRegionList();
    }

    /**
     * 根据区域id获取服务图标信息
     *
     * @param regionId 区域id
     * @return 服务图标列表
     */
    @Override
    @Caching(
            cacheable = {
                    //result为null时,属于缓存穿透情况，缓存时间30分钟
                    @Cacheable(value = RedisConstants.CacheName.SERVE_ICON, key = "#regionId", unless = "#result.size() != 0", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
                    //result不为null时,永久缓存
                    @Cacheable(value = RedisConstants.CacheName.SERVE_ICON, key = "#regionId", unless = "#result.size() == 0", cacheManager = RedisConstants.CacheManager.FOREVER)
            }
    )
    public List<ServeCategoryResDTO> queryServeIconCategoryByRegionIdCache(Long regionId) {
        //1.校验当前城市是否为启用状态
        Region region = regionService.getById(regionId);
        if (ObjectUtil.isEmpty(region) || ObjectUtil.equal(FoundationStatusEnum.DISABLE.getStatus(), region.getActiveStatus())) {
            return Collections.emptyList();
        }

        //2.根据城市编码查询所有的服务图标
        List<ServeCategoryResDTO> list = serveMapper.findServeIconCategoryByRegionId(regionId);
        if (ObjectUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        //3.服务类型取前两个，每个类型下服务项取前4个
        //list的截止下标
        int endIndex = list.size() >= 2 ? 2 : list.size();
        List<ServeCategoryResDTO> serveCategoryResDTOS = new ArrayList<>(list.subList(0, endIndex));
        serveCategoryResDTOS.forEach(v -> {
            List<ServeSimpleResDTO> serveResDTOList = v.getServeResDTOList();
            //serveResDTOList的截止下标
            int endIndex2 = serveResDTOList.size() >= 4 ? 4 : serveResDTOList.size();
            List<ServeSimpleResDTO> serveSimpleResDTOS = new ArrayList<>(serveResDTOList.subList(0, endIndex2));
            v.setServeResDTOList(serveSimpleResDTOS);
        });

        return serveCategoryResDTOS;
    }


    /**
     * 根据区域id查询热门服务列表
     *
     * @param regionId 区域id
     * @return 服务列表
     */
    @Override
    @Caching(
            cacheable = {
                    //result为null时,属于缓存穿透情况，缓存时间30分钟
                    @Cacheable(value = RedisConstants.CacheName.HOT_SERVE, key = "#regionId", unless = "#result.size() != 0", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
                    //result不为null时,永久缓存
                    @Cacheable(value = RedisConstants.CacheName.HOT_SERVE, key = "#regionId", unless = "#result.size() == 0", cacheManager = RedisConstants.CacheManager.FOREVER)
            }
    )
    public List<ServeAggregationSimpleResDTO> findHotServeListByRegionIdCache(Long regionId) {
        //1.校验当前城市是否为启用状态
        Region region = regionService.getById(regionId);
        if (ObjectUtil.equal(FoundationStatusEnum.DISABLE.getStatus(), region.getActiveStatus())) {
            return Collections.emptyList();
        }

        //2.根据城市编码查询热门服务
        List<ServeAggregationSimpleResDTO> list = serveService.findHotServeListByRegionId(regionId);
        if (ObjectUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    /**
     * 根据区域id查询已开通的服务类型
     *
     * @param regionId 区域id
     * @return 已开通的服务类型
     */
    @Override
    @Caching(
            cacheable = {
                    //result为null时,属于缓存穿透情况，缓存时间30分钟
                    @Cacheable(value = RedisConstants.CacheName.SERVE_TYPE, key = "#regionId", unless = "#result.size() != 0", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
                    //result不为null时,永久缓存
                    @Cacheable(value = RedisConstants.CacheName.SERVE_TYPE, key = "#regionId", unless = "#result.size() == 0", cacheManager = RedisConstants.CacheManager.FOREVER)
            }
    )
    public List<ServeAggregationTypeSimpleResDTO> queryServeTypeListByRegionIdCache(Long regionId) {
        //1.校验当前城市是否为启用状态
        Region region = regionService.getById(regionId);
        if (ObjectUtil.equal(FoundationStatusEnum.DISABLE.getStatus(), region.getActiveStatus())) {
            return Collections.emptyList();
        }

        //2.根据城市编码查询服务对应的服务分类
        List<ServeAggregationTypeSimpleResDTO> list = serveService.findServeTypeListByRegionId(regionId);
        if (ObjectUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    /**
     * 根据id查询区域服务信息
     *
     * @param id 服务id
     * @return 服务
     */
    @Override
    @Cacheable(value = RedisConstants.CacheName.SERVE, key = "#id", cacheManager = RedisConstants.CacheManager.ONE_DAY)
    public Serve queryServeByIdCache(Long id) {
        return serveService.getById(id);
    }

    /**
     * 根据id查询服务项
     *
     * @param id 服务项id
     * @return 服务项
     */
    @Override
    @Cacheable(value = RedisConstants.CacheName.SERVE_ITEM, key = "#id", cacheManager = RedisConstants.CacheManager.ONE_DAY)
    public ServeItem queryServeItemByIdCache(Long id) {
        return serveItemService.getById(id);
    }

    /**
     * 刷新区域id相关缓存：首页图标、热门服务、服务分类
     *
     * @param regionId 区域id
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = RedisConstants.CacheName.SERVE_ICON, key = "#regionId", beforeInvocation = true),
            @CacheEvict(value = RedisConstants.CacheName.HOT_SERVE, key = "#regionId", beforeInvocation = true),
            @CacheEvict(value = RedisConstants.CacheName.SERVE_TYPE, key = "#regionId", beforeInvocation = true)
    })
    public void refreshRegionRelateCaches(Long regionId) {
        //刷新缓存：首页图标、热门服务、服务类型
        homeService.queryServeIconCategoryByRegionIdCache(regionId);
        homeService.findHotServeListByRegionIdCache(regionId);
        homeService.queryServeTypeListByRegionIdCache(regionId);
    }
}
