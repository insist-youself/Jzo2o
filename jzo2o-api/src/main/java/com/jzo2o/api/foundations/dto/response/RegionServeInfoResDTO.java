package com.jzo2o.api.foundations.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 86188
 */
@ApiModel("区域服务信息")
@Data
public class RegionServeInfoResDTO {
    /**
     * 机构佣金比例
     */
    @ApiModelProperty("机构佣金人员比例")
    private Double institutionStaffCommissionRate;

    /**
     * 服务人员佣金比例
     */
    @ApiModelProperty("服务人员佣金比例")
    private Double serveStaffCommissionRate;

    /**
     * 机构人员服务范围距离
     */
    @ApiModelProperty("机构人员服务范围距离")
    private Double institutionStaffServeDistance;

    /**
     * 服务人员服务范围距离
     */
    @ApiModelProperty("服务人员服务范围距离")
    private Double serveStaffServeDistance;

    /**
     * 派单间距时间
     */
    @ApiModelProperty("抢单转派单时间,单位分钟")
    private Integer dispatchOrdersDuration;

    @ApiModelProperty("抢单超时时间")
    private Integer seizeTimeoutMinutes;

    @ApiModelProperty("派单策略，1：距离优先策略，2：评分优先策略，3：最少接单优先策略")
    private Integer dispatchStrategy;

    /**
     * 未接单用户提醒间隔时间
     */
    @ApiModelProperty("未接单用户提醒间隔时间")
    private Integer noPickUpOrdersWarnDuration;
}
