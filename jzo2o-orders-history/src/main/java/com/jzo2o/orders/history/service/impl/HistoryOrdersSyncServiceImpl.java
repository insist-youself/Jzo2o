package com.jzo2o.orders.history.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.common.utils.NumberUtils;
import com.jzo2o.orders.history.model.domain.HistoryOrdersSync;
import com.jzo2o.orders.history.mapper.HistoryOrdersSyncMapper;
import com.jzo2o.orders.history.model.domain.StatDay;
import com.jzo2o.orders.history.model.domain.StatHour;
import com.jzo2o.orders.history.service.IHistoryOrdersSyncService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author itcast
 * @since 2023-09-21
 */
@Service
@Slf4j
public class HistoryOrdersSyncServiceImpl extends ServiceImpl<HistoryOrdersSyncMapper, HistoryOrdersSync> implements IHistoryOrdersSyncService {

    @Override
    public List<StatDay> statForDay(Integer statDay) {
        //todo
        return Collections.emptyList();
    }

    @Override
    public List<StatHour> statForHour(Integer statDay) {
        List<StatHour> statForHour = baseMapper.statForHour(statDay);
        if(CollUtils.isEmpty(statForHour)) {
            return null;
        }
        // 按小时统计订单
        List<StatHour> statHours = statForHour.stream().peek(sd -> {
            // 订单总数
            sd.setTotalOrderNum(NumberUtils.add(sd.getEffectiveOrderNum(), sd.getCloseOrderNum(), sd.getCancelOrderNum()).intValue());
            // 实付订单均价
            if (sd.getEffectiveOrderNum().compareTo(0) == 0) {
                sd.setRealPayAveragePrice(BigDecimal.ZERO);
            } else {
                BigDecimal realPayAveragePrice = sd.getEffectiveOrderTotalAmount().divide(new BigDecimal(sd.getEffectiveOrderNum()), 2, RoundingMode.FLOOR);
                sd.setRealPayAveragePrice(realPayAveragePrice);
            }
        }).collect(Collectors.toList());
        // 统计数据判空，并给初始化
        if(CollUtils.isEmpty(statHours)){
            statHours = new ArrayList<>();
        }
        return statHours;
    }

    @Override
    public Integer countBySortTime(LocalDateTime minSortTime, LocalDateTime maxSortTime) {
        return lambdaQuery()
                .between(HistoryOrdersSync::getSortTime, minSortTime, maxSortTime)
                .count();
    }

    @Override
    public void deleteBySortTime(LocalDateTime minSortTime, LocalDateTime maxSortTime) {

        lambdaUpdate().between(HistoryOrdersSync::getSortTime, minSortTime, maxSortTime)
                .remove();

    }

    /**
     * 获取昨天15天前的id列表,
     * @param statDay
     * @return
     */
    private List<Long> getStatIds(Integer statDay,Boolean isHour) {
        List<Long> ids = new ArrayList<>();

        LocalDateTime yesterDayEndTime = DateUtils.getDayEndTime(DateUtils.now().minusDays(1));
        LocalDateTime statTime = DateUtils.parse(statDay + "", "yyyyMMdd");
        while (statTime.isBefore(yesterDayEndTime)) {
            if(isHour) {
                ids.add(DateUtils.getFormatDate(statTime, "yyyyMMddHH"));
                statTime = statTime.plusHours(1);
            }else {
                ids.add(DateUtils.getFormatDate(statTime, "yyyyMMdd"));
                statTime = statTime.plusDays(1);
            }
        }
        return ids;
    }

}
