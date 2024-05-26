package com.jzo2o.orders.manager.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 服务人员/机构服务数据
 *
 * @author itcast
 * @create 2023/9/15 14:26
 **/
@Data
@ApiModel("服务人员/机构服务数据")
public class ServeProviderServeResDTO {

    @ApiModelProperty("订单id")
    private Long id;

    @ApiModelProperty("服务名称")
    private String serveItemName;

    @ApiModelProperty("机构下属服务人员id")
    private Long institutionStaffId;

    @ApiModelProperty("机构下属服务人员姓名")
    private String institutionStaffName;

    @ApiModelProperty("机构下属服务人员电话")
    private String institutionStaffPhone;

    /**
     * 服务前照片
     */
    @ApiModelProperty("服务前照片")
    private List<String> serveBeforeImgs;

    /**
     * 服务后照片
     */
    @ApiModelProperty("服务后照片")
    private List<String> serveAfterImgs;

    /**
     * 实际支付金额
     */
    @ApiModelProperty("实际支付金额")
    private BigDecimal ordersAmount;

    /**
     * 评分
     */
    @ApiModelProperty("评分")
    private Double score;

    /**
     * 实际服务完结时间
     */
    @ApiModelProperty("实际服务完结时间")
    private LocalDateTime realServeEndTime;
}
