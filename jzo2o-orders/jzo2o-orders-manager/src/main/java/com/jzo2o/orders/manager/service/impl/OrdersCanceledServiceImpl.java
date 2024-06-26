package com.jzo2o.orders.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.orders.base.mapper.OrdersCanceledMapper;
import com.jzo2o.orders.base.model.domain.OrdersCanceled;
import com.jzo2o.orders.manager.service.IOrdersCanceledService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  订单取消服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-08-19
 */
@Service
public class OrdersCanceledServiceImpl extends ServiceImpl<OrdersCanceledMapper, OrdersCanceled> implements IOrdersCanceledService {

}
