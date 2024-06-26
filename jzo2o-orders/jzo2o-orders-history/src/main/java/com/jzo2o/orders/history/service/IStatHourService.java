package com.jzo2o.orders.history.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.orders.history.model.domain.StatHour;

import java.util.List;

/**
 * <p>
 * 小时统计表 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-09-21
 */
public interface IStatHourService extends IService<StatHour> {

    /**
     * 根据id范围查询统计数据
     *
     * @param minId 最小id
     * @param maxId 最大id
     * @return 统计数据
     */
    List<StatHour> queryListByIdRange(Long minId, Long maxId);

    /**
     * 按日统计15天内的每个小时数据，并保存到数据库
     */
    void statAndSaveData();
}
