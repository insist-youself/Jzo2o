package com.jzo2o.mysql.config;

import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PageHelperConfiguration {

    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }
}
