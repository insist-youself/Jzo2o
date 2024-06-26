package com.jzo2o.customer.controller.operation;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.response.WorkerCertificationAuditResDTO;
import com.jzo2o.customer.service.IWorkerCertificationAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author itcast
 */
@RestController("operationWorkerCertificationAuditController")
@RequestMapping("/operation/worker-certification-audit")
@Api(tags = "运营端 - 审核服务人员认证相关接口")
public class WorkerCertificationAuditController {

    @Resource
    private IWorkerCertificationAuditService workerCertificationAuditService;

    @GetMapping("/page")
    @ApiOperation("审核服务人员认证分页查询")
    public PageResult<WorkerCertificationAuditResDTO> page(WorkerCertificationAuditPageQueryReqDTO workerCertificationAuditPageQueryReqDTO) {
        return workerCertificationAuditService.pageQuery(workerCertificationAuditPageQueryReqDTO);
    }

    @PutMapping("/audit/{id}")
    @ApiOperation("审核服务人员认证信息")
    public void auditById(@PathVariable("id") Long id, CertificationAuditReqDTO certificationAuditReqDTO) {
        workerCertificationAuditService.auditById(id, certificationAuditReqDTO);
    }
}
