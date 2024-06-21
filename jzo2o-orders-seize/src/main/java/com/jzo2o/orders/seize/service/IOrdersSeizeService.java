package com.jzo2o.orders.seize.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.orders.base.model.domain.OrdersSeize;
import com.jzo2o.orders.seize.model.dto.request.OrdersSerizeListReqDTO;
import com.jzo2o.orders.seize.model.dto.response.OrdersSeizeListResDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 抢单池 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-08-15
 */
@Service
public interface IOrdersSeizeService extends IService<OrdersSeize> {


    /**
     * 根据城市编码和抢单超时时间获取超时订单，已经过超时处理的订单不被检索
     * 超时条件满足一条均认为是订单超时，如下
     * 当前时间距离服务预约时间间隔小于配置值时进入派单
     *
     * @param cityCode        城市编码
     * @param timeoutInterval 抢单成功
     * @return
     */
    List<OrdersSeize> queryTimeoutSeizeOrders(String cityCode, Integer timeoutInterval);

    /**
     * 批量将订单改为抢单超时（预约时间未超时），并继续抢单
     *
     * @param ids 抢单id
     */
    void batchTimeout(List<Long> ids);


    /**
     * 查询到达预约时间还未抢单成功的记录
     * @return
     */
    List<OrdersSeize> queryArriveServeStartTimeSeizeOrder();
    /**
     * 查询到达预约时间还未抢单成功的记录 从es中查询
     * @return
     */
    List<OrdersSeizeListResDTO.OrdersSeize> queryArriveServeStartTimeSeizeOrderFromEs();

    /**
     * 批量删除抢单记录
     *
     * @param ids 抢单id
     */
    void batchDeleteByIds(List<Long> ids);

    /**
     * 查询当前用户可以服务的抢单记录
     *
     * @param ordersSerizeListReqDTO 抢单分页查询条件
     *
     * @return 抢单数据
     */
    OrdersSeizeListResDTO queryForList(OrdersSerizeListReqDTO ordersSerizeListReqDTO);


    /**
     * 抢单
     * @param id 抢单id
     * @param serveProviderId 服务人员或机构id
     * @param serveProviderType 用户类型，2：服务人员，3：机构
     * @param isMatchine 是否是机器抢单
     */
    void seize(Long id, Long serveProviderId, Integer serveProviderType, Boolean isMatchine);

    /**
     * 获取抢单列表展示数量
     *
     * @param currentProviderType
     * @return
     */
//    Integer getSeizeListDisplayNum(Integer currentProviderType);

    /**
     * 抢单成功处理结果
     * @param ordersSeize 抢单信息
     * @param serveProviderId 服务人员或机构id
     * @param serveProviderType 抢单服务人员或机构类型
     * @param isMatchine 是否是机器抢单
     */
    void seizeOrdersSuccess(OrdersSeize ordersSeize, Long serveProviderId, Integer serveProviderType,Boolean isMatchine);

    /**
     * 处理某个城市的订单超时任务
     *
     * @param cityCode 城市编码
     * @param timeoutInterval 超时时间间隔
     */
//    void processSeizeTimeout(String cityCode, Integer timeoutInterval);


}
