package com.jzo2o.customer.controller.agency;


import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.response.RejectReasonResDTO;
import com.jzo2o.customer.service.IAgencyCertificationAuditService;
import com.jzo2o.mvc.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 机构认证审核信息表 前端控制器
 * </p>
 *
 * @author linger
 * @since 2024-05-19
 */
@RestController
@RequestMapping("/agency/agency-certification-audit")
@Api(tags = "机构端 - 提交认证接口")
public class AgencyCertificationAuditController {

    @Resource
    private IAgencyCertificationAuditService agencyCertificationAuditService;

    @PostMapping
    @ApiOperation("提交认证申请")
    public void submitCertification(@RequestBody AgencyCertificationAuditAddReqDTO agencyCertificationAuditAddReqDTO) {
        agencyCertificationAuditAddReqDTO.setServeProviderId(UserContext.currentUserId());
        agencyCertificationAuditService.submitCertification(agencyCertificationAuditAddReqDTO);
    }


    @GetMapping("/rejectReason")
    @ApiOperation("查询最新的驳回原因")
    public RejectReasonResDTO rejectReason() {
        return agencyCertificationAuditService.rejectReason(UserContext.currentUserId());
    }

}
