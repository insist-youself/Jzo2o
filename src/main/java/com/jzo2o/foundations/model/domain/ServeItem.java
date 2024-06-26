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
 * 服务表
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("serve_item")
public class ServeItem implements Serializable {
    private static final long serialVersionUID = -6558310077509611789L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 服务编码
     */
    private String code;

    /**
     * 服务类型id
     */
    private Long serveTypeId;

    /**
     * 服务名称
     */
    private String name;

    /**
     * 服务图标
     */
    private String serveItemIcon;

    /**
     * 服务图片
     */
    private String img;

    /**
     * 服务单位
     */
    private Integer unit;

    /**
     * 服务描述
     */
    private String description;

    /**
     * 服务详图
     */
    private String detailImg;

    /**
     * 参考价格
     */
    private BigDecimal referencePrice;

    /**
     * 排序字段
     */
    private Integer sortNum;

    /**
     * 活动状态，0：草稿，1禁用，2启用
     */
    private Integer activeStatus;

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
