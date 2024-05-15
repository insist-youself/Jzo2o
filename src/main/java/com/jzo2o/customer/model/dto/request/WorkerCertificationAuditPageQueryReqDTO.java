package com.jzo2o.customer.model.dto.request;

import com.jzo2o.common.model.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务人员分页查询请求
 *
 * @author itcast
 * @create 2023/9/6 13:52
 **/
@Data
@ApiModel("服务人员分页查询请求")
public class WorkerCertificationAuditPageQueryReqDTO extends PageQueryDTO {
    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 身份证号
     */
    @ApiModelProperty("身份证号")
    private String idCardNo;

    /**
     * 审核状态，0：未审核，1：已审核
     */
    @ApiModelProperty("审核状态，0：未审核，1：已审核")
    private Integer auditStatus;

    /**
     * 认证状态，2：认证通过，3：认证失败
     */
    @ApiModelProperty("认证状态，2：认证通过，3：认证失败")
    private Integer certificationStatus;
}
