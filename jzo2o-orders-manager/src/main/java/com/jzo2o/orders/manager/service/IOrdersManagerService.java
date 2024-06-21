package com.jzo2o.orders.manager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.api.market.dto.request.CouponUseBackReqDTO;
import com.jzo2o.api.market.dto.response.AvailableCouponsResDTO;
import com.jzo2o.api.orders.dto.response.OrderResDTO;
import com.jzo2o.api.orders.dto.response.OrderSimpleResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.model.msg.TradeStatusMsg;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.manager.model.dto.OrderCancelDTO;
import com.jzo2o.orders.manager.model.dto.request.OrderPageQueryReqDTO;
import com.jzo2o.orders.manager.model.dto.request.OrdersPayReqDTO;
import com.jzo2o.orders.manager.model.dto.request.PlaceOrderReqDTO;
import com.jzo2o.orders.manager.model.dto.response.OperationOrdersDetailResDTO;
import com.jzo2o.orders.manager.model.dto.response.OrdersPayResDTO;
import com.jzo2o.orders.manager.model.dto.response.PlaceOrderResDTO;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-07-10
 */
public interface IOrdersManagerService extends IService<Orders> {

    /**
     * @param ids
     * @return
     */
    List<Orders> batchQuery(List<Long> ids);

    Orders queryById(Long id);

    /**
     * 根据订单id查询
     *
     * @param id 订单id
     * @return 订单详情
     */
    OrderResDTO getDetail(Long id);

    /**
     * 订单信息聚合
     *
     * @param id 订单id
     * @return 订单详情
     */
    OperationOrdersDetailResDTO aggregation(Long id);

    /**
     * 取消订单
     *
     * @param orderCancelDTO 取消订单模型
     */
    void cancel(OrderCancelDTO orderCancelDTO);



    /**
     * 用户端-订单删除（隐藏）
     *
     * @param id       订单id
     * @param userType 用户类型
     * @param userId   用户id
     */
    void hide(Long id, Integer userType, Long userId);

    /**
     * 管理端 - 分页查询
     *
     * @param orderPageQueryReqDTO 分页查询条件
     * @return 分页结果
     */
    PageResult<OrderSimpleResDTO> operationPageQuery(OrderPageQueryReqDTO orderPageQueryReqDTO);

    /**
     * 滚动分页查询
     *
     * @param currentUserId 当前用户id
     * @param ordersStatus  订单状态，0：待支付，100：派单中，200：待服务，300：服务中 500：订单完成，600：已取消，700：已关闭
     * @param sortBy        排序字段
     * @return 订单列表
     */
    List<OrderSimpleResDTO> consumerQueryList(Long currentUserId, Integer ordersStatus, Long sortBy);


    /**
     * 用户端-订单显示状态设置
     *
     * @param id            订单id
     * @param displayStatus 用户端是否展示，1：展示，0：隐藏
     */
    void displaySetting(Long id, Integer displayStatus);


    /**
     * 管理端 - 分页查询订单id列表
     *
     * @param orderPageQueryReqDTO 分页查询模型
     * @return 分页结果
     */
    Page<Long> operationPageQueryOrdersIdList(OrderPageQueryReqDTO orderPageQueryReqDTO);

    /**
     * 根据订单id列表查询并排序
     *
     * @param orderPageQueryReqDTO 订单分页查询请求
     * @return 订单列表
     */
    List<Orders> queryAndSortOrdersListByIds(OrderPageQueryReqDTO orderPageQueryReqDTO);



    /**
     * 查询超过评价时间的订单
     *
     * @param count 订单数量
     * @return 订单列表
     */
    List<Orders> queryOverTimeEvaluateOrdersList(Integer count);

    /**
     * 订单评价
     *
     * @param ordersId 订单id
     */
    void evaluationOrder(Long ordersId);

    /**
     * 查询派单超时的订单
     *
     * @param count 订单数量
     * @return 订单列表
     */
    List<Orders> queryDispatchOverTimeOrdersList(Integer count);
}
