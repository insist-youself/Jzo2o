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
 * 机构认证审核信息表 服务类
 * </p>
 *
 * @author linger
 * @since 2024-05-19
 */
public interface IAgencyCertificationAuditService extends IService<AgencyCertificationAudit> {

    /**
     * 提交认证申请
     * @param agencyCertificationAuditAddReqDTO 机构端提交认证申请请求信息
     */
    void submitCertification(AgencyCertificationAuditAddReqDTO agencyCertificationAuditAddReqDTO);

    /**
     * 查询最新的驳回原因
     *
     * @return serveProviderId
     */
    RejectReasonResDTO rejectReason(Long serveProviderId);

    /**
     * 审核机构认证分页查询
     * @param agencyCertificationAuditPageQueryReqDTO
     * @return
     */
    PageResult<AgencyCertificationAuditResDTO> pageQuery(AgencyCertificationAuditPageQueryReqDTO agencyCertificationAuditPageQueryReqDTO);

    /**
     * 审核机构认证信息
     * @param id
     * @param certificationAuditReqDTO
     */
    void auditById(Long id, CertificationAuditReqDTO certificationAuditReqDTO);
}
