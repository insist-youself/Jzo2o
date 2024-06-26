package com.jzo2o.api.orders.dto.request;

import com.jzo2o.common.model.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单分页查询请求
 *
 * @author itcast
 * @create 2023/7/20 21:17
 **/
@Data
@ApiModel("订单分页查询请求")
public class OrderPageQueryReqDTO extends PageQueryDTO {
    /**
     * 订单状态，0：待支付，100：派单中，200：待服务，300：服务中，400：待评价，500：订单完成，600：订单取消
     */
    @ApiModelProperty("订单状态，0：待支付，100：派单中，200：待服务，300：服务中，400：待评价，500：订单完成，600：订单取消")
    private Integer ordersStatus;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long ordersId;

    /**
     * 客户电话
     */
    @ApiModelProperty("客户电话")
    private String contactsPhone;

    /**
     * 最小创建时间
     */
    @ApiModelProperty("最小创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime minCreateTime;

    /**
     * 最大创建时间
     */
    @ApiModelProperty("最大创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime maxCreateTime;

    /**
     * 订单id列表
     */
    @ApiModelProperty("订单id列表")
    private List<Long> ordersIdList;
}
