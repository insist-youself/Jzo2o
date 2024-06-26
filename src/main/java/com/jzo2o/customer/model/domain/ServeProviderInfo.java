package com.jzo2o.customer.model.domain;

import com.jzo2o.common.model.Location;
import lombok.Data;

import java.util.List;

@Data
public class ServeProviderInfo {
    /**
     * 服务人员或机构同步表
     */
    private Long id;

    /**
     * 服务人类型，2：服务人员，3：机构
     */
    private Integer serveProviderType;

    /**
     * 技能列表
     */
    private List<Long> serveItemIds;

    /**
     * 经纬度
     */
    private Location location;

    /**
     * 城市编码
     */
    private String cityCode;

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
    private List<Integer> serveTimes;

    /**
     * 接单数
     */
    private Integer acceptanceNum;

    /**
     * 首次设置状态，0：未完成，1：已完成设置
     */
    private Integer settingStatus;
    /**
     * 状态，0：正常，1：冻结
     */
    private Integer status;
}
