package com.jzo2o.orders.seize.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.orders.base.model.domain.ServeProviderSync;

import java.util.List;

/**
 * <p>
 * 机构服务端更新服务时间 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-08-09
 */
public interface IServeProviderSyncService extends IService<ServeProviderSync> {


    /**
     * 获取服务提供者的服务时间 年月日时
     * @param id
     * @return
     */
    List<Integer> queryServeTimesById(Long id);

    /**
     * 统计用户或机构当前服务时间列表和接单数量
     *
     * @param id 服务人员或机构id
     * @param serveProviderType 类型，2：服务人员，3：机构
     */
    void countServeTimesAndAcceptanceNum(Long id, Integer serveProviderType);




}
