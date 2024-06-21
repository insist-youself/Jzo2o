package com.jzo2o.orders.seize.service.impl;

import cn.hutool.db.DbRuntimeException;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.expcetions.BadRequestException;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.orders.base.mapper.OrdersServeMapper;
import com.jzo2o.orders.base.model.domain.OrdersServe;
import com.jzo2o.orders.seize.service.IOrdersServeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.jzo2o.orders.base.enums.ServeStatusEnum.*;

/**
 * <p>
 * 服务任务 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-08-02
 */
@Service
public class OrdersServeServiceImpl extends ServiceImpl<OrdersServeMapper, OrdersServe> implements IOrdersServeService {


    @Override
    public List<Integer> countServeTimes(Long serveProviderId) {

        List<OrdersServe> list = lambdaQuery()
                .ge(OrdersServe::getId, 0)
                .eq(OrdersServe::getServeProviderId, serveProviderId)
                .in(OrdersServe::getServeStatus, Arrays.asList(NO_ALLOCATION.getStatus(), NO_SERVED.getStatus(), SERVING.getStatus()))
                .select(OrdersServe::getServeStartTime)
                .list();
        if(CollUtils.isEmpty(list)) {
            return null;
        }
        // 将预约时间转换成指定格式
        return list.stream()
                .map(ordersServe -> DateUtils.getFormatDate(ordersServe.getServeStartTime(), "yyyMMddHH").intValue())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Integer countNoServedNum(Long serveProviderId) {
        return lambdaQuery()
                .ge(OrdersServe::getId, 0)
                .eq(OrdersServe::getServeProviderId, serveProviderId)
                .in(OrdersServe::getServeStatus, Arrays.asList(NO_ALLOCATION.getStatus(), NO_SERVED.getStatus(), SERVING.getStatus()))
                .count();
    }

    @Override
    public OrdersServe findById(Long id) {
        return lambdaQuery()
                .eq(OrdersServe::getId, id)
                .ge(OrdersServe::getServeProviderId,0)
                .one();
    }


}
