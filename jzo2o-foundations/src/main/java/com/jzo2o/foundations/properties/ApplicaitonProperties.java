package com.jzo2o.foundations.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置，没有明确分类的系统配置
 */
@Configuration
@ConfigurationProperties(prefix = "jzo2o")
@Data
public class ApplicaitonProperties {

    /**
     * jwt 加密秘钥
     */
    private String jwtKey;
}
