package com.jzo2o.customer.model.domain;

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
 * 服务人员/机构附属信息
 * </p>
 *
 * @author itcast
 * @since 2023-07-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("serve_provider_settings")
public class ServeProviderSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 服务人员/机构id
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 城市码
     */
    private String cityCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 经度
     */
    private Double lon;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 意向单范围
     */
    private String intentionScope;

    /**
     * 是否有技能
     */
    private Integer haveSkill;

    /**
     * 是否可以接单，0：关闭接单，1：开启接单
     */
    private Integer canPickUp;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer isDeleted;


}
