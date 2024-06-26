package com.jzo2o.orders.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author itcast
 */

@Getter
@AllArgsConstructor
public enum OrderPayStatusEnum {
    NO_PAY(2, "未支付"),
    PAY_SUCCESS(4, "支付成功");

    private int status;
    private final String desc;

    public boolean equals(int status) {
        return this.status == status;
    }


}
