package com.jzo2o.api.orders.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单聚合响应数据
 *
 * @author itcast
 * @create 2023/7/20 21:19
 **/
@Data
@ApiModel("订单聚合响应数据")
public class OrderAggregationResDTO {
    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long id;

    /**
     * 订单编码
     */
    @Deprecated
    @ApiModelProperty("订单编码")
    private String ordersCode;

    /**
     * 服务项id
     */
    @ApiModelProperty("服务项id")
    private Long serveItemId;

    /**
     * 服务项名称
     */
    @ApiModelProperty("服务项名称")
    private String serveItemName;

    /**
     * 服务单位
     */
    @ApiModelProperty("服务单位")
    private Integer unit;

    /**
     * 服务id
     */
    @ApiModelProperty("服务id")
    private Long serveId;

    /**
     * 订单状态，0：待支付，100：派单中，200：待服务，300：服务中，400：待评价，500：订单完成，600：订单取消，700已退单
     */
    @ApiModelProperty("订单状态，0：待支付，100：派单中，200：待服务，300：服务中，400：待评价，500：订单完成，600：订单取消，700已退单")
    private Integer ordersStatus;

    @ApiModelProperty("支付状态，0：待支付，1：支付成功")
    private Integer payStatus;

    @ApiModelProperty("退款状态，0：发起退款，1：退款中，2：退款成功  3：退款失败")
    private Integer refundStatus;
    /**
     * 单价
     */
    @ApiModelProperty("单价")
    private BigDecimal price;

    /**
     * 购买数量
     */
    @ApiModelProperty("购买数量")
    private Integer purNum;

    /**
     * 实际支付金额
     */
    @ApiModelProperty("实际支付金额")
    private BigDecimal realPayAmount;

    /**
     * 服务详细地址
     */
    @ApiModelProperty("服务详细地址")
    private String serveAddress;

    /**
     * 联系人手机号
     */
    @ApiModelProperty("联系人手机号")
    private String contactsPhone;

    /**
     * 联系人姓名
     */
    @ApiModelProperty("联系人姓名")
    private String contactsName;

    /**
     * 服务人id，机构接单则该id为机构下属服务人员
     */
    @ApiModelProperty("服务人id，机构接单则该id为机构下属服务人员")
    private String serverId;

    /**
     * 服务人姓名
     */
    @ApiModelProperty("服务人姓名")
    private String serverName;

    /**
     * 服务开始时间
     */
    @ApiModelProperty("服务开始时间")
    private LocalDateTime serveStartTime;

    /**
     * 服务结束时间
     */
    @ApiModelProperty("服务结束时间")
    private LocalDateTime serveEndTime;

    /**
     * 服务实际开始时间
     */
    @ApiModelProperty("服务实际开始时间")
    private LocalDateTime realServeStartTime;

    /**
     * 服务实际结束时间
     */
    @ApiModelProperty("服务实际结束时间")
    private LocalDateTime realServeEndTime;

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
     * 服务前说明
     */
    @ApiModelProperty("服务前说明")
    private String serveBeforeIllustrate;

    /**
     * 服务后说明
     */
    @ApiModelProperty("服务后说明")
    private String serveAfterIllustrate;

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

    /**
     * 订单进度条
     */
    @ApiModelProperty("订单进度条")
    private List<OrderProgress> orderProgressList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderProgress {
        /**
         * 订单状态，0：待支付，100：派单中，200：待服务，300：服务中，400：待评价，500：订单完成，600：订单取消，700已退单
         */
        @ApiModelProperty("订单状态，0：待支付，100：派单中，200：待服务，300：服务中，400：待评价，500：订单完成，600：订单取消，700已退单")
        private Integer status;

        /**
         * 时间
         */
        @ApiModelProperty("时间")
        private LocalDateTime dateTime;
    }
}
