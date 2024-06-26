package com.jzo2o.orders.base.mapper;

import com.jzo2o.orders.base.model.domain.OrdersDispatch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 派单池 Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2023-08-08
 */
public interface OrdersDispatchMapper extends BaseMapper<OrdersDispatch> {

//    @Insert("<script>INSERT INTO orders_dispatch(id, city_code, serve_type_id," +
//            "serve_item_name, serve_type_name, serve_item_id, serve_address, serve_item_img," +
//            "total_amount, serve_start_time, lon, lat," +
//            "pur_num, is_transfer_manual) VALUE <foreach collection='records' item='record' separator=','>#{record.id},#{record.cityCode},#{record.serveTypeId}," +
//            "#{record.serveItemName},#{record.serveStartTime},#{record.lon},#{record.lat},#{record.purNum},#{record.isTransferManual})</foreach></script>")
//    Integer batchAdd(@Param("records")List<OrdersDispatch> ordersDispatches);

}
