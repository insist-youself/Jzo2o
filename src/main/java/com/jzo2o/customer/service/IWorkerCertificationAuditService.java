package com.jzo2o.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.WorkerCertificationAudit;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.response.RejectReasonResDTO;
import com.jzo2o.customer.model.dto.response.WorkerCertificationAuditResDTO;


/**
 * <p>
 * 服务人员认证审核信息表 服务类
 * </p>
 *
 * @author linger
 * @since 2024-05-19
 */
public interface IWorkerCertificationAuditService extends IService<WorkerCertificationAudit> {

    /**
     * 提交认证申请
     * @param workerCertificationAuditAddReqDTO 服务端提交认证申请请求信息
     */
    void submitCertification(WorkerCertificationAuditAddReqDTO workerCertificationAuditAddReqDTO);

    /**
     * 查询最新的驳回原因
     *
     * @param serveProviderId
     * @return
     */
    RejectReasonResDTO rejectReason(Long serveProviderId);


    /**
     * 审核服务人员认证分页查询
     * @param workerCertificationAuditPageQueryReqDTO
     * @return
     */
    PageResult<WorkerCertificationAuditResDTO> pageQuery(WorkerCertificationAuditPageQueryReqDTO workerCertificationAuditPageQueryReqDTO);

    /**
     * 审核服务人员认证信息
     * @param id
     * @param certificationAuditReqDTO
     */
    void auditById(Long id, CertificationAuditReqDTO certificationAuditReqDTO);
}
