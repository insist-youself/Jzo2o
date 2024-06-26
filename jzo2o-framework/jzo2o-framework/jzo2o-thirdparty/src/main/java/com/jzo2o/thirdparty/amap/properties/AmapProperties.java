package com.jzo2o.thirdparty.amap.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author itcast
 */
@Component
@ConfigurationProperties(prefix = "amap")
@ConditionalOnProperty(prefix = "amap", name = "enable", havingValue = "true")
@Data
public class AmapProperties {
    private String key;
}