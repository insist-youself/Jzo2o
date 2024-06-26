package com.jzo2o.customer.model.dto.response;

import com.jzo2o.customer.model.dto.ServeSkillSimpleDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 服务人员/机构基本信息
 *
 * @author itcast
 * @create 2023/9/6 16:32
 **/
@Data
@ApiModel("服务人员/机构基本信息")
public class ServeProviderBasicInformationResDTO {
    /**
     * 服务人员/机构id
     */
    @ApiModelProperty("服务人员/机构id")
    private Long id;

    /**
     * 类型，2：服务人员，3：服务机构
     */
    @ApiModelProperty("类型，2：服务人员，3：服务机构")
    private Integer type;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 账号
     */
    @ApiModelProperty("账号")
    private String phone;

    @ApiModelProperty("服务技能简略信息")
    private List<ServeSkillSimpleDTO> serveSkillSimpleList;

    /**
     * 服务分类
     */
    @ApiModelProperty("服务分类")
    private String serveTypes;

    /**
     * 服务技能
     */
    @ApiModelProperty("服务技能")
    private String serveSkills;

    /**
     * 城市名称
     */
    @ApiModelProperty("城市名称")
    private String cityName;

    /**
     * 意向单范围
     */
    @ApiModelProperty("意向接单范围")
    private String intentionScope;

    /**
     * 是否可以接单，0：关闭接单，1：开启接单
     */
    @ApiModelProperty("是否可以接单，0：关闭接单，1：开启接单")
    private Integer canPickUp;

    /**
     * 状态，0：正常，1：冻结
     */
    @ApiModelProperty("状态，0：正常，1：冻结")
    private Integer status;

    /**
     * 综合评分
     */
    @ApiModelProperty("综合评分")
    private Double score;

    /**
     * 认证时间
     */
    @ApiModelProperty("认证时间")
    private LocalDateTime certificationTime;
}
