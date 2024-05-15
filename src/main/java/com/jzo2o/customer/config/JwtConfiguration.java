package com.jzo2o.customer.config;

import com.jzo2o.common.utils.JwtTool;
import com.jzo2o.customer.properties.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class JwtConfiguration {

    @Resource
    private ApplicationProperties applicationProperties;

    @Bean
    public JwtTool jwtTool() {
        return new JwtTool(applicationProperties.getJwtKey());
    }
}
