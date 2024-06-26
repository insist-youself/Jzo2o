package com.jzo2o.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.trade.enums.RefundStatusEnum;
import com.jzo2o.trade.model.domain.RefundRecord;

import java.util.List;

/**
 * @Description： 退款记录表服务类
 */
public interface RefundRecordService extends IService<RefundRecord> {

    /**
     * 根据退款单号查询退款记录
     *
     * @param refundNo 退款单号
     * @return 退款记录数据
     */
    RefundRecord findByRefundNo(Long refundNo);

    /**
     * 根据交易单号查询退款单
     *
     * @param tradingOrderNo 交易单号
     * @return 退款列表
     */
    List<RefundRecord> findByTradingOrderNo(Long tradingOrderNo);

    /**
     * 根据订单号查询退款列表
     *
     * @param productAppId 业务系统标识
     * @param productOrderNo 订单号
     * @return 退款列表
     */
    List<RefundRecord> findListByProductOrderNo(String productAppId,Long productOrderNo);

    /***
     * 按状态查询退款单，按照时间正序排序
     *
     * @param refundStatus 状态
     * @param count 查询数量，默认查询10条
     * @return 退款单数据列表
     */
    List<RefundRecord> findListByRefundStatus(RefundStatusEnum refundStatus, Integer count);
}
