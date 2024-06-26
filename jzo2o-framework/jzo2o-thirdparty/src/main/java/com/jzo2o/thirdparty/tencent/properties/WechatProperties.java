
package com.jzo2o.thirdparty.tencent.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author itcast
 */
@Component
@ConfigurationProperties(prefix = "tencent.wechat")
@ConditionalOnProperty(prefix = "tencent.wechat", name = "enable", havingValue = "true")
@Data
public class WechatProperties {

    private String appId;
    private String secret;
}