package com.jzo2o.orders.dispatch.strategys;

import com.jzo2o.orders.dispatch.model.dto.ServeProviderDTO;

import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description 策略接口
 * @date 2023/11/24 10:56
 */
public interface IProcessStrategy {
    /**
     * 从服务人员/机构列表中获取高优先级别的一个，如果出现多个相同优先级随机获取一个
     *
     * @param serveProviderDTOS 服务人员/机构列表
     * @return
     */
    ServeProviderDTO getPrecedenceServeProvider(List<ServeProviderDTO> serveProviderDTOS);
}
