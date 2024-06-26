package com.jzo2o.foundations.config;

import com.jzo2o.common.utils.JwtTool;
import com.jzo2o.foundations.properties.ApplicaitonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author itcast
 */
@Configuration
public class JwtConfiguration {

    @Resource
    private ApplicaitonProperties applicaitonProperties;

    @Bean
    public JwtTool jwtTool() {
        return new JwtTool(applicaitonProperties.getJwtKey());
    }
}
