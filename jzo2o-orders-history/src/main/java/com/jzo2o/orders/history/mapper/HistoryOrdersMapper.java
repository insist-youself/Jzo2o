package com.jzo2o.orders.history.mapper;

import com.jzo2o.orders.history.model.domain.HistoryOrders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2023-09-11
 */
public interface HistoryOrdersMapper extends BaseMapper<HistoryOrders> {


}
