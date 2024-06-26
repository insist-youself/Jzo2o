package com.jzo2o.orders.base.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 违约记录
 * </p>
 *
 * @author itcast
 * @since 2023-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("breach_record")
public class BreachRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 违约id
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 违约机构或师傅
     */
    private Long serveProviderId;

    /**
     * 类型，2：师傅、3：机构
     */
    private Integer serveProviderType;

    /**
     * 行为类型，1：待分配时取消，2：待服务时取消，3：服务中取消，4：派单拒绝，5：派单超时
     */
    private Integer behaviorType;

    /**
     * 违约原因
     */
    private String breachReason;

    /**
     * 服务项名称
     */
    private String serveItemName;

    /**
     * 服务地址
     */
    private String serveAddress;

    /**
     * 被服务人
     */
    private Long servedUserId;

    /**
     * 被服务人员手机号，脱敏
     */
    private String servedPhone;

    /**
     * 违约时间
     */
    private LocalDateTime breachTime;

    /**
     * 违约日，格式例如20200512,2020年5月12日
     */
    private Integer breachDay;

    /**
     * 违约单订单id
     */
    private Long ordersId;

    /**
     * 服务单id
     */
    private Long ordersServeId;


}
