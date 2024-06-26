package com.jzo2o.foundations.model.dto.request;

import lombok.Data;

/**
 * 服务同步表更新
 *
 * @author itcast
 * @create 2023/8/1 19:06
 **/
@Data
public class ServeSyncUpdateReqDTO {
    /**
     * 服务类型名称
     */
    private String serveTypeName;

    /**
     * 服务类型图片
     */
    private String serveTypeImg;

    /**
     * 服务类型图标
     */
    private String serveTypeIcon;

    /**
     * 服务类型排序
     */
    private Integer serveTypeSortNum;

    /**
     * 服务项名称
     */
    private String serveItemName;

    /**
     * 服务项图片
     */
    private String serveItemImg;

    /**
     * 服务项图标
     */
    private String serveItemIcon;

    /**
     * 服务项排序
     */
    private Integer serveItemSortNum;

    /**
     * 服务收费价格单位
     */
    private Integer unit;

}
