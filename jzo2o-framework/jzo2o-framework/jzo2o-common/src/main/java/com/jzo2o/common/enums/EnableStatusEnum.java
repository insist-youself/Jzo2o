package com.jzo2o.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnableStatusEnum {
    UNKNOWAL(-1,"未知"),ENABLE(1,"启用"),DISABLE(0, "禁用");
    private int status;
    private String description;

    public boolean equals(Integer status) {
        return this.status == status;
    }

    public boolean equals(EnableStatusEnum enableStatusEnum) {
        return enableStatusEnum != null && enableStatusEnum.status == this.getStatus();
    }
}
