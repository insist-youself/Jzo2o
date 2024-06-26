package com.jzo2o.api.foundations.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServeAggregationResDTO {
    /**
     * 服务id
     */
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
     * 售卖状态，0：草稿，1下架，2上架
     */
    private Integer saleStatus;

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
