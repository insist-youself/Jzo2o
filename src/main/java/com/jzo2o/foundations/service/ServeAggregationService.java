package com.jzo2o.foundations.service;

import com.jzo2o.foundations.model.dto.response.ServeSimpleResDTO;

import java.util.List;

/**
 * @author linger
 * @date 2024/5/25 19:59
 */
public interface ServeAggregationService {

    /**
     * 查询服务列表
     *
     * @param cityCode    城市编码
     * @param serveTypeId 服务类型id
     * @param keyword     关键词
     * @return 服务列表
     */
    List<ServeSimpleResDTO> findServeList(String cityCode, Long serveTypeId, String keyword);
}
