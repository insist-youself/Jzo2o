package com.jzo2o.orders.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.orders.base.enums.BreachHaviorTypeEnum;
import com.jzo2o.orders.base.mapper.BreachRecordMapper;
import com.jzo2o.orders.base.model.domain.BreachRecord;
import com.jzo2o.orders.manager.service.IBreachRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 违约记录 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-08-02
 */
@Service
public class BreachRecordServiceImpl extends ServiceImpl<BreachRecordMapper, BreachRecord> implements IBreachRecordService {



    @Override
    public void add(BreachRecord breachRecord) {
        breachRecord.setBreachDay(DateUtils.getDay());
        breachRecord.setBreachTime(DateUtils.now());
        baseMapper.insert(breachRecord);
    }


    @Override
    public int count(Long serveProviderId, BreachHaviorTypeEnum breachHaviorTypeEnum, int breachDay) {
        return lambdaQuery().eq(BreachRecord::getServeProviderId, serveProviderId)
                .eq(BreachRecord::getBehaviorType, breachHaviorTypeEnum.getType())
                .eq(BreachRecord::getBreachDay, breachDay)
                .count();
    }
}
