package com.jzo2o.foundations.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 城市编码表
 * </p>
 *
 * @author itcast
 * @since 2023-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("city_directory")
public class CityDirectory implements Serializable {
    private static final long serialVersionUID = -1543919563486396187L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 父级城市编码
     */
    private String parentCode;

    /**
     * 城市类型，1：省份。2：市级
     */
    private String type;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 排序字段
     */
    private Integer sortNum;

    /**
     * 城市名称拼音首字母
     */
    private String pinyinInitial;
}
