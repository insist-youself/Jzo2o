package com.jzo2o.orders.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author itcast
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    NO_PAY(0, "待支付", "NO_PAY"),
    DISPATCHING(100, "派单中", "DISPATCHING"),
    NO_SERVE(200, "待服务", "NO_SERVE"),
    SERVING(300, "服务中", "SERVING"),
//    NO_EVALUATION(400, "待评价", "NO_EVALUATION"),
    FINISHED(500, "已完成", "FINISHED"),
    CANCELED(600, "已取消", "CANCELED"),
    CLOSED(700, "已关闭", "CLOSED");

    private final Integer status;
    private final String desc;
    private final String code;

    /**
     * 根据状态值获得对应枚举
     *
     * @param status 状态
     * @return 状态对应枚举
     */
    public static OrderStatusEnum codeOf(Integer status) {
        for (OrderStatusEnum orderStatusEnum : values()) {
            if (orderStatusEnum.status.equals(status)) {
                return orderStatusEnum;
            }
        }
        return null;
    }
}
