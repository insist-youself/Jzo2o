package com.jzo2o.common.expcetions;

import static com.jzo2o.common.constants.ErrorInfo.Msg.REQUEST_FAILD;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

/**
 * 请求异常，
 * 使用场景：请求参数不合法，频繁请求
 *
 * @author itcast
 */
public class BadRequestException extends CommonException {

    public BadRequestException() {
        this(REQUEST_FAILD);
    }

    public BadRequestException(String message) {
        super(HTTP_BAD_REQUEST, message);
    }

    public BadRequestException(Throwable throwable, String message) {
        super(throwable, HTTP_BAD_REQUEST, message);
    }

    public BadRequestException(Throwable throwable) {
        super(throwable, HTTP_BAD_REQUEST, REQUEST_FAILD);
    }
}
