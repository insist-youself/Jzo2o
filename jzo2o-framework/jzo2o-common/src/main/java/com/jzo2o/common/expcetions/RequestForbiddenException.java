package com.jzo2o.common.expcetions;

import static com.jzo2o.common.constants.ErrorInfo.Msg.REQUEST_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;

/**
 * 权限校验被拒
 *
 * @author itheima
 */
public class RequestForbiddenException extends CommonException{

    public RequestForbiddenException() {
        this(REQUEST_FORBIDDEN);
    }

    public RequestForbiddenException(String message) {
        super(HTTP_FORBIDDEN, message);
    }

    public RequestForbiddenException(Throwable throwable, String message) {
        super(throwable, HTTP_FORBIDDEN, message);
    }

    public RequestForbiddenException(Throwable throwable) {
        super(throwable, HTTP_FORBIDDEN, REQUEST_FORBIDDEN);
    }

}
