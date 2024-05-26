package com.jzo2o.foundations.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import com.jzo2o.foundations.model.dto.response.ServeSimpleResDTO;
import com.jzo2o.foundations.service.HomeService;
import com.jzo2o.foundations.service.IRegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * 首页查询相关功能
 * @author linger
 * @date 2024/5/22 20:46
 */
@Slf4j
@Service
public class HomeServiceImpl implements HomeService {

    @Resource
    private ServeMapper serveMapper;

    @Resource
    private IRegionService regionService;


    @Override
    @Caching(
            cacheable = {
                    @Cacheable(value = RedisConstants.CacheName.HOT_SERVE, key = "#regionId", unless = "#result.size() != 0", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
                    @Cacheable(value = RedisConstants.CacheName.HOT_SERVE, key = "#regionId", unless = "#result.size() == 0", cacheManager = RedisConstants.CacheManager.FOREVER),
            }
    )
    public List<ServeAggregationSimpleResDTO> queryHotServeList(Long regionId) {
        return serveMapper.queryHotServeList(regionId);
    }

    /**
     * 根据区域id查询已开通的服务类型
     *
     * @param regionId 区域id
     * @return 已开通的服务类型
     */
    @Caching(
            cacheable = {
                    //result为null时,属于缓存穿透情况，缓存时间30分钟
                    @Cacheable(value = RedisConstants.CacheName.SERVE_ICON, key = "#regionId", unless = "#result.size() != 0", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
                    //result不为null时,永久缓存
                    @Cacheable(value = RedisConstants.CacheName.SERVE_ICON, key = "#regionId", unless = "#result.size() == 0", cacheManager = RedisConstants.CacheManager.FOREVER)
            }
    )
    @Override
    public List<ServeCategoryResDTO> queryServeIconCategoryByRegionIdCache(Long regionId) {
        // 1. 校验当前城市状态是否启用
        Region region = regionService.getById(regionId);
        if (ObjectUtil.isNull(region) ||
                region.getActiveStatus() != FoundationStatusEnum.ENABLE.getStatus()) {
            return Collections.emptyList();
        }
        // 2. 根据城市编码查询所有的服务图标
        List<ServeCategoryResDTO> list = serveMapper.findServeIconCategoryByRegionId(regionId);
        if(ObjectUtil.isNull(list)) {
            return Collections.emptyList();
        }
        // 3. 服务类型获取前两个， 服务项获取前四个
        int endIndex = list.size() >= 2 ? 2 : list.size();
        List<ServeCategoryResDTO> serveCategoryResDTOS = new ArrayList<>(list.subList(0, endIndex));
        serveCategoryResDTOS.forEach(v -> {
            List<ServeSimpleResDTO> serveResDTOList = v.getServeResDTOList();
            int endIndex2 = serveResDTOList.size() >= 4 ? 4 : serveResDTOList.size();
            List<ServeSimpleResDTO> serveSimpleResDTOS = new ArrayList<>(serveResDTOList.subList(0, endIndex2));
            v.setServeResDTOList(serveSimpleResDTOS);
        });
        return serveCategoryResDTOS;
    }

    @Caching(cacheable = {
            @Cacheable(value = RedisConstants.CacheName.SERVE_TYPE, key = "#regionId" , unless = "#result.size() == 0",cacheManager = RedisConstants.CacheManager.FOREVER),
            @Cacheable(value = RedisConstants.CacheName.SERVE_TYPE, key = "#regionId" , unless = "#result.size() != 0",cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
    })
    @Override
    public List<ServeAggregationTypeSimpleResDTO> queryServeTypeList(Long regionId) {
        return serveMapper.findServeTypeList(regionId);
    }

    @Caching(cacheable = {
            @Cacheable(value = RedisConstants.CacheName.SERVE_ITEM, key = "#id" , unless = "#result == null",cacheManager = RedisConstants.CacheManager.FOREVER),
            @Cacheable(value = RedisConstants.CacheName.SERVE_ITEM, key = "#id" , unless = "#result != null",cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
    })
    @Override
    public ServeAggregationSimpleResDTO queryByServeId(Long id) {
        return serveMapper.queryByServeId(id);
    }


}
