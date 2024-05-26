package com.jzo2o.orders.base.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务任务
 * </p>
 *
 * @author itcast
 * @since 2023-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "orders_serve",autoResultMap = true)
public class OrdersServe implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 属于哪个用户
     */
    private Long userId;

    /**
     * 服务人员或服务机构id
     */
    private Long serveProviderId;

    /**
     * 服务者类型，2：服务端服务，3：机构端服务
     */
    private Integer serveProviderType;

    /**
     * 机构服务人员id
     */
    private Long institutionStaffId;

    /**
     * 订单id
     */
    private Long ordersId;

    /**
     * 订单来源类型，1：抢单，2：派单
     */
    private Integer ordersOriginType;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 服务分类id
     */
    private Long serveTypeId;

    /**
     * 预约时间
     */
    private LocalDateTime serveStartTime;

    /**
     * 服务项id
     */
    private Long serveItemId;

    /**
     * 服务图片地址
     */
    private String serveItemImg;

    /**
     * 任务状态
     */
    private Integer serveStatus;

    /**
     * 结算状态，0：不可结算，1：待结算，2：结算完成
     */
    private Integer settlementStatus;

    /**
     * 实际服务开始时间
     */
    private LocalDateTime realServeStartTime;

    /**
     * 实际服务完结时间
     */
    private LocalDateTime realServeEndTime;

    /**
     * 服务前照片
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> serveBeforeImgs;

    /**
     * 服务后照片
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> serveAfterImgs;

    /**
     * 服务前说明
     */
    private String serveBeforeIllustrate;

    /**
     * 服务后说明
     */
    private String serveAfterIllustrate;

    /**
     * 取消时间,可以是退单，可以是取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 订单金额
     */
    private BigDecimal ordersAmount;

    /**
     * 购买数量
     */
    private Integer purNum;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 排序字段（serve_start_time（秒级时间戳）+订单id（后6位））
     */
    private Long sortBy;

    /**
     * 服务端/机构端是否展示，1：展示，0：隐藏
     */
    private Integer display;


    /**
     * 更新人
     */
    private Long updateBy;


}
