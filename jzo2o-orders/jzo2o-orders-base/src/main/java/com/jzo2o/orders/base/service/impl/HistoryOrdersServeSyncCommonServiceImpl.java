package com.jzo2o.orders.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.customer.InstitutionStaffApi;
import com.jzo2o.api.customer.ServeProviderApi;
import com.jzo2o.api.orders.OrdersHistoryApi;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.orders.base.mapper.HistoryOrdersServeSyncMapper;
import com.jzo2o.orders.base.mapper.OrdersCanceledMapper;
import com.jzo2o.orders.base.mapper.OrdersMapper;
import com.jzo2o.orders.base.mapper.OrdersServeMapper;
import com.jzo2o.orders.base.model.domain.HistoryOrdersServeSync;
import com.jzo2o.orders.base.service.IHistoryOrdersServeSyncCommonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务任务 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-09-14
 */
@Service
public class HistoryOrdersServeSyncCommonServiceImpl extends ServiceImpl<HistoryOrdersServeSyncMapper, HistoryOrdersServeSync> implements IHistoryOrdersServeSyncCommonService {
}
