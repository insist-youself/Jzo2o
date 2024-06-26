package com.jzo2o.orders.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 违约行为枚举
 */
@AllArgsConstructor
@Getter
public enum BreachHaviorTypeEnum {
    CANCEL_ALLOCATION(1,"机构取消未分配服务单违约行为"),
    CANCEL_NO_SERVE(2,"机构或服务人员取消待服务订单违约行为"),
    CANCEL_SERVING(3,"机构或服务人员取消服务中订单违约行为"),
    DISPATCH_REJECT(4,"机构或服务人员拒绝接单违约行为");
    private int type;
    private String desc;

}
