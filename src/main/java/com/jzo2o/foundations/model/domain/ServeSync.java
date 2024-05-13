package com.jzo2o.foundations.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 服务同步表
 * </p>
 *
 * @author itcast
 * @since 2023-07-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("serve_sync")
public class ServeSync implements Serializable {
    private static final long serialVersionUID = 7506867018480258143L;

    /**
     * 服务id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 服务项名称
     */
    private String serveItemName;

    /**
     * 服务类型id
     */
    private Long serveTypeId;

    /**
     * 服务项id
     */
    private Long serveItemId;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 是否是热门
     */
    private Integer isHot;

    /**
     * 更新为热门的时间戳
     */
    private Long hotTimeStamp;

    /**
     * 服务项排序字段
     */
    private Integer serveItemSortNum;

    /**
     * 服务类型排序字段
     */
    private Integer serveTypeSortNum;

    /**
     * 服务类型名称
     */
    private String serveTypeName;

    /**
     * 服务类型图片
     */
    private String serveTypeImg;

    /**
     * 服务类型icon
     */
    private String serveTypeIcon;

    /**
     * 服务收费价格单位
     */
    private Integer unit;

    /**
     * 服务详情图片
     */
    private String detailImg;

    /**
     * 服务项图片
     */
    private String serveItemImg;

    /**
     * 服务图标
     */
    private String serveItemIcon;


}
