package com.jzo2o.knife4j.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * swagger配置属性
 *
 * @Author itheima
 * @Date 2023/04/06 17:25
 */
@Data
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties implements Serializable {
    private Boolean enableResponseWrap = false;

    /**
     * controller扫描路径
     */
    public String packagePath;

    /**
     * swagger文档标题
     */
    public String title;

    /**
     * 应用描述
     */
    public String description;

    /**
     * 联系人名称
     */
    public String contactName;

    /**
     * 联系人访问地址
     */
    public String contactUrl;

    /**
     * 联系人email
     */
    public String contactEmail;

    /**
     * 版本号
     */
    public String version;
}
