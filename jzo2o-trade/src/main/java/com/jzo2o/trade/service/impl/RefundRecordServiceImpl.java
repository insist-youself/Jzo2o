package com.jzo2o.trade.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.trade.enums.RefundStatusEnum;
import com.jzo2o.trade.mapper.RefundRecordMapper;
import com.jzo2o.trade.model.domain.RefundRecord;
import com.jzo2o.trade.service.RefundRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author itcast
 * @Description： 退款记录服务实现类
 */
@Service
public class RefundRecordServiceImpl extends ServiceImpl<RefundRecordMapper, RefundRecord> implements RefundRecordService {

    @Override
    public RefundRecord findByRefundNo(Long refundNo) {
        LambdaQueryWrapper<RefundRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RefundRecord::getRefundNo, refundNo);
        return super.getOne(queryWrapper);
    }

    @Override
    public List<RefundRecord> findByTradingOrderNo(Long tradingOrderNo) {
        LambdaQueryWrapper<RefundRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RefundRecord::getTradingOrderNo, tradingOrderNo);
        queryWrapper.orderByDesc(RefundRecord::getCreateTime);
        return super.list(queryWrapper);
    }

    @Override
    public List<RefundRecord> findListByProductOrderNo(String productAppId,Long productOrderNo) {
        LambdaQueryWrapper<RefundRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RefundRecord::getProductOrderNo, productOrderNo)
                        .eq(RefundRecord::getProductAppId, productAppId);
        queryWrapper.orderByDesc(RefundRecord::getCreateTime);
        return super.list(queryWrapper);
    }

    @Override
    public List<RefundRecord> findListByRefundStatus(RefundStatusEnum refundStatus, Integer count) {
        count = NumberUtil.max(count, 10);
        LambdaQueryWrapper<RefundRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RefundRecord::getRefundStatus, refundStatus)
                .orderByAsc(RefundRecord::getCreateTime)
                .last("LIMIT " + count);
        return list(queryWrapper);
    }
}
