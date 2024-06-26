package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 地址薄新增更新
 * </p>
 *
 * @author itcast
 * @since 2023-07-06
 */
@Data
@ApiModel("地址薄新增更新")
public class AddressBookUpsertReqDTO {

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空")
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /**
     * 电话
     */
    @NotNull(message = "电话不能为空")
    @ApiModelProperty(value = "电话", required = true)
    private String phone;

    /**
     * 省份
     */
    @NotNull(message = "省份不能为空")
    @ApiModelProperty(value = "省份", required = true)
    private String province;

    /**
     * 市级
     */
    @NotNull(message = "市级不能为空")
    @ApiModelProperty(value = "市级", required = true)
    private String city;

    /**
     * 区/县
     */
    @NotNull(message = "区/县不能为空")
    @ApiModelProperty(value = "区/县", required = true)
    private String county;

    /**
     * 详细地址
     */
    @NotNull(message = "详细地址不能为空")
    @ApiModelProperty(value = "详细地址", required = true)
    private String address;

    /**
     * 是否为默认地址，0：否，1：是
     */
    @NotNull(message = "是否为默认地址不能为空")
    @ApiModelProperty(value = "是否为默认地址，0：否，1：是", required = true)
    private Integer isDefault;

    /**
     * 经纬度
     */
    @ApiModelProperty(value = "经纬度")
    private String location;
}
