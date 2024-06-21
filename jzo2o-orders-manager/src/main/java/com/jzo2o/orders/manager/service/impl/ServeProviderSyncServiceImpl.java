package com.jzo2o.orders.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.NumberUtils;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.orders.base.mapper.ServeProviderSyncMapper;
import com.jzo2o.orders.base.model.domain.ServeProviderSync;
import com.jzo2o.orders.manager.service.IOrdersServeManagerService;
import com.jzo2o.orders.manager.service.IServeProviderSyncService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 机构服务端更新服务时间 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-08-09
 */
@Service
public class ServeProviderSyncServiceImpl extends ServiceImpl<ServeProviderSyncMapper, ServeProviderSync> implements IServeProviderSyncService {

    @Resource
    private IOrdersServeManagerService ordersServeManagerService;

    @Override
    public List<Integer> queryServeTimesById(Long id) {

        ServeProviderSync serveProviderSync = lambdaQuery().eq(ServeProviderSync::getId, id).one();
        return ObjectUtils.get(serveProviderSync, ServeProviderSync::getServeTimes);
    }

    @Override
    public void countServeTimesAndAcceptanceNum(Long id, Integer serveProviderType) {
        ServeProviderSync serveProviderSync = new ServeProviderSync();
        serveProviderSync.setId(id);
        if(serveProviderType == UserType.WORKER) {
            List<Integer> serveTimes = ordersServeManagerService.countServeTimes(id);
            //由于调用saveOrUpdate时遇到null不更新，这里如果为空设置为空list
            List<Integer> integers = CollUtils.defaultIfEmpty(serveTimes, new ArrayList<>());
            serveProviderSync.setServeTimes(integers);
        }else {
            serveProviderSync.setServeTimes(new ArrayList<>());
        }
        Integer acceptanceNum = ordersServeManagerService.countNoServedNum(id);
        serveProviderSync.setAcceptanceNum(NumberUtils.null2Zero(acceptanceNum));
        this.saveOrUpdate(serveProviderSync);
    }
}
