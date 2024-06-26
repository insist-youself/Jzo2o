package com.jzo2o.customer.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务人员/机构表
 * </p>
 *
 * @author itcast
 * @since 2023-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("serve_provider")
public class ServeProvider implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 类型，2：服务人员，3：服务机构
     */
    private Integer type;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态，0：正常，1：冻结
     */
    private Integer status;

    /**
     * 首次设置状态，0：未完成设置，1：已完成设置
     */
    private Integer settingsStatus;

    /**
     * 机构登录密码
     */
    private String password;

    /**
     * 账号冻结原因
     */
    private String accountLockReason;

    /**
     * 综合评分
     */
    private Double score;

    /**
     * 好评率
     */
    private String goodLevelRate;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否已删除，0：未删除，1：已删除
     */
    private Integer isDeleted;
}
