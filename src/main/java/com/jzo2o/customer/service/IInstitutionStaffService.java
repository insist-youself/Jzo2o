package com.jzo2o.customer.service;

import com.jzo2o.api.customer.dto.request.InstitutionStaffAddReqDTO;
import com.jzo2o.api.customer.dto.request.InstitutionStaffPageQueryReqDTO;
import com.jzo2o.api.customer.dto.response.InstitutionStaffResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.InstitutionStaff;
import com.jzo2o.customer.model.dto.request.InstitutionStaffUpsertReqDTO;
import com.jzo2o.customer.model.dto.response.InstitutionStaffSimpleResDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 机构下属服务人员 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-07-18
 */
public interface IInstitutionStaffService extends IService<InstitutionStaff> {

    /**
     * 新增机构下属服务人员
     *
     * @param institutionStaffUpsertReqDTO 插入更新机构下属服务人员
     */
    void add(InstitutionStaffUpsertReqDTO institutionStaffUpsertReqDTO);

    /**
     * 新增服务人员，用于模拟下单整个流程
     * @param institutionStaffAddReqDTO
     */
    void add(InstitutionStaffAddReqDTO institutionStaffAddReqDTO);

    /**
     * 更新机构下属服务人员
     *
     * @param id                           机构下属服务人员id
     * @param institutionStaffUpsertReqDTO 插入更新机构下属服务人员
     */
    void update(Long id, InstitutionStaffUpsertReqDTO institutionStaffUpsertReqDTO);

    /**
     * 分页查询
     *
     * @param institutionStaffPageQueryReqDTO 分页查询请求
     * @return 分页结果
     */
    PageResult<InstitutionStaffResDTO> pageQuery(InstitutionStaffPageQueryReqDTO institutionStaffPageQueryReqDTO);

    /**
     * 获取机构下属服务人员简略列表
     *
     * @return 服务人员简略列表
     */
    List<InstitutionStaffSimpleResDTO> queryInstitutionStaffList();

    /**
     * 删除服务人员
     *
     * @param id 服务人员id
     */
    void delete(Long id);

    /**
     * 查询机构服务人员
     * @param id
     * @param institutionId
     * @return
     */
    InstitutionStaffResDTO findByIdAndInstitutionId(Long id, Long institutionId);

    /**
     * 根据机构id获取服务人员
     *
     * @param institutionId 机构id
     * @return 机构下服务人员列表
     */
    List<InstitutionStaffResDTO> findByInstitutionId(Long institutionId);
}
