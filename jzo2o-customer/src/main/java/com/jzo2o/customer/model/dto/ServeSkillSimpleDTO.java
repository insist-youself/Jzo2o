package com.jzo2o.customer.model.dto;

import lombok.Data;

/**
 * 服务技能简略信息
 *
 * @author itcast
 * @create 2023/9/6 17:12
 **/
@Data
public class ServeSkillSimpleDTO {
    /**
     * 服务类型名称
     */
    private String serveTypeName;

    /**
     * 服务项名称
     */
    private String serveItemName;
}
