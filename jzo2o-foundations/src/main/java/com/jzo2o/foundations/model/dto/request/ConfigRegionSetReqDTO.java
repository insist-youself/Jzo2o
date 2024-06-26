package com.jzo2o.foundations.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("区域配置")
@Data
public class ConfigRegionSetReqDTO {

    /**
     * 区域id
     */
    @ApiModelProperty(value = "区域id",required = true)
    private Long id;
    /**
     * 城市编码
     */
    @ApiModelProperty(value = "区域编码",required = true)
    private String cityCode;

    /**
     * （个体）接单量限制
     */
    @ApiModelProperty(value = "区域个人接单数量限制（单位个）",required = true)
    private Integer staffReceiveOrderMax;

    /**
     * （企业）接单量限制值
     */
    @ApiModelProperty(value = "区域企业接单数量限制，（单位个）",required = true)
    private Integer institutionReceiveOrderMax;

    /**
     * （个体）服务范围半径
     */
    @ApiModelProperty(value = "个人服务半径，（单位km）",required = true)
    private Integer staffServeRadius;

    /**
     * （企业）服务范围半径
     */
    @ApiModelProperty(value = "企业服务半径（单位km）",required = true)
    private Integer institutionServeRadius;

    /**
     * 分流间隔（单位分钟），即下单时间与服务预计开始时间的间隔
     */
    @ApiModelProperty(value = "分流间隔（单位分钟），即下单时间与服务预计开始时间的间隔",required = true)
    private Integer diversionInterval;

    /**
     * 抢单超时时间间隔（单位分钟），从支付成功进入抢单后超过当前时间抢单派单同步进行
     */
    @ApiModelProperty(value = "抢单超时时间间隔（单位分钟）",required = true)
    private Integer seizeTimeoutInterval;

    /**
     * 派单策略，1：距离优先策略，2：评分优先策略，3：接单量优先策略
     */
    @ApiModelProperty(value = "派单策略，1：距离优先策略，2：评分优先策略，3：接单量优先策略",required = true)
    private Integer dispatchStrategy;

    /**
     * 派单每轮时间间隔,（单位s）
     */
    @ApiModelProperty(value = "派单每轮时间间隔,（单位s）",required = true)
    private Integer dispatchPerRoundInterval;
}
