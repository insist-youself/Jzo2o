package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 评价完整信息
 * 连带了评价的回复
 *
 * @author itcast
 * @create 2023/4/22 16:34
 **/
@Data
@ApiModel("评价完整信息")
public class EvaluationResDTO {

    @ApiModelProperty("评价id")
    private String id;

    @ApiModelProperty("评价对象id")
    private String targetId;

    @ApiModelProperty("关联id（如订单id）")
    private String relationId;

    @ApiModelProperty("评价对象名称")
    private String targetName;

    @ApiModelProperty("所属人id")
    private String ownerId;

    @ApiModelProperty("评价人id")
    private String evaluatorId;

    @ApiModelProperty("评价人信息")
    private Person evaluatorInfo;

    @ApiModelProperty("评价分数")
    private ScoreItem[] scoreArray;

    @ApiModelProperty("总分")
    private Double totalScore;

    @ApiModelProperty("评价等级，1差评，2中评，3好评")
    private Integer scoreLevel;

    @ApiModelProperty("评价内容")
    private String content;

    @ApiModelProperty("评价图片列表")
    private String[] pictureArray;

    @ApiModelProperty("是否置顶，0为取消置顶，1为置顶")
    private Integer isTop;

    @ApiModelProperty("热度指标统计")
    private HotIndexStatistics statistics;

    @ApiModelProperty("热度值")
    private Integer hotScore;

    @ApiModelProperty("省份")
    private String province;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("是否点赞")
    private Boolean isLiked;

    /**
     * 评分项
     */
    @Data
    @ApiModel("评分项信息")
    public static class ScoreItem {
        /**
         * 评分项名称
         */
        @ApiModelProperty("评分项名称")
        private String itemName;

        /**
         * 分数
         */
        @ApiModelProperty(value = "分数", required = true)
        private Double score;
    }

    /**
     * 人物信息
     */
    @Data
    @ApiModel("人物信息")
    public static class Person {
        /**
         * 昵称
         */
        @ApiModelProperty("昵称")
        private String nickName;

        /**
         * 头像
         */
        @ApiModelProperty("头像")
        private String avatar;

        /**
         * 是否匿名
         */
        @ApiModelProperty("是否匿名,0为未匿名，1为匿名")
        private Integer isAnonymous;
    }

    /**
     * 热度指标统计
     */
    @Data
    @ApiModel("热度指标统计")
    public static class HotIndexStatistics {
        /**
         * 点赞数
         */
        @ApiModelProperty("点赞数")
        private Integer likeNumber;

        /**
         * 回复数
         */
        @ApiModelProperty("回复数")
        private Integer replyNumber;

        /**
         * 内容质量分数
         */
        @ApiModelProperty("内容质量分数")
        private Integer qualityScore;
    }
}
