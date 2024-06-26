//package com.jzo2o.mvc.config;
//
//import com.jzo2o.mvc.advice.CommonExceptionAdvice;
//import com.jzo2o.mvc.interceptor.UserContextInteceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * @author itcast
// */
//@Configuration
//public class UserContextConfiguration implements WebMvcConfigurer {
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // token拦截器
//        registry.addInterceptor(new UserContextInteceptor())
//                .addPathPatterns("/**");
//    }
//
//    @Bean
//    public CommonExceptionAdvice commonExceptionAdvice() {
//        return new CommonExceptionAdvice();
//    }
//}
