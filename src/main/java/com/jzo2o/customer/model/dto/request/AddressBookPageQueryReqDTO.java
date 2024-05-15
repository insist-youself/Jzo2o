package com.jzo2o.customer.model.dto.request;

import com.jzo2o.common.model.dto.PageQueryDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 * 地址薄分页查询请求
 * </p>
 *
 * @author itcast
 * @since 2023-07-06
 */
@Data
@ApiModel("地址薄分页查询请求")
public class AddressBookPageQueryReqDTO extends PageQueryDTO {
}
