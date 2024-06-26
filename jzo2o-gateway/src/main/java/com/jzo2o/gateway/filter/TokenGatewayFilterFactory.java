package com.jzo2o.gateway.filter;

import com.jzo2o.gateway.properties.ApplicationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 86188
 */
@Component
public class TokenGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Resource
    private ApplicationProperties applicationProperties;

    public TokenGatewayFilterFactory() {
    }

    @Override
    public GatewayFilter apply(Object config) {
        return new TokenFilter(applicationProperties);
    }
}
