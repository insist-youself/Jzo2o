package com.jzo2o.api.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 评价评分
 *
 * @author itcast
 * @create 2023/9/16 09:59
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationScoreResDTO {
    /**
     * 评分，key为订单id，value为分数
     */
    private Map<String, Double> scoreMap;
}
