package com.jzo2o.foundations.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FoundationStatusEnum {
    INIT(0,"草稿"),
    ENABLE(2,"启用"),
    DISABLE(1, "禁用");
    private int status;
    private String description;

    public boolean equals(Integer status) {
        return this.status == status;
    }

    public boolean equals(FoundationStatusEnum enableStatusEnum) {
        return enableStatusEnum != null && enableStatusEnum.status == this.getStatus();
    }
}
