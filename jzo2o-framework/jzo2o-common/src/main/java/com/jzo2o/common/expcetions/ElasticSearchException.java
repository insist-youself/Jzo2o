package com.jzo2o.common.expcetions;

/**
 * @author itcast
 */
public class ElasticSearchException extends CommonException {
    public ElasticSearchException() {
    }

    public ElasticSearchException(int code, String message) {
        super(code, message);
    }

    public ElasticSearchException(Throwable throwable, int code, String message) {
        super(throwable, code, message);
    }

    public ElasticSearchException(String message) {
        super(message);
    }
}
