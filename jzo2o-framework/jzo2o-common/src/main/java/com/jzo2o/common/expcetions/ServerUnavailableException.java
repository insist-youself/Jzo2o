package com.jzo2o.common.expcetions;

import static com.jzo2o.common.constants.ErrorInfo.Msg.PROCESS_FAILD;
import static java.net.HttpURLConnection.HTTP_UNAVAILABLE;

/**
 * 服务不可用，注册中心找不到对应服务
 *
 * @author itcast
 */
public class ServerUnavailableException extends CommonException {
    public ServerUnavailableException() {
        this(PROCESS_FAILD);
    }

    public ServerUnavailableException(String message) {
        super(HTTP_UNAVAILABLE, message);
    }

    public ServerUnavailableException(Throwable throwable, String message) {
        super(throwable, HTTP_UNAVAILABLE, message);
    }

    public ServerUnavailableException(Throwable throwable) {
        super(throwable, HTTP_UNAVAILABLE, PROCESS_FAILD);
    }
}
