package com.jzo2o.orders.dispatch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.orders.base.model.domain.OrdersDispatch;
import com.jzo2o.orders.dispatch.enums.DispatchStrategyEnum;
import com.jzo2o.orders.dispatch.model.dto.ServeProviderDTO;

import java.util.List;

/**
 * <p>
 * 派单池 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-08-02
 */
public interface IOrdersDispatchService extends IService<OrdersDispatch> {


    /**
     * 派单接口
     * @param id
     */
    void dispatch(Long id);


    /**
     * 批量向派单池中添加派单信息
     *
     * @param ordersDispatch
     * @return
     */
//    void batchAdd(List<OrdersDispatch> ordersDispatch);


    /**
     * 转人工派单
     *
     * @param id
     */
//    void transferManual(Long id);

    /**
     * 批量删除派单
     *
     * @param ids
     * @return
     */
//    int batchDeleteByIds(List<Long> ids);

    /**
     *  查询符合条件的服务人员或机构
     * @param cityCode 城市编码
     * @param serveItemId 服务项id
     * @param maxDistance 派单最大限制距离
     * @param serveTime 服务时间格式yyyyMMddHH 数字格式
     * @param dispatchStrategyEnum 派单策略，距离优先策略、最少接单策略、
     * @param lon
     * @param lat
     * @param limit
     * @return
     */
    List<ServeProviderDTO> searchDispatchInfo(String cityCode, long serveItemId, double maxDistance, int serveTime, DispatchStrategyEnum dispatchStrategyEnum, Double lon, Double lat, int limit);


}
