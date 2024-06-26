package com.jzo2o.api.customer.dto.request;

import com.jzo2o.common.model.CurrentUserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发表评价请求体
 *
 * @author itcast
 * @create 2023/9/11 16:22
 **/
@Data
@ApiModel("发表评价请求体")
public class EvaluationSubmitReqDTO {

    @ApiModelProperty(value = "当前用户信息", hidden = true)
    private CurrentUserInfo currentUserInfo;

    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型", hidden = true)
    private Integer userType;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", hidden = true)
    private Long userId;

    @ApiModelProperty("订单id")
    private Long ordersId;

    @ApiModelProperty("服务项评价内容")
    private String serveItemEvaluationContent;

    @ApiModelProperty("服务项评价图片列表")
    private String[] serveItemPictureArray;

    @ApiModelProperty("服务项评分项列表")
    private ScoreItem[] serveItemScoreItems;

    @ApiModelProperty("师傅评价内容")
    private String serveProviderEvaluationContent;

    @ApiModelProperty("师傅评分项列表")
    private ScoreItem[] serveProviderScoreItems;

    @ApiModelProperty(value = "是否匿名,0为未匿名，1为匿名", required = true)
    private Integer isAnonymous;
}
