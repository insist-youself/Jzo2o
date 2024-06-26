package com.jzo2o.foundations.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.api.foundations.dto.response.ServeTypeSimpleResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.ServeType;
import com.jzo2o.foundations.model.dto.request.ServeTypePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeTypeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeTypeResDTO;

import java.util.List;

/**
 * @author itcast
 */
public interface IServeTypeService extends IService<ServeType> {

    /**
     * 服务类型新增
     *
     * @param serveTypeUpsertReqDTO 插入更新服务类型
     */
    void add(ServeTypeUpsertReqDTO serveTypeUpsertReqDTO);

    /**
     * 服务类型修改
     *
     * @param id                    服务类型id
     * @param serveTypeUpsertReqDTO 插入更新服务类型
     */
    void update(Long id, ServeTypeUpsertReqDTO serveTypeUpsertReqDTO);


    /**
     * 服务类型启用/禁用
     *
     * @param id 服务类型id
     */
    void activate(Long id);

    /**
     * 服务类型启用/禁用
     *
     * @param id 服务类型id
     */
    void deactivate(Long id);

    /**
     * 根据id删除服务类型
     *
     * @param id 服务类型id
     */
    void deleteById(Long id);

    /**
     * 分页查询
     *
     * @param serveTypePageQueryReqDTO 查询条件
     * @return 分页结果
     */
    PageResult<ServeTypeResDTO> page(ServeTypePageQueryReqDTO serveTypePageQueryReqDTO);

    /**
     * 根据活动状态查询简略列表
     *
     * @param activeStatus 活动状态，0：草稿，1：禁用，:2：启用
     * @return 服务类型列表
     */
    List<ServeTypeSimpleResDTO> queryServeTypeListByActiveStatus(Integer activeStatus);
}
