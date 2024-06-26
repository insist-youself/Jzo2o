package com.jzo2o.orders.base.mapper;

import com.jzo2o.orders.base.model.domain.HistoryOrdersSync;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

/**
 * <p>
 * 历史订单完成15天后同步到历史订单同步表中，通过canal同步到历史订单库中；1天后删除（删除条件当天数据和历史订单库中的订单数据数量一致） Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2023-09-13
 */
public interface HistoryOrdersSyncMapper extends BaseMapper<HistoryOrdersSync> {

    @Select("select max(sort_time) from history_orders_sync")
    LocalDateTime queryMaxSortTime();
}
