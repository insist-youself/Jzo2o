//package com.jzo2o.mvc.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.util.Collections;
//
///**
// * 解决apifox拉取api报错问题
// */
//@Configuration
//public class CrosConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        // 跨域配置源
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        //设置跨域的配置信息
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        //1,允许任何来源
//        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
//        //2,允许任何请求头
//        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
//        //3,允许任何方法
//        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
//        //4,允许凭证
//        corsConfiguration.setAllowCredentials(true);
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        return new CorsFilter(source);
//    }
//}
