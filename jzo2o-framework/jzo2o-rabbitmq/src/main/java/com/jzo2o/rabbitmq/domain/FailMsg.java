package com.jzo2o.rabbitmq.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 失败消息
 * </p>
 *
 * @author itcast
 * @since 2023-07-11
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FailMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息id
     */
    private Long id;

    /**
     * 交换机
     */
    private String exchange;

    /**
     * 路由key
     */
    private String routingKey;

    /**
     * 消息
     */
    private String msg;

    /**
     * 原因
     */
    private String reason;

    /**
     * 延迟消息执行时间
     */
    private Integer delayMsgExecuteTime;

    /**
     * 下次拉取时间
     */
    private Integer nextFetchTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
