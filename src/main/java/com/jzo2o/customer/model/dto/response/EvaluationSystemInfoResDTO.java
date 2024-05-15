package com.jzo2o.customer.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 86188
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统信息包含当前用户信息和配置信息")
public class EvaluationSystemInfoResDTO {

    @ApiModelProperty("当前用户信息")
    private UserInfo currentUser;

    @ApiModelProperty("评价类配置")
    private EvaluationConfig evaluationConfig;

    @ApiModelProperty("回复类配置")
    private ReplyConfig replyConfig;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserInfo {
        /**
         * 用户id
         */
        @ApiModelProperty("用户id")
        private String id;
        /**
         * 用户姓名/昵称
         */
        @ApiModelProperty("用户姓名/昵称")
        private String nickName;

        /**
         * 头像
         */
        @ApiModelProperty("用户头像")
        private String avatar;
    }

    @ApiModel("评论配置信息")
    @Data
    public static class EvaluationConfig {

        /**
         * 是否可以回复评价
         */
        @ApiModelProperty("是否开启回复评价")
        private Boolean canReply;
        /**
         * 是否开启评分评价
         */
        @ApiModelProperty("是否开启评分评价")
        private Boolean canScore;

        /**
         * 评分配置
         */
        @ApiModelProperty("评分配置")
        private List<ScoreConfig> scoreConfigList;

        /**
         * 是否开启匿名评价
         */
        @ApiModelProperty("是否开启匿名评价")
        private Boolean canAnonymous;

        /**
         * 是否开启点赞
         */
        @ApiModelProperty("是否开启点赞")
        private Boolean canLike;

        /**
         * 是否开启修改评价
         */
        @ApiModelProperty("是否开启修改评价")
        private Boolean canUpdate;
        /**
         * 是否开启删除评价
         */
        @ApiModelProperty("是否开启删除评价")
        private Boolean canDelete;
        /**
         * 是否开启举报评价
         */
        @ApiModelProperty("是否开启举报评价")
        private Boolean canReport;
        /**
         * 是否开启评价追评
         */
        @ApiModelProperty("是否开启评价追评")
        private Boolean canAppend;
        /**
         * 追评字数限制
         */
        @ApiModelProperty("追评字数限制")
        private Integer appendWordNum;
        /**
         * 是否开启图片评价
         */
        @ApiModelProperty("是否开启图片评价")
        private Boolean canAddImg;
        /**
         * 评价字数限制
         */
        @ApiModelProperty("评价字数限制")
        private Integer contentWordNum;
    }

    /**
     * 评分项
     */
    @Data
    @ApiModel("评分项")
    public static class ScoreConfig {
        /**
         * 评分项id
         */
        @ApiModelProperty("评分项id")
        private String itemId;

        /**
         * 评分项名称
         */
        @ApiModelProperty("评分项名称")
        private String itemName;

        /**
         * 权重
         */
        @ApiModelProperty("权重")
        private Integer weight;

        /**
         * 启用状态。true表示启用,false表示禁用
         */
        @ApiModelProperty("启用状态。true表示启用,false表示禁用")
        private Boolean enabled;
    }

    @ApiModel("回复配置信息")
    @Data
    public static class ReplyConfig {
        /**
         * 是否开启回复的回复
         */
        @ApiModelProperty("是否开启回复的回复")
        private Boolean canReply;

        /**
         * 是否开启修改回复
         */
        @ApiModelProperty("是否开启修改回复")
        private Boolean canUpdate;
        /**
         * 是否开启删除回复
         */
        @ApiModelProperty("是否开启删除回复")
        private Boolean canDelete;
        /**
         * 是否开启举报回复
         */
        @ApiModelProperty("是否开启举报回复")
        private Boolean canReport;
        /**
         * 是否开启匿名回复
         */
        @ApiModelProperty("是否开启匿名回复")
        private Boolean canAnonymous;

        /**
         * 是否开启点赞
         */
        @ApiModelProperty("是否开启点赞")
        private Boolean canLike;
        /**
         * 是否开启图片回复
         */
        @ApiModelProperty("是否开启图片回复")
        private Boolean canAddImg;
        /**
         * 回复字数限制
         */
        @ApiModelProperty("回复字数限制")
        private Integer contentWordNum;
    }
}
