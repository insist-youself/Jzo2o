package com.jzo2o.orders.history.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.orders.history.model.domain.StatDay;
import com.jzo2o.orders.history.model.domain.StatHour;
import com.jzo2o.orders.history.mapper.StatHourMapper;
import com.jzo2o.orders.history.service.IHistoryOrdersSyncService;
import com.jzo2o.orders.history.service.IStatHourService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.orders.history.mapper.StatHourMapper;
import com.jzo2o.orders.history.model.domain.StatHour;
import com.jzo2o.orders.history.service.IStatHourService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 小时统计表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-09-21
 */
@Service
public class StatHourServiceImpl extends ServiceImpl<StatHourMapper, StatHour> implements IStatHourService {
    @Resource
    private IHistoryOrdersSyncService historyOrdersSyncService;
    /**
     * 根据id范围查询统计数据
     *
     * @param minId 最小id
     * @param maxId 最大id
     * @return 统计数据
     */
    @Override
    public List<StatHour> queryListByIdRange(Long minId, Long maxId) {
        LambdaQueryWrapper<StatHour> queryWrapper = Wrappers.<StatHour>lambdaQuery()
                .between(StatHour::getId, minId, maxId)
                .orderByAsc(StatHour::getId);
        return baseMapper.selectList(queryWrapper);
    }


    @Override
    public void statAndSaveData() {
        // 1.数据统计
        // 15天前时间
        LocalDateTime statDayLocalDateTime = DateUtils.now().minusDays(15);
        long statDayTime = DateUtils.getFormatDate(statDayLocalDateTime, "yyyMMdd");
        // 统计数据
        List<StatHour> statForHour = historyOrdersSyncService.statForHour((int) statDayTime);
        // 2.数据保存
        saveOrUpdateBatch(statForHour,500);
    }
}
