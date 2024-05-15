package com.jzo2o.customer.model.dto;

import lombok.Data;

/**
 * mq消息 - 评分统计数据
 *
 * @author itcast
 * @create 2023/9/14 15:56
 **/
@Data
public class ScoreStatisticsMsg {
    /**
     * 对象类型id
     */
    private String targetTypeId;

    /**
     * 对象id
     */
    private String targetId;

    /**
     * 对象名称
     */
    private String targetName;

    /**
     * 评分项id，如果综合评分设为0
     */
    private String itemId;

    /**
     * 差评数量
     */
    private Long badLevelCount;

    /**
     * 中评数量
     */
    private Long middleLevelCount;

    /**
     * 好评数量
     */
    private Long goodLevelCount;

    /**
     * 总评价数
     */
    private Long count;

    /**
     * 总评分数
     */
    private Long scoreCount;

    /**
     * 总分
     */
    private Double totalScore;

    /**
     * 平均分
     */
    private Double averageScore;

}
