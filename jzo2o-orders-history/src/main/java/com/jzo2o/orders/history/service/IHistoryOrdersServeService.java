package com.jzo2o.orders.history.service;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.orders.history.model.domain.HistoryOrdersServe;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.orders.history.model.dto.request.HistoryOrdersServePageQueryReqDTO;
import com.jzo2o.orders.history.model.dto.request.HistoryOrdersServeListQueryReqDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersServeDetailResDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersServeResDTO;

import java.util.List;

/**
 * <p>
 * 服务任务 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-09-11
 */
public interface IHistoryOrdersServeService extends IService<HistoryOrdersServe> {

    /**
     * 分页查询历史服务单
     *
     * @param historyOrdersServePageQueryReqDTO
     * @return
     */
    PageResult<HistoryOrdersServeResDTO> queryForPage(HistoryOrdersServePageQueryReqDTO historyOrdersServePageQueryReqDTO);

    /**
     * 滚动分页查询
     * @param historyOrdersServeListQueryReqDTO
     * @return
     */
    List<HistoryOrdersServeResDTO> queryForList(HistoryOrdersServeListQueryReqDTO historyOrdersServeListQueryReqDTO);
    /**
     * 查询历史服务单名称
     * @param id
     * @return
     */
    HistoryOrdersServeDetailResDTO queryDetailById(Long id);

    /**
     * 根据id列表获取已经存在的历史订单id列表
     * @param ids ids列表
     * @return
     */
    List<Long> queryExistIdsByIds(List<Long> ids);

    /**
     * 数据迁移，从history_orders_serve_sync表中迁移到history_orders_serve表中
     */
    void migrate();

    /**
     * 删除已经迁移完成的服务单
     */
    void deleteMigrated();

}
