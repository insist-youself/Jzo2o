package com.jzo2o.common.expcetions;

import static com.jzo2o.common.constants.ErrorInfo.Msg.REQUEST_TIME_OUT;
import static java.net.HttpURLConnection.HTTP_CLIENT_TIMEOUT;

/**
 * 请求超时异常
 *
 * @author itheima
 */
public class RequestTimeoutException extends CommonException {

    public RequestTimeoutException() {
        this(REQUEST_TIME_OUT);
    }

    public RequestTimeoutException(String message) {
        super(HTTP_CLIENT_TIMEOUT, message);
    }

    public RequestTimeoutException(Throwable throwable, String message) {
        super(throwable, HTTP_CLIENT_TIMEOUT, message);
    }

    public RequestTimeoutException(Throwable throwable) {
        super(throwable, HTTP_CLIENT_TIMEOUT, REQUEST_TIME_OUT);
    }

}
