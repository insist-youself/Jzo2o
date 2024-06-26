package com.jzo2o.gateway.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "jzo2o")
@Data
@Slf4j
public class ApplicationProperties {

    /**
     * 每一个端都要配置一个token解析key
     * “1”：xxx c端用户token生成key
     * "2": xxx 服务端用户token生成key
     * "3": xxx 机构端用户token生成key
     * "4": xxx 运营端用户token生成key
     * tokenkey
     */
    @NestedConfigurationProperty
    private final Map<String,String> tokenKey = new HashMap<>();

    /**
     * 访问路径地址白名单
     */
    @NestedConfigurationProperty
    private List<String> accessPathWhiteList = new ArrayList<>();

    /**
     * 访问路径黑名单
     */
    @NestedConfigurationProperty
    private List<String> accessPathBlackList = new ArrayList<>();

}
