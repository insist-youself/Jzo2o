package com.jzo2o.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 当前用户信息
 */
@Data
@AllArgsConstructor
public class CurrentUserInfo implements Serializable {
    /**
     * 当前用户id
     */
    private Long id;
    /**
     * 用户名/昵称
     */
    private String name;
    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户类型
     */
    private Integer userType;
}
