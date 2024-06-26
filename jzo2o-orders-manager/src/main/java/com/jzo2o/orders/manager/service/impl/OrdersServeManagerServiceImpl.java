package com.jzo2o.orders.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.orders.dto.response.InstitutionStaffServeCountResDTO;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.orders.base.mapper.OrdersServeMapper;
import com.jzo2o.orders.base.model.domain.OrdersServe;
import com.jzo2o.orders.manager.service.IOrdersServeManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务服务单 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-07-26
 */
@Service
@Slf4j
public class OrdersServeManagerServiceImpl extends ServiceImpl<OrdersServeMapper, OrdersServe> implements IOrdersServeManagerService {


    @Override
    public List<OrdersServe> batchQuery(List<Long> ids) {
        return lambdaQuery()
                .in(OrdersServe::getId, ids)
                .ge(OrdersServe::getServeProviderId, 0)
                .list();
    }

    @Override
    public OrdersServe queryById(Long id) {
        List<OrdersServe> list = lambdaQuery()
                .eq(OrdersServe::getId, id)
                .ge(OrdersServe::getServeProviderId, 0)
                .last(" limit 1")
                .list();
        return CollUtils.getFirst(list);
    }


    /**
     * 根据机构服务人员id查询服务数量
     *
     * @param institutionStaffId 机构服务人员id
     * @return 服务数量
     */
    @Override
    public InstitutionStaffServeCountResDTO countByInstitutionStaffId(Long institutionStaffId) {
        LambdaQueryWrapper<OrdersServe> queryWrapper = Wrappers.<OrdersServe>lambdaQuery()
                .eq(OrdersServe::getInstitutionStaffId, institutionStaffId)
                .gt(OrdersServe::getId, 0)
                .gt(OrdersServe::getServeProviderId, 0);
        Integer count = baseMapper.selectCount(queryWrapper);
        return new InstitutionStaffServeCountResDTO(count);
    }
}
