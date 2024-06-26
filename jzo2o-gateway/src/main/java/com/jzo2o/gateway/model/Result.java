package com.jzo2o.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    public static final String REQUEST_OK = "OK";
    private int code;
    private String msg;
    private T data;
    private String requestId;

    public static Result<?> error(int code, String msg) {
        return new Result<>(code, msg, null, null);
    }
}
