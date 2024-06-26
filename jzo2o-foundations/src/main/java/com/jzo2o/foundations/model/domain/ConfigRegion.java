package com.jzo2o.foundations.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 区域业务配置
 * </p>
 *
 * @author itcast
 * @since 2023-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 区域id
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * （个体）接单量限制
     */
    private Integer staffReceiveOrderMax;

    /**
     * （企业）接单量限制值
     */
    private Integer institutionReceiveOrderMax;

    /**
     * （个体）服务范围半径
     */
    private Integer staffServeRadius;

    /**
     * （企业）服务范围半径
     */
    private Integer institutionServeRadius;

    /**
     * 分流间隔（单位分钟），即下单时间与服务预计开始时间的间隔
     */
    private Integer diversionInterval;

    /**
     * 抢单超时时间间隔（单位分钟），从支付成功进入抢单后超过当前时间抢单派单同步进行
     */
    private Integer seizeTimeoutInterval;

    /**
     * 派单策略，1：距离优先策略，2：评分优先策略，3：接单量优先策略
     */
    private Integer dispatchStrategy;

    /**
     * 派单每轮时间间隔
     */
    private Integer dispatchPerRoundInterval;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createBy;

    private Long updateBy;


}
