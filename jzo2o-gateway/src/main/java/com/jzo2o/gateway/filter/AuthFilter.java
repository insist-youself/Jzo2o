package com.jzo2o.gateway.filter;

import com.jzo2o.common.constants.ErrorInfo;
import com.jzo2o.common.constants.HeaderConstants;
import com.jzo2o.gateway.utils.GatewayWebUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.jzo2o.gateway.constants.UserConstants.X_USER_TYPE;

/**
 * nginx根据不同的端的访问地址在header头上标记出用户的类型，和token中的用户类型进行对比
 */
public class AuthFilter implements GatewayFilter {

    public AuthFilter() {
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 来自token的用户类型
        String userType = GatewayWebUtils.getRequestHeader(exchange, HeaderConstants.USER_TYPE);
        // 来自网关的用户类型
        String xUserType = GatewayWebUtils.getRequestHeader(exchange, X_USER_TYPE);
        // 对比两个用户类型是否一致
        if (xUserType.equals(userType)) {
            return chain.filter(exchange);
        }
        //无访问权限返回
        return GatewayWebUtils.toResponse(exchange,
                ErrorInfo.Code.NO_PERMISSIONS,
                ErrorInfo.Msg.NO_PERMISSIONS);

    }
}
