package com.jzo2o.foundations.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务表
 * </p>
 *
 * @author linger
 * @since 2024-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("serve")
public class Serve implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 服务id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 服务项id
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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 更新者
     */
    private Long updateBy;


}
