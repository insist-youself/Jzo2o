package com.jzo2o.orders.base.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author itcast
 */

@Getter
@AllArgsConstructor
public enum EvaluationStatusEnum {
 WAIT_EVALUATE(0, "待评价"),
 COMPLETE_EVALUATION(1, "已评价");

 /**
  * 状态
  */
 private final Integer status;

 /**
  * 描述
  */
 private final String desc;

}

