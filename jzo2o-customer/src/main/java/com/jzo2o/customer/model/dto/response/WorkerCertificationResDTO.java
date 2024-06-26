package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务人员申认证信息响应值
 *
 * @author itcast
 * @create 2023/9/6 11:51
 **/
@Data
@ApiModel("服务人员认证信息响应值")
public class WorkerCertificationResDTO {
    /**
     * 主键
     */
    @ApiModelProperty("服务人员id")
    private Long id;

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
     * 身份证正面
     */
    @ApiModelProperty("身份证正面")
    private String frontImg;

    /**
     * 身份证反面
     */
    @ApiModelProperty("身份证反面")
    private String backImg;

    /**
     * 证明资料
     */
    @ApiModelProperty("证明资料")
    private String certificationMaterial;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
