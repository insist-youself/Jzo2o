package com.jzo2o.common.expcetions;

import cn.hutool.http.HttpStatus;
import com.jzo2o.common.constants.ErrorInfo;
import lombok.Data;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

/**
 * @author itcast
 */
@Data
public class CommonException extends RuntimeException {
    private int code;
    private String message;

    public CommonException() {
        this.code = HTTP_BAD_REQUEST;
        this.message = ErrorInfo.Msg.PROCESS_FAILD;
    }

    public CommonException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonException(Throwable throwable, int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonException(String message) {
        this(HttpStatus.HTTP_INTERNAL_ERROR, message);
    }

}
