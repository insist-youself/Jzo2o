package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author itcast
 */
@Data
@ApiModel("点赞请求")
public class LikeOrCancelReqDTO {

    @ApiModelProperty("点赞目标类型，1：评论点赞，2：回复点赞")
    private Integer likeTargetType = 1;

    /**
     * 点赞目标id
     */
    @ApiModelProperty("点赞目标id,评论id或回复id")
    private String id;

    /**
     * 点赞状态，0：取消点赞，1：点赞
     */
    @ApiModelProperty("点赞状态，0：取消点赞，1：点赞")
    private Integer state;
}