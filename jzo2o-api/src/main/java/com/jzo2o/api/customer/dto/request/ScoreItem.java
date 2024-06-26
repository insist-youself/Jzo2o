package com.jzo2o.api.customer.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评分项
 *
 * @author itcast
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("评价发表评分")
public class ScoreItem {
    /**
     * 评分项id
     */
    @ApiModelProperty(value = "评分项id", required = true)
    private String itemId;

    /**
     * 评分项名称
     */
    @ApiModelProperty(value = "评分项名称", required = true)
    private String itemName;

    /**
     * 分数
     */
    @ApiModelProperty(value = "分数", required = true)
    private Double score;

    /**
     * 获取默认评分项
     *
     * @param itemId   评分项id
     * @param itemName 评分项名称
     * @return 评分项
     */
    public static ScoreItem defaultScoreItem(String itemId, String itemName) {
        return new ScoreItem(itemId, itemName, 5.0);
    }
}