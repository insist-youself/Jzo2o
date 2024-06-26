package com.jzo2o.foundations.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("serve")
public class Serve implements Serializable {
    private static final long serialVersionUID = -283112689446411326L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 服务id
     */
    private Long serveItemId;

    /**
     * 区域id
     */
    private Long regionId;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 售卖状态，0：草稿，1下架，2上架
     */
    private Integer saleStatus;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 是否为热门，0非热门，1热门
     */
    private Integer isHot;

    /**
     * 更新为热门的时间戳
     */
    private Long hotTimeStamp;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
}
