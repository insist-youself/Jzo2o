package com.jzo2o.common.model;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.nio.charset.StandardCharsets;

@Data
public class Result<T> {

    public static final int SUCCESS = 200;
    public static final String OK = "OK";
    public static final int FAILED = 1;

    public static final byte[] OK_BYTES =
            String.format("{\"code\":%d,\"msg\":\"%s\",\"data\": {}}", SUCCESS, OK).getBytes(StandardCharsets.UTF_8);
    public static final byte[] OK_PREFIX =
            String.format("{\"code\":%d,\"msg\":\"%s\",\"data\": ", SUCCESS, OK).getBytes(StandardCharsets.UTF_8);
    public static final byte[] OK_SUFFIX = "}".getBytes(StandardCharsets.UTF_8);
    public static final byte[] OK_STR_PREFIX =
            String.format("{\"code\":%d,\"msg\":\"%s\",\"data\":", SUCCESS, OK).getBytes(StandardCharsets.UTF_8);
    public static final byte[] OK_STR_SUFFIX = "}".getBytes(StandardCharsets.UTF_8);
    public static final String REQUEST_OK = "OK";

    @ApiModelProperty(value = "业务状态码，200-成功，其它-失败")
    private int code;
    @ApiModelProperty(value = "响应消息", example = "OK")
    private String msg;
    @ApiModelProperty(value = "响应数据")
    private T data;
    @ApiModelProperty(value = "请求id", example = "1af123c11412e")
    private String requestId;

    public static Result<Void> ok() {
        return new Result<Void>(HttpStatus.HTTP_OK, REQUEST_OK, null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(HttpStatus.HTTP_OK, REQUEST_OK, data);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(HttpStatus.HTTP_BAD_REQUEST, msg, null);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    public Result() {
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean success() {
        return code == HttpStatus.HTTP_OK;
    }

    public static byte[] plainOk() {
        return OK_BYTES;
    }

    public static byte[] plainOk(byte[] data) {
        if(data == null || data.length <= 0){
            return OK_BYTES;
        }
        byte b = data[0];
        if (b == 91 || b == 123) {
            return ArrayUtil.addAll(OK_PREFIX, data, OK_SUFFIX);
        }
        return ArrayUtil.addAll(OK_STR_PREFIX, data, OK_STR_SUFFIX);
    }
}
