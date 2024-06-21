package com.jzo2o.orders.manager.service;

public interface ISeizeDispatchService {


    /**
     * 清理资源池
     * @param cityCode 城市编码
     * @param id 资源id
     */
    void clearSeizeDispatchPool(String cityCode,Long id);

}
