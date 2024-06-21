package com.jzo2o.orders.history.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jzo2o.orders.history.model.domain.HistoryOrdersServe;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务任务 Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2023-09-11
 */
public interface HistoryOrdersServeMapper extends BaseMapper<HistoryOrdersServe> {

    Integer migrate(@Param("yesterDayStartTime") LocalDateTime yesterDayStartTime,
                    @Param("yesterDayEndTime") LocalDateTime yesterDayEndTime,
                    @Param("offset") Integer offset,
                    @Param("perNum") Integer perNum);
}
