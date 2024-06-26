package com.jzo2o.orders.base.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 派单配置
 */
@Configuration
@ConfigurationProperties(prefix = "orders.dispatch")
@Data
public class DispatchProperties {
    /**
     * 机构端展示抢单列表数量
     */
    public Integer seizeListDispalyNumOfInstitution = 50;
    /**
     * 服务端展示抢单列表数量
     */
    public Integer seizeListDispalyNumOfServe = 20;

    /**
     * 机构端拥有服务单数量
     */
    public Integer serveTaskNumOrInstitution = 50;

    /**
     * 服务人员最大拥有服务数量
     */
    public Integer serveTaskNumOfServe = 10;
}
