package com.jzo2o.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jzo2o.trade.model.domain.Trading;
import org.apache.ibatis.annotations.Mapper;

/**
 * 交易订单表Mapper接口
 */
@Mapper
public interface TradingMapper extends BaseMapper<Trading> {

}
