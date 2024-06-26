package com.jzo2o.api.customer.dto.request;

import com.jzo2o.common.model.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 机构下属服务人员分页查询
 *
 * @author itcast
 * @create 2023/7/18 20:42
 **/
@Data
@ApiModel("机构下属服务人员分页查询请求")
public class InstitutionStaffPageQueryReqDTO extends PageQueryDTO {
    /**
     * 机构id
     */
    @ApiModelProperty("机构id")
    private Long institutionId;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;
}
