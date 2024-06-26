package com.jzo2o.orders.base.mapper;

import com.jzo2o.orders.base.model.domain.OrdersServe;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 服务任务 Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2023-08-02
 */
public interface OrdersServeMapper extends BaseMapper<OrdersServe> {

    /**
     * 物理删除服务单
     * @param id 服务单id
     * @param serveProviderId 服务人员或机构id
     * @return 删除数量
     */
//    @Delete("delete from orders_serve where id=#{id} and serve_provider_id=#{serveProviderId}")
//    Integer deleteByIdAndServeProviderId(@Param("id") Long id, @Param("serveProviderId") Long serveProviderId);
}
