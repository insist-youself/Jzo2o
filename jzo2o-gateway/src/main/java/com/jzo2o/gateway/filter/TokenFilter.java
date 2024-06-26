package com.jzo2o.gateway.filter;


import cn.hutool.core.util.IdUtil;
import com.jzo2o.common.constants.ErrorInfo;
import com.jzo2o.common.constants.HeaderConstants;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.common.utils.Base64Utils;
import com.jzo2o.common.utils.JsonUtils;
import com.jzo2o.common.utils.JwtTool;
import com.jzo2o.common.utils.StringUtils;
import com.jzo2o.gateway.properties.ApplicationProperties;
import com.jzo2o.gateway.utils.GatewayWebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * token解析过滤器
 *
 * @author 86188
 */
@Slf4j
public class TokenFilter implements GatewayFilter {

    /**
     * token header名称
     */
    private static final String HEADER_TOKEN = "Authorization";

    private ApplicationProperties applicationProperties;

    public TokenFilter(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 1.黑名单和白名单校验
        // 1.1.黑名单校验
        String uri = GatewayWebUtils.getUri(exchange);
        log.info("uri : {}", uri);


        if (applicationProperties.getAccessPathBlackList().contains(uri)) {
            return GatewayWebUtils.toResponse(exchange,
                    HttpStatus.FORBIDDEN.value(),
                    ErrorInfo.Msg.REQUEST_FORBIDDEN);
        }
        // 1.2.访问白名单
        if (applicationProperties.getAccessPathWhiteList().contains(uri)) {
            return chain.filter(exchange);
        }

        // 2.获取token解析工具JwtTool工具
        // 2.1.获取token
        String token = GatewayWebUtils.getRequestHeader(exchange, HEADER_TOKEN);
        if (StringUtils.isEmpty(token)) {
            return GatewayWebUtils.toResponse(exchange,
                    HttpStatus.FORBIDDEN.value(),
                    ErrorInfo.Msg.REQUEST_FORBIDDEN);
        }
        // 2.2.获取tokenKey
        String tokenKey = applicationProperties.getTokenKey().get(JwtTool.getUserType(token) + "");
        if (StringUtils.isEmpty(token)) {
            return GatewayWebUtils.toResponse(exchange,
                    HttpStatus.FORBIDDEN.value(),
                    ErrorInfo.Msg.REQUEST_FORBIDDEN);
        }
        // 2.3.新建toekn解析工具对象jwtToken
        JwtTool jwtTool = new JwtTool(tokenKey);

        // 3.token解析
        CurrentUserInfo currentUserInfo = jwtTool.parseToken(token);
        if (currentUserInfo == null) {
            return GatewayWebUtils.toResponse(exchange,
                    HttpStatus.FORBIDDEN.value(), "登录过期或访问被拒绝");
        }
        // 4.用户id和用户类型向后传递
        String userInfo = Base64Utils.encodeStr(JsonUtils.toJsonStr(currentUserInfo));

        // 4.1.设置用户信息向下传递
        GatewayWebUtils.setRequestHeader(exchange,
                HeaderConstants.USER_INFO, userInfo);
        // 4.2.设置用户类型向下传递
        GatewayWebUtils.setRequestHeader(exchange,
                HeaderConstants.USER_TYPE, currentUserInfo.getUserType() + "");
        // 4.3.请求id
        GatewayWebUtils.setRequestHeader(exchange, HeaderConstants.REQUEST_ID, IdUtil.getSnowflakeNextIdStr());
        // 4.请求放行
        return chain.filter(exchange);
    }
}
