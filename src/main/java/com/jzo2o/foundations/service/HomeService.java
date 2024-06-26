package com.jzo2o.foundations.service;

import com.jzo2o.api.foundations.dto.response.RegionSimpleResDTO;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;

import java.util.List;

/**
 * 首页查询相关功能
 *
 * @author itcast
 * @create 2023/8/21 10:55
 **/
public interface HomeService {

    /**
     * 已开通服务区域列表
     *
     * @return 区域简略列表
     */
    List<RegionSimpleResDTO> queryActiveRegionListCache();

    /**
     * 根据区域id获取服务图标信息
     *
     * @param regionId 区域id
     * @return 服务图标列表
     */
    List<ServeCategoryResDTO> queryServeIconCategoryByRegionIdCache(Long regionId);

    /**
     * 根据区域id查询热门服务列表
     *
     * @param regionId 区域id
     * @return 服务列表
     */
    List<ServeAggregationSimpleResDTO> findHotServeListByRegionIdCache(Long regionId);

    /**
     * 根据区域id查询已开通的服务类型
     *
     * @param regionId 区域id
     * @return 已开通的服务类型
     */
    List<ServeAggregationTypeSimpleResDTO> queryServeTypeListByRegionIdCache(Long regionId);

    /**
     * 根据id查询区域服务信息
     *
     * @param id 服务id
     * @return 服务
     */
    Serve queryServeByIdCache(Long id);

    /**
     * 根据id查询服务项
     *
     * @param id 服务项id
     * @return 服务项
     */
    ServeItem queryServeItemByIdCache(Long id);

    /**
     * 刷新区域id相关缓存：首页图标、热门服务、服务分类
     *
     * @param regionId 区域id
     */
    void refreshRegionRelateCaches(Long regionId);
}
