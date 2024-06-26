package com.jzo2o.orders.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author itcast
 */

@Getter
@AllArgsConstructor
public enum OrderRefundStatusEnum {
//    APPLY_REFUND(0, "发起退款"),
    REFUNDING(1, "退款中"),
    REFUND_SUCCESS(2, "退款成功"),
    REFUND_FAIL(3, "退款失败");

    private int status;
    private final String desc;

    public boolean equals(int status) {
        return this.status == status;
    }


}
