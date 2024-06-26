package com.jzo2o.statemachine.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * 业务数据快照
 *
 * @author itcast
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("biz_snapshot")
public class BizSnapshot {

    /**
     * 主键
     */
    private Long id;

    /**
     * 状态机名称
     */
    private String stateMachineName;

    /**
     * 业务id
     */
    private String bizId;

    /**
     * 分库键
     */
    private Long dbShardId;

    /**
     * 状态,取StatusDefine中的 code
     */
    private String state;

    /**
     * 业务数据（json格式化）
     */
    private String bizData;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建时间
     */
    private LocalDateTime updateTime;
}
