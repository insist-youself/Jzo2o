package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务人员/机构
 *
 * @author itcast
 * @create 2023/9/4 20:27
 **/
@Data
@ApiModel("服务人员/机构")
public class ServeProviderListResDTO {
    /**
     * 服务人员/机构id
     */
    @ApiModelProperty("服务人员/机构id")
    private  Long id;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 身份证号/统一社会信用代码
     */
    @ApiModelProperty("身份证号/统一社会信用代码")
    private String idNumber;

    /**
     * 城市名称
     */
    @ApiModelProperty("城市名称")
    private String cityName;

    /**
     * 是否可以接单，0：关闭接单，1：开启接单
     */
    @ApiModelProperty("是否可以接单，0：关闭接单，1：开启接单")
    private Integer canPickUp;

    /**
     * 状态，0：正常，1：冻结
     */
    @ApiModelProperty("状态，0：正常，1：冻结")
    private Integer status;

    /**
     * 认证时间
     */
    @ApiModelProperty("认证时间")
    private LocalDateTime certificationTime;
}
