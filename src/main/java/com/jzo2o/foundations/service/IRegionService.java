package com.jzo2o.foundations.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.api.foundations.dto.response.RegionSimpleResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.dto.request.RegionPageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.RegionUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.RegionDisplayResDTO;
import com.jzo2o.foundations.model.dto.response.RegionResDTO;

import java.util.List;

/**
 * 区域管理
 *
 * @author itcast
 * @create 2023/7/17 16:49
 **/
public interface IRegionService extends IService<Region> {

    /**
     * 区域新增
     *
     * @param regionUpsertReqDTO 插入更新区域
     */
    void add(RegionUpsertReqDTO regionUpsertReqDTO);

    /**
     * 区域修改
     *
     * @param id           区域id
     * @param managerName  负责人姓名
     * @param managerPhone 负责人电话
     */
    void update(Long id, String managerName, String managerPhone);

    /**
     * 区域删除
     *
     * @param id 区域id
     */
    void deleteById(Long id);

    /**
     * 分页查询
     *
     * @param regionPageQueryReqDTO 查询条件
     * @return 分页结果
     */
    PageResult<RegionResDTO> page(RegionPageQueryReqDTO regionPageQueryReqDTO);

    /**
     * 已开通服务区域列表
     *
     * @return 区域列表
     */
    List<RegionSimpleResDTO> queryActiveRegionList();

    /**
     * 区域启用
     *
     * @param id 区域id
     */
    void active(Long id);

    /**
     * 区域禁用
     *
     * @param id 区域id
     */
    void deactivate(Long id);

    /**
     * 已开通服务区域列表
     *
     * @return 区域简略列表
     */
    List<RegionSimpleResDTO> queryActiveRegionListCache();

}
