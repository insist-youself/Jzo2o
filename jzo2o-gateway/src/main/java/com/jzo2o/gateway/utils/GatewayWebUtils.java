package com.jzo2o.gateway.utils;

import cn.hutool.core.lang.ObjectId;
import com.jzo2o.common.utils.*;
import com.jzo2o.gateway.model.Result;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * @author 86188
 */
public class GatewayWebUtils {

    /**
     * 网关请求输出数据
     *
     * @param exchange
     * @param code
     * @param message
     * @return
     */
    public static Mono<Void> toResponse(ServerWebExchange exchange, int code, String message) {
        // 设置返回码
        exchange.getResponse()
                .setStatusCode(HttpStatus.OK);
        // 返回信息
        Result result = Result.error(code, message);

        DataBuffer dataBuffer = exchange.getResponse()
                .bufferFactory()
                .wrap(JsonUtils.toJsonStr(result)
                        .getBytes(StandardCharsets.UTF_8));
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");

        return exchange.getResponse()
                .writeWith(Flux.just(dataBuffer));
    }

    /**
     * 比较method方法是否相同
     *
     * @param exchange
     * @param httpMethod
     * @return
     */
    public static boolean methodEqual(ServerWebExchange exchange, HttpMethod httpMethod) {
        return getHttpMethtod(exchange).compareTo(httpMethod) == 0;
    }

    /**
     * 根据header名称获取header值
     *
     * @param exchange
     * @param headerName
     * @return
     */
    public static String getRequestHeader(ServerWebExchange exchange, String headerName) {
        return exchange.getRequest()
                .getHeaders()
                .getFirst(headerName);
    }

    public static String getRequestParameter(ServerWebExchange exchange, String parameterName) {
        return exchange.getRequest()
                .getQueryParams()
                .getFirst(parameterName);
    }

    public static String getUri(ServerWebExchange exchange) {
        URI uri = exchange.getRequest().getURI();
        return uri.getPath();

    }
    /**
     * 请求header中设置
     *
     * @param exchange
     * @param headerName
     * @param headerValue
     */
    public static void setRequestHeader(ServerWebExchange exchange, String headerName, String headerValue) {
        exchange.mutate()
                .request(builder -> builder.header(headerName, headerValue))
                .build();
    }

    /**
     * 设置多个请求header
     *
     * @param exchange
     * @param headers  headers中包含header名称和header值，headers的数量是偶数，header名称在前，header值在后，该字段不能传控
     */
    public static void setManyRequestHeader(ServerWebExchange exchange, String... headers) {

        ServerWebExchange.Builder mutate = exchange.mutate();
        for (int count = 0; count < ArrayUtils
                .length(headers); count += 2) {
            String headerName = headers[count];
            String headerValue = headers[count + 1];
            mutate.request(builder -> builder.header(headerName, headerValue));
        }
        mutate.build();
    }

    /**
     * 获取访问地址授权路径，例如get /evaluations/1,路径为EVALUATIONS:GET
     * 注意：路径中的数字，mongo生成id不作为权限配置的部分
     *
     * @param exchange
     * @return
     */
    public static String getAuthPath(ServerWebExchange exchange) {
        // 1.请求路径
        List<PathContainer.Element> pathElements = exchange.getRequest().getPath().elements();
        StringBuffer buffer = new StringBuffer();
        // 2.路径遍历（从后向前遍历），且拼接成权限路径
        pathElements = CollUtils.reverse(new ArrayList<>(pathElements));
        pathElements.stream()
                // 过滤掉/、id（id是mongo自动生成的）和纯数字
                .filter(element -> !element.value().equals(StringUtils.SLASH) && !ObjectId.isValid(element.value()) && !NumberUtils.isNumber(element.value()))
                .forEach(element -> buffer.insert(0, StringUtils.DOT).insert(0, element.value()));

        if (buffer.length() > 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        // 3.将请求方法拼接到权限路径中
        buffer.append(StringUtils.COLON)
                .append(getMethod(exchange));
        return buffer.toString();
    }

    /**
     * 获取请求httpmethod
     *
     * @param exchange
     * @return
     */
    public static HttpMethod getHttpMethtod(ServerWebExchange exchange) {
        return exchange.getRequest()
                .getMethod();
    }

    /**
     * 获取字符串请求方法
     *
     * @param exchange
     * @return
     */
    public static String getMethod(ServerWebExchange exchange) {
        return getHttpMethtod(exchange)
                .toString();
    }

}
