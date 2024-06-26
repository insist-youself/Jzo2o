package com.jzo2o.market.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityTypeEnum {
    AMOUNT_DISCOUNT(1, "满减"), RATE_DISCOUNT(2, "打折");

    private int type;
    private String name;

    public boolean equals(Integer type) {
        return type !=  null && type.equals(this.type);
    }
}
