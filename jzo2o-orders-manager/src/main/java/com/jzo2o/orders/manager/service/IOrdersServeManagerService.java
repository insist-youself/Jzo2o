package com.jzo2o.orders.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.api.orders.dto.response.InstitutionStaffServeCountResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.orders.base.model.domain.OrdersServe;
import com.jzo2o.orders.manager.model.dto.request.*;
import com.jzo2o.orders.manager.model.dto.response.OrdersServeDetailResDTO;
import com.jzo2o.orders.manager.model.dto.response.OrdersServeResDTO;
import com.jzo2o.orders.manager.model.dto.response.OrdersServeStatusNumResDTO;
import com.jzo2o.orders.manager.model.dto.response.ServeProviderServeResDTO;

import java.util.List;

/**
 * <p>
 * 服务服务单 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-07-26
 */
public interface IOrdersServeManagerService extends IService<OrdersServe> {


    /**
     * 批量获取服务单
     *
     * @param ids 服务单id列表
     * @return 服务单列表
     */
    List<OrdersServe> batchQuery(List<Long> ids);

    /**
     * 获取服务单信息
     * @param id
     * @return 服务单信息
     */
    OrdersServe queryById(Long id);

    /**
     * 根据机构服务人员id查询服务数量
     *
     * @param institutionStaffId 机构服务人员id
     * @return 服务数量
     */
    InstitutionStaffServeCountResDTO countByInstitutionStaffId(Long institutionStaffId);
}
