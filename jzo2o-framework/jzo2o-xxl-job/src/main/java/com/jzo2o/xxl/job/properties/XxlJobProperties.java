package com.jzo2o.xxl.job.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "xxl-job")
@Configuration
public class XxlJobProperties {

    private String accessToken;
    private Admin admin;
    private Executor executor;

    @Data
    public static class Admin {
        private String address;
    }

    @Data
    public static class Executor {
        private String appName;
        private String ip;
        private Integer port;
        private String logPath;
        private Integer logRetentionDays;

    }
}