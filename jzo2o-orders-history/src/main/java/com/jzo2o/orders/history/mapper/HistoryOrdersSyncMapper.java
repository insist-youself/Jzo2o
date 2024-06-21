package com.jzo2o.orders.history.mapper;

import com.jzo2o.orders.history.model.domain.HistoryOrdersSync;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jzo2o.orders.history.model.domain.StatDay;
import com.jzo2o.orders.history.model.domain.StatHour;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单统计
 * </p>
 *
 * @author itcast
 * @since 2023-09-21
 */
public interface HistoryOrdersSyncMapper extends BaseMapper<HistoryOrdersSync> {


    List<StatHour> statForHour(@Param("queryDay") Integer queryDay);

}
