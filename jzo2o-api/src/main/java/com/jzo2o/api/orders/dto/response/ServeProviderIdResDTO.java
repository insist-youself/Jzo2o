package com.jzo2o.api.orders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务人员/机构id
 *
 * @author itcast
 * @create 2023/9/11 19:19
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServeProviderIdResDTO {

    /**
     * 服务人员/机构id
     */
    private Long serveProviderId;
}
