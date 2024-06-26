package com.jzo2o.customer.service;

import com.jzo2o.api.foundations.dto.response.ServeItemSimpleResDTO;
import com.jzo2o.api.foundations.dto.response.ServeTypeSimpleResDTO;
import com.jzo2o.customer.model.domain.ServeSkill;
import com.jzo2o.customer.model.dto.request.ServeSkillAddReqDTO;
import com.jzo2o.customer.model.dto.response.ServeSkillCategoryResDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务技能表 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-07-18
 */
public interface IServeSkillService extends IService<ServeSkill> {

    /**
     * 批量新增或修改
     *
     * @param serveSkillAddReqDTOList 批量新增或修改数据
     */
    void batchUpsert(List<ServeSkillAddReqDTO> serveSkillAddReqDTOList);

    /**
     * 查询服务技能目录
     *
     * @return 服务技能目录
     */
    List<ServeSkillCategoryResDTO> category();

    /**
     * 查询服务者的服务技能
     *
     * @param providerId   服务者id
     * @param providerType 服务者类型
     * @param cityCode     城市编码
     * @return 服务技能列表
     */
    List<Long> queryServeSkillListByServeProvider(Long providerId, Integer providerType, String cityCode);

    /**
     * 获取服务者的技能分类
     *
     * @return 技能分类列表
     */
    List<ServeTypeSimpleResDTO> queryCurrentUserServeSkillTypeList();

    /**
     * 获取服务者的所有技能
     *
     * @return 技能列表
     */
    List<ServeItemSimpleResDTO> queryCurrentUserServeSkillItemList();
}
