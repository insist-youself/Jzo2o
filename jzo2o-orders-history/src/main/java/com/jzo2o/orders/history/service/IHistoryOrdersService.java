package com.jzo2o.orders.history.service;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.orders.history.model.domain.HistoryOrders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.orders.history.model.dto.request.HistoryOrdersListQueryReqDTO;
import com.jzo2o.orders.history.model.dto.request.HistoryOrdersPageQueryReqDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersDetailResDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersListResDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersPageResDTO;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-09-11
 */
public interface IHistoryOrdersService extends IService<HistoryOrders> {

    /**
     * 用户端滚动查询列表
     * @param historyOrdersListQueryReqDTO
     * @return
     */
    List<HistoryOrdersListResDTO> queryUserOrderForList(HistoryOrdersListQueryReqDTO historyOrdersListQueryReqDTO);

    /**
     * 管理端分页查询历史订单
     *
     * @param historyOrdersPageQueryReqDTO
     * @return
     */
    PageResult<HistoryOrdersPageResDTO> queryForPage(HistoryOrdersPageQueryReqDTO historyOrdersPageQueryReqDTO);

    HistoryOrdersDetailResDTO getDetailById(Long id);

    /**
     * 根据id列表获取已经存在的历史订单id列表
     * @param ids ids列表
     * @return
     */
    List<Long> queryExistIdsByIds(List<Long> ids);

    /**
     * 迁移历史订单
     */
    void migrate();

    /**
     * 删除已经迁移完成的订单
     */
    void deleteMigrated();
}
