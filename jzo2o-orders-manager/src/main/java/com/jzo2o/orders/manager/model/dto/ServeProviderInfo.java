package com.jzo2o.orders.manager.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class ServeProviderInfo {
    /**
     * 服务人员或机构同步表
     */
    private Long id;

    /**
     * 技能列表
     */
    private List<Long> serveItemIds;

    /**
     * 经纬度
     */
    private String location;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 接单距离
     */
    private Double recieveDistance;

    /**
     * 接单开关1，：接单开启，0：接单关闭
     */
    private Integer pickUp;

    /**
     * 评分,默认50分
     */
    private Double evaluationScore;

    /**
     * 服务时间
     */
    private List<String> serveTimes;

    /**
     * 首次设置状态，0：未完成，1：已完成设置
     */
    private Integer setttingStatus;
}
