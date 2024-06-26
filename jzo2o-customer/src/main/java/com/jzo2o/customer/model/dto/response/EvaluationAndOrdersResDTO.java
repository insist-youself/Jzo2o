package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 评价和订单响应数据
 *
 * @author itcast
 * @create 2023/4/22 16:34
 **/
@Data
@ApiModel("评价和订单响应数据")
public class EvaluationAndOrdersResDTO {

    @ApiModelProperty("评价id")
    private String id;

    @ApiModelProperty("评价对象id")
    private String targetId;

    @ApiModelProperty("关联id（如订单id）")
    private String relationId;

    @ApiModelProperty("评价对象名称")
    private String targetName;

    @ApiModelProperty("评价人id")
    private String evaluatorId;

    @ApiModelProperty("评价人信息")
    private Person evaluatorInfo;

    @ApiModelProperty("总分")
    private Double totalScore;

    @ApiModelProperty("评价等级，1差评，2中评，3好评")
    private Integer scoreLevel;

    @ApiModelProperty("评价内容")
    private String content;

    @ApiModelProperty("评价图片列表")
    private String[] pictureArray;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("是否点赞")
    private Boolean isLiked;

    @ApiModelProperty("服务预约时间")
    private LocalDateTime serveStartTime;

    @ApiModelProperty("服务地址")
    private String serveAddress;

    @ApiModelProperty("服务项图片")
    private String serveItemImg;


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
}
