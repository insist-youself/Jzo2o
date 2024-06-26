package com.jzo2o.orders.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServeStatusEnum {

    NO_ALLOCATION(0, "待分配"),
    NO_SERVED(1,"待服务"),
    SERVING(2,"服务中"),
    SERVE_FINISHED(3, "服务完成"),
    CANCLE(4, "取消");
    private int status;
    private String desc;

    public boolean equals(Integer status) {
        return status != null && status.equals(this.getStatus());
    }
}
