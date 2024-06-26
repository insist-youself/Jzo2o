package com.jzo2o.api.customer.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 地址薄
 * </p>
 *
 * @author itcast
 * @since 2023-07-06
 */
@Data
@ApiModel("地址薄详情")
public class AddressBookResDTO {

    /**
     * 主键
     */
    @ApiModelProperty("地址薄id")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

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
     * 省份
     */
    @ApiModelProperty("省份")
    private String province;

    /**
     * 市级
     */
    @ApiModelProperty("市级")
    private String city;

    /**
     * 区/县
     */
    @ApiModelProperty("区/县")
    private String county;

    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String address;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private Double lon;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private Double lat;

    /**
     * 是否为默认地址，0：否，1：是
     */
    @ApiModelProperty("是否为默认地址，0：否，1：是")
    private Integer isDefault;

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
