package com.jzo2o.orders.history.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.jzo2o.orders.history.model.domain.HistoryOrdersServeSync;
import com.jzo2o.orders.history.mapper.HistoryOrdersServeSyncMapper;
import com.jzo2o.orders.history.service.IHistoryOrdersServeSyncService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务单 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-09-21
 */
@Service
public class HistoryOrdersServeSyncServiceImpl extends ServiceImpl<HistoryOrdersServeSyncMapper, HistoryOrdersServeSync> implements IHistoryOrdersServeSyncService {

    @Override
    public Integer countBySortTime(LocalDateTime minSortTime, LocalDateTime maxSortTime) {
        return lambdaQuery().between(HistoryOrdersServeSync::getSortTime, minSortTime, maxSortTime)
                .count();
    }

    @Override
    public void deleteBySortTime(LocalDateTime minSortTime, LocalDateTime maxSortTime) {
        lambdaUpdate().between(HistoryOrdersServeSync::getSortTime, minSortTime, maxSortTime)
                .remove();
    }
}
