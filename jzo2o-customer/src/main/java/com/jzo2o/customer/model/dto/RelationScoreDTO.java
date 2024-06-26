package com.jzo2o.customer.model.dto;

import lombok.Data;

/**
 * 关联id对应评分数据
 *
 * @author itcast
 * @create 2023/9/16 10:28
 **/
@Data
public class RelationScoreDTO {
    /**
     * 关联id
     */
    private String relationId;
    
    /**
     * 评分
     */
    private Double score;
}