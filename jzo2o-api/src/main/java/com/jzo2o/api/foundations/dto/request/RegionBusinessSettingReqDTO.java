package com.jzo2o.api.foundations.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("区域业务设置请求DTO")
public class RegionBusinessSettingReqDTO {
    @ApiModelProperty("区域id")
    private Long id;
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

    /**
     * 未接单用户提醒间隔时间
     */
    @ApiModelProperty("未接单用户提醒间隔时间")
    private Integer noPickUpOrdersWarnDuration;

}
