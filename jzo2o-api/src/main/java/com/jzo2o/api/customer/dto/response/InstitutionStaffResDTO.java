package com.jzo2o.api.customer.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 机构下属服务人员响应数据
 *
 * @author itcast
 * @create 2023/7/18 20:45
 **/
@Data
@ApiModel("机构下属服务人员响应数据")
public class InstitutionStaffResDTO {
    /**
     * 服务人员id
     */
    @ApiModelProperty("服务人员id")
    private Long id;

    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private String code;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String phone;

    /**
     * 综合评分
     */
    @ApiModelProperty("综合评分")
    private Double score;

    /**
     * 身份证号
     */
    @ApiModelProperty("身份证号")
    private String idCardNo;

    /**
     * 证明资料列表
     */
    @ApiModelProperty("证明资料列表")
    private List<String> certificationImgs;

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
