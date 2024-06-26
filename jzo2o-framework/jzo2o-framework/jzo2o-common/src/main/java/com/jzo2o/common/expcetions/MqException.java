package com.jzo2o.common.expcetions;

import lombok.Data;

/**
 * @author itcast
 */
@Data
public class MqException extends CommonException{

    /**
     * mq失败消息
     */
    private String msg;
    private Long mqId;

    public MqException() {
    }

    public MqException(int code, String message) {
        super(code, message);
    }

    public MqException(Throwable throwable, int code, String message) {
        super(throwable, code, message);
    }

    public MqException(String message) {
        super(message);
    }
}
