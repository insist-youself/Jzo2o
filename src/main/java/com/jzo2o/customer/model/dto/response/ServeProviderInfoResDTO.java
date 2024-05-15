package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务人员/机构信息
 *
 * @author itcast
 * @create 2023/9/18 10:01
 **/
@Data
@ApiModel("服务人员/机构信息")
public class ServeProviderInfoResDTO {
    /**
     * 主键
     */
    @ApiModelProperty("服务人员/机构id")
    private Long id;

    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private String code;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String phone;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;


    /**
     * 综合评分
     */
    @ApiModelProperty("综合评分")
    private Double score;

    /**
     * 好评率
     */
    @ApiModelProperty("好评率")
    private String goodLevelRate;
}
