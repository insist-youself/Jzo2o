package com.jzo2o.market.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CouponStatusEnum {
    NO_USE(1, "未使用"), USED(2, "已使用"), INVALID(3, "已失效"),VOIDED(4,"已作废");
    private int status;
    private String name;
}
