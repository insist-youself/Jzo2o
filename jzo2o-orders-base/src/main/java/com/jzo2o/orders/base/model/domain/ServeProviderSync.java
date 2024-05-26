package com.jzo2o.orders.base.model.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 机构服务端更新服务时间
 * </p>
 *
 * @author itcast
 * @since 2023-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "serve_provider_sync",autoResultMap = true)
public class ServeProviderSync implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 服务时间段
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> serveTimes;

    /**
     * 接单数
     */
    private Integer acceptanceNum;


}
