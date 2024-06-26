package com.jzo2o.xxl.job.config;

import com.jzo2o.common.utils.StringUtils;
import com.jzo2o.xxl.job.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "xxl-job", value = "enable", havingValue = "true")
@EnableConfigurationProperties(XxlJobProperties.class)
public class XxlJobConfiguration {

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties prop) {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        XxlJobProperties.Admin admin = prop.getAdmin();
        if (admin != null && StringUtils.isNotEmpty(admin.getAddress())) {
            xxlJobSpringExecutor.setAdminAddresses(admin.getAddress());
        }
        XxlJobProperties.Executor executor = prop.getExecutor();
        xxlJobSpringExecutor.setAccessToken(prop.getAccessToken());
        if (executor != null) {
            xxlJobSpringExecutor.setIp(executor.getIp());
            xxlJobSpringExecutor.setAppname(executor.getAppName());
            xxlJobSpringExecutor.setPort(executor.getPort());
            xxlJobSpringExecutor.setLogPath(executor.getLogPath());
            xxlJobSpringExecutor.setLogRetentionDays(
                    executor.getLogRetentionDays()
            );
        }

        log.info(">>>>>>>>>>> xxl-job config end.");
        return xxlJobSpringExecutor;
    }
}
