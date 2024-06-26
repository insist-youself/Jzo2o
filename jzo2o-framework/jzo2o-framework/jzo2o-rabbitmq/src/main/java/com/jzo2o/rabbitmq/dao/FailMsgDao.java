package com.jzo2o.rabbitmq.dao;


import com.jzo2o.rabbitmq.domain.FailMsg;

import java.util.List;

public interface FailMsgDao {
    /**
     * 保存失败消息
     *
     * @param failMsg 失败消息
     */
    void save(FailMsg failMsg);

    /**
     *
     * @param id
     * @param exchange
     * @param routingKey
     * @param msg
     * @param delay
     * @param failMessage
     */
    void save(Long id, String exchange, String routingKey, Object msg, Integer delay, Integer nextFetchTime, String failMessage);

    /**
     * 获取当前可执行消息，并修改执行消息执行的时间，防止重复执行
     * @param limit 获取条数
     * @return
     */
    List<FailMsg> fetch(Integer limit);

    /**
     * 根据id删除
     *
     * @param id 失败消息id
     */
    void removeById(Long id);
}
