package com.jzo2o.interceptor;

import com.jzo2o.common.constants.HeaderConstants;
import com.jzo2o.common.handler.RequestIdHandler;
import com.jzo2o.common.handler.UserInfoHandler;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.common.utils.Base64Utils;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.JsonUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

/**
 * 向下一个服务传递参数，用户信息、访问来源、访问id
 */import static com.jzo2o.common.constants.HeaderConstants.*;

/**
 * @author itcast
 */
@Slf4j
@ConditionalOnBean(value = {UserInfoHandler.class, RequestIdHandler.class})
public class FeignInterceptor implements RequestInterceptor {
    private final UserInfoHandler userInfoHandler;
    private final RequestIdHandler requestIdHandler;

    public FeignInterceptor(UserInfoHandler userInfoHandler, RequestIdHandler requestIdHandler) {
        this.userInfoHandler = userInfoHandler;
        this.requestIdHandler = requestIdHandler;
    }

    /**
     * 将用户信息base64格式编码，传递到下一个微服务
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
//         1.用户信息
        CurrentUserInfo userInfo = userInfoHandler.currentUserInfo();
        String userInfoStr = Base64Utils.encodeStr(JsonUtils.toJsonStr(userInfo));
        requestTemplate.header(USER_INFO, userInfoStr);
        // 2.访问来源信息
        requestTemplate.header(HeaderConstants.REQUEST_ORIGIN_FLAG, REQUEST_ORIGIN_FLAG_INNER);
        // 3.访问id
        requestTemplate.header(REQUEST_ID, requestIdHandler.getRequestId());

    }
}
