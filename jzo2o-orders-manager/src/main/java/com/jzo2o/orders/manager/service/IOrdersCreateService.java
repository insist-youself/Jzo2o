package com.jzo2o.orders.manager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.api.market.dto.request.CouponUseBackReqDTO;
import com.jzo2o.api.market.dto.response.AvailableCouponsResDTO;
import com.jzo2o.api.orders.dto.response.OrderResDTO;
import com.jzo2o.api.orders.dto.response.OrderSimpleResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.model.msg.TradeStatusMsg;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.manager.model.dto.OrderCancelDTO;
import com.jzo2o.orders.manager.model.dto.request.OrderPageQueryReqDTO;
import com.jzo2o.orders.manager.model.dto.request.OrdersPayReqDTO;
import com.jzo2o.orders.manager.model.dto.request.PlaceOrderReqDTO;
import com.jzo2o.orders.manager.model.dto.response.OperationOrdersDetailResDTO;
import com.jzo2o.orders.manager.model.dto.response.OrdersPayResDTO;
import com.jzo2o.orders.manager.model.dto.response.PlaceOrderResDTO;

import java.util.List;

/**
 * <p>
 * 下单服务类
 * </p>
 *
 * @author itcast
 * @since 2023-07-10
 */
public interface IOrdersCreateService extends IService<Orders> {

    /**
     * 获取可用优惠券
     *
     * @param serveId 服务id
     * @param purNum  购买数量
     * @return 可用优惠券列表
     */
    List<AvailableCouponsResDTO> getAvailableCoupons(Long serveId, Integer purNum);

    /**
     * 下单
     *
     * @param placeOrderReqDTO
     * @return
     */
    PlaceOrderResDTO placeOrder(PlaceOrderReqDTO placeOrderReqDTO);

    /**
     * 更新支付状态
     *
     * @param id        订单id
     * @param payStatus 支付状态
     */
    Boolean updatePayStatus(Long id, Integer payStatus);

    /**
     * 更新退款状态
     *
     * @param id           订单id
     * @param refundStatus 退款状态
     * @param refundId     第三方支付的退款单号
     * @param refundNo     支付服务退款单号
     */
    Boolean updateRefundStatus(Long id, Integer refundStatus, String refundId, Long refundNo);



    /**
     * 生成订单
     *
     * @param orders
     */
    void add(Orders orders);

    /**
     * 生成订单 使用优惠券
     *
     * @param orders   订单信息
     * @param couponId 优惠券id
     */
    void addWithCoupon(Orders orders, Long couponId);

    /**
     * 支付成功， 其他信息暂且不填
     *
     * @param tradeStatusMsg 交易状态消息
     */
    void paySuccess(TradeStatusMsg tradeStatusMsg);

    /**
     * 订单支付
     *
     * @param id              订单id
     * @param ordersPayReqDTO 订单支付请求体
     * @return 订单支付响应体
     */
    OrdersPayResDTO pay(Long id, OrdersPayReqDTO ordersPayReqDTO);



    /**
     * 请求支付服务查询支付结果
     *
     * @param id 订单id
     * @return 订单支付响应体
     */
    int getPayResultFromTradServer(Long id);

    /**
     * 查询超时订单id列表
     *
     * @param count 数量
     * @return 订单id列表
     */
    List<Orders> queryOverTimePayOrdersListByCount(Integer count);


}
