package com.jzo2o.foundations.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务类型表
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("serve_type")
public class ServeType implements Serializable {
    private static final long serialVersionUID = 9096692500357281141L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 服务类型编码
     */
    private String code;

    /**
     * 服务类型名称
     */
    private String name;

    /**
     * 服务类型图标
     */
    private String serveTypeIcon;

    /**
     * 服务类型图片
     */
    private String img;

    /**
     * 排序字段
     */
    private Integer sortNum;

    /**
     * 活动状态，0：草稿，1：禁用，:2：启用
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
