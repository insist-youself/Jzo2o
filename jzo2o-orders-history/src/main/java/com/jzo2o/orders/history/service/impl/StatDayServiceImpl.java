package com.jzo2o.orders.history.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.orders.history.model.domain.StatDay;
import com.jzo2o.orders.history.mapper.StatDayMapper;
import com.jzo2o.orders.history.service.IHistoryOrdersService;
import com.jzo2o.orders.history.service.IHistoryOrdersSyncService;
import com.jzo2o.orders.history.service.IStatDayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.orders.history.mapper.StatDayMapper;
import com.jzo2o.orders.history.model.domain.StatDay;
import com.jzo2o.orders.history.model.domain.StatHour;
import com.jzo2o.orders.history.service.IStatDayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 日统计表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-09-21
 */
@Service
public class StatDayServiceImpl extends ServiceImpl<StatDayMapper, StatDay> implements IStatDayService {

    @Resource
    private IHistoryOrdersSyncService historyOrdersSyncService;


    /**
     * 根据id范围聚合统计
     *
     * @param minId 最小id
     * @param maxId 最大id
     * @return 聚合统计数据
     */
    @Override
    public StatDay aggregationByIdRange(Long minId, Long maxId) {
        return baseMapper.aggregationByIdRange(minId, maxId);
    }

    /**
     * 根据id范围查询统计数据
     *
     * @param minId 最小id
     * @param maxId 最大id
     * @return 统计数据
     */
    @Override
    public List<StatDay> queryListByIdRange(Long minId, Long maxId) {
        LambdaQueryWrapper<StatDay> queryWrapper = Wrappers.<StatDay>lambdaQuery()
                .between(StatDay::getId, minId, maxId)
                .orderByAsc(StatDay::getId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void statAndSaveData() {

    }
}
