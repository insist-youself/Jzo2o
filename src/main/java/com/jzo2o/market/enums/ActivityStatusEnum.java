package com.jzo2o.market.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityStatusEnum {
    NO_DISTRIBUTE(1, "待生效"), DISTRIBUTING(2, "进行中"), LOSE_EFFICACY(3, "已失效"),VOIDED(4, "作废");
    private int status;
    private String name;

    public boolean equals(Integer status) {
        return status != null && this.status == status;
    }
}
