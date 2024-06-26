package com.jzo2o.customer.model.dto;

import com.jzo2o.api.customer.dto.request.ScoreItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 评价提交对象
 *
 * @author itcast
 * @create 2023/4/22 10:32
 **/
@Data
@ApiModel("评价提交对象")
public class EvaluationSubmitDTO {
    /**
     * 评价对象id
     */
    @ApiModelProperty(value = "评价对象id", required = true)
    private String targetId;

    /**
     * 关联id（如订单id）
     */
    @ApiModelProperty("关联id（如订单id）")
    private String relationId;

    /**
     * 评价对象名称
     */
    @ApiModelProperty("评价对象名称")
    private String targetName;

    /**
     * 所属人id
     */
    @ApiModelProperty("所属人id")
    private String ownerId;

    /**
     * 是否匿名
     */
    @ApiModelProperty(value = "是否匿名,0为未匿名，1为匿名", required = true)
    private Integer isAnonymous;

    /**
     * 评价分数
     */
    @ApiModelProperty("评价分数")
    private ScoreItem[] scoreArray;

    /**
     * 评价内容
     */
    @ApiModelProperty("评价内容")
    private String content;

    /**
     * 评价图片列表
     */
    @ApiModelProperty("评价图片列表")
    private String[] pictureArray;

    /**
     * 非前端传入，在controller层设置
     */
    private String ip;
}
