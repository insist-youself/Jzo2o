package com.jzo2o.foundations.service;

import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;

import java.util.List;

/**
 * 首页查询相关功能
 * @author linger
 * @date 2024/5/22 20:44
 */
public interface HomeService {

    /**
     *  首页热门服务列表
     * @param regionId
     * @return
     */
    List<ServeAggregationSimpleResDTO> queryHotServeList(Long regionId);

    /**
     * 根据区域id获取服务图标信息
     *
     * @param regionId
     * @return
     */
    List<ServeCategoryResDTO> queryServeIconCategoryByRegionIdCache(Long regionId);


    /**
     * 首页热门服务列表
     * @param regionId
     * @return
     */
    List<ServeAggregationTypeSimpleResDTO> queryServeTypeList(Long regionId);

    /**
     * 根据id查询服务
     * @param id
     * @return
     */
    ServeAggregationSimpleResDTO queryByServeId(Long id);
}
