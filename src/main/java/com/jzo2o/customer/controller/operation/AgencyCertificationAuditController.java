package com.jzo2o.customer.controller.operation;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.response.AgencyCertificationAuditResDTO;
import com.jzo2o.customer.service.IAgencyCertificationAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author itcast
 */
@RestController("operationAgencyCertificationAuditController")
@RequestMapping("/operation/agency-certification-audit")
@Api(tags = "运营端 - 审核机构认证相关接口")
public class AgencyCertificationAuditController {

    @Resource
    private IAgencyCertificationAuditService agencyCertificationAuditService;

    @GetMapping("/page")
    @ApiOperation("审核机构认证分页查询")
    public PageResult<AgencyCertificationAuditResDTO> page(AgencyCertificationAuditPageQueryReqDTO agencyCertificationAuditPageQueryReqDTO) {
        return agencyCertificationAuditService.pageQuery(agencyCertificationAuditPageQueryReqDTO);
    }

    @PutMapping("/audit/{id}")
    @ApiOperation("审核机构认证信息")
    public void auditById(@PathVariable("id") Long id, CertificationAuditReqDTO certificationAuditReqDTO) {
        agencyCertificationAuditService.auditById(id, certificationAuditReqDTO);
    }
}
