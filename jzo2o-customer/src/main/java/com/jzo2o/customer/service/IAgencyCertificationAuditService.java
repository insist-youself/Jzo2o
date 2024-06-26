package com.jzo2o.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.AgencyCertificationAudit;
import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.response.AgencyCertificationAuditResDTO;
import com.jzo2o.customer.model.dto.response.RejectReasonResDTO;

/**
 * <p>
 * 机构认证审核表 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-09-06
 */
public interface IAgencyCertificationAuditService extends IService<AgencyCertificationAudit> {
    /**
     * 机构申请资质认证
     *
     * @param agencyCertificationAuditAddReqDTO 认证申请请求体
     */
    void applyCertification(AgencyCertificationAuditAddReqDTO agencyCertificationAuditAddReqDTO);

    /**
     * 审核认证信息
     *
     * @param id                       申请记录id
     * @param certificationAuditReqDTO 审核请求
     */
    void auditCertification(Long id, CertificationAuditReqDTO certificationAuditReqDTO);

    /**
     * 分页查询
     *
     * @param agencyCertificationAuditPageQueryReqDTO 分页查询条件
     * @return 分页结果
     */
    PageResult<AgencyCertificationAuditResDTO> pageQuery(AgencyCertificationAuditPageQueryReqDTO agencyCertificationAuditPageQueryReqDTO);

    /**
     * 查询当前用户最近驳回原因
     *
     * @return 驳回原因
     */
    RejectReasonResDTO queryCurrentUserLastRejectReason();
}
