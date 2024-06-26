package com.jzo2o.statemachine.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 状态持久化
 *
 * @author itcast
 * @create 2023/8/5 15:30
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("state_persister")
public class StatePersister {
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
     * 状态,取StatusDefine中的 code
     */
    private String state;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
