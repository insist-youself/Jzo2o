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
     * 分页查询，机构端
     *
     * @param currentUserId
     * @param serveProviderType
     * @param ordersServePageQueryReqDTO
     * @return
     */
    PageResult<OrdersServeResDTO> queryForPage(Long currentUserId, Integer serveProviderType, OrdersServePageQueryReqDTO ordersServePageQueryReqDTO);

    /**
     * 列表查询
     *
     * @param currentUserId 用户id
     * @param serveStatus 服务状态
     * @param sortBy 排序字段
     * @return
     */
    List<OrdersServeResDTO> queryForList(Long currentUserId, Integer serveStatus, Long sortBy);


    /**
     * 机构服务单人员分配
     *
     * @param serveProviderId 机构id
     * @param institutionStaffId 机构服务人员id
     */
    void allocation(Long id, Long serveProviderId, Long institutionStaffId);

    /**
     * 服务端、机构端删除订单，服务端和机构端视为不可见并非逻辑删除或实际删除
     *
     * @param id
     * @param serveProviderId
     */
    void deleteServe(Long id, Long serveProviderId, Integer serveProviderType);

    /**
     *
     * @param serveStartReqDTO
     * @param serveProviderId
     */
    void serveStart(ServeStartReqDTO serveStartReqDTO, Long serveProviderId);


    void serveFinished(ServeFinishedReqDTO serveFinishedReqDTO, Long serveProviderId, Integer serveProviderType);


    /**
     * 获取服务单明细
     * @param id
     * @param serveProviderId
     * @return
     */
    OrdersServeDetailResDTO getDetail(Long id, Long serveProviderId);

    /**
     * 取消服务,机构端、服务端前端调用，会产生违约记录，影响接派单
     * 待分配、待服务、待上门
     *
     * @param orderServeCancelReqDTO
     * @param serveProviderId
     */
    void cancelByProvider(OrderServeCancelReqDTO orderServeCancelReqDTO, Long serveProviderId);

    /**
     * 取消服务单方法，已结算的服务单取消状态不变，其他结算状态变为不可结算状态
     * 服务单已完成：表示服务单状态变为退单，其他服务状态变为取消
     * 取消订单不会产生违约记录，不影响订单接单，派单行为
     *
     * @param ordersId
     */
    void cancelByUserAndOperation(Long ordersId);

    /**
     * 统计机构或服务人员的状态机对应的数量
     *
     * @param serveProviderId
     * @return
     */
    OrdersServeStatusNumResDTO countServeStatusNum(Long serveProviderId);

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
     * 查询服务人员/机构服务数据
     *
     * @param ordersServePageQueryByCurrentUserReqDTO 分页条件
     * @return 分页结果
     */
    PageResult<ServeProviderServeResDTO> pageQueryByServeProvider(OrdersServePageQueryByServeProviderReqDTO ordersServePageQueryByCurrentUserReqDTO);


    /**
     * 查询服务单
     *
     * @param id 服务单id
     * @param serveProviderId 服务人员或机构id
     */
    OrdersServe queryByIdAndServeProviderId(Long id, Long serveProviderId);


    /**
     * 根据id更新服务单信息
     *
     * @param id 服务单id
     * @param serveProviderId 服务人员或机构id
     * @param ordersServe 服务单信息
     * @return
     */
    boolean updateByIdAndServeProviderId(Long id, Long serveProviderId, OrdersServe ordersServe);

    /**
     * 统计当前服务单数（包含待接单、待开始、待分配
     * @param serveProviderId
     * @return
     */
    List<Integer> countServeTimes(Long serveProviderId);

    /**
     * 统计当前订单未完成的服务数量
     * @param serveProviderId
     * @return
     */
    Integer countNoServedNum(Long serveProviderId);

    /**
     * 删除服务单
     *
     * @param id 服务单id
     * @param serveProviderId 服务人员或机构id
     */
    void deleteByIdAndServeProviderId(Long id, Long serveProviderId);

    /**
     * 根据机构服务人员id查询服务数量
     *
     * @param institutionStaffId 机构服务人员id
     * @return 服务数量
     */
    InstitutionStaffServeCountResDTO countByInstitutionStaffId(Long institutionStaffId);
}
