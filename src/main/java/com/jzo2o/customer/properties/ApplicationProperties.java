package com.jzo2o.customer.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author itcast
 */
@ConfigurationProperties(prefix = "jzo2o")
@Configuration
@Data
public class ApplicationProperties {

    private String jwtKey;
}
