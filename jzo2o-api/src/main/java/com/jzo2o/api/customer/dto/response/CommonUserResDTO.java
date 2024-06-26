package com.jzo2o.api.customer.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息响应
 *
 * @author itcast
 * @since 2023-07-04
 */
@Data
@ApiModel("用户信息响应")
public class CommonUserResDTO {

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long id;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;

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
     * 状态，0：正常，1：冻结
     */
    @ApiModelProperty("状态，0：正常，1：冻结")
    private Integer status;

    /**
     * 账号冻结原因
     */
    @ApiModelProperty("账号冻结原因")
    private String accountLockReason;

    /**
     * 下单次数
     */
    @ApiModelProperty("下单次数")
    private Integer orderNumber;

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
