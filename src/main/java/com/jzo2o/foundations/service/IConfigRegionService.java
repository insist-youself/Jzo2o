package com.jzo2o.foundations.service;

import com.jzo2o.api.foundations.dto.response.ConfigRegionInnerResDTO;
import com.jzo2o.foundations.model.domain.ConfigRegion;
import com.jzo2o.foundations.model.dto.request.ConfigRegionSetReqDTO;
import com.jzo2o.foundations.model.dto.response.ConfigRegionResDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 区域业务配置 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-08-21
 */
public interface IConfigRegionService extends IService<ConfigRegion> {

    /**
     * 获取区域配置
     *
     * @param id 区域id
     * @return 区域配置信息
     */
    ConfigRegionResDTO queryById(Long id);

    /**
     * 设置区域业务配置
     *
     * @param id 区域id
     * @param configRegionSetReqDTO 区域配置
     */
    void setConfigRegionById(Long id, ConfigRegionSetReqDTO configRegionSetReqDTO);

    /**
     * 初始化区域配置
     * @param id
     * @param cityCode
     */
    void init(Long id, String cityCode);

    /**
     * 查询所有的区域配置
     *
     * @return
     */
    List<ConfigRegionInnerResDTO> queryAll();

    /**
     * 根据城市编码获取区域配置
     *
     * @param cityCode 城市编码
     * @return 区域配置
     */
    ConfigRegionInnerResDTO queryByCityCode(String cityCode);
}
