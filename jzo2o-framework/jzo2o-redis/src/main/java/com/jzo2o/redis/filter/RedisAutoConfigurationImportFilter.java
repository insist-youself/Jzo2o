package com.jzo2o.redis.filter;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;

/**
 * 强制排除自动装箱的StringRedisTemplate和RedisTemplate两个模板类，使用自定义的RedisTemplate
 */
public class RedisAutoConfigurationImportFilter implements AutoConfigurationImportFilter {

    private static final String REDIS_AUTO_CONFIGURATION_CLASS_PATH = "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration";

    @Override
    public boolean[] match(String[] autoConfigurationClasseNames, AutoConfigurationMetadata autoConfigurationMetadata) {
        boolean[] matches = new boolean[autoConfigurationClasseNames.length];

        for (int count = 0; count < autoConfigurationClasseNames.length; count++) {
            String className = autoConfigurationClasseNames[count];
            matches[count] = !REDIS_AUTO_CONFIGURATION_CLASS_PATH.equals(className);
        }
        return matches;
    }
}
