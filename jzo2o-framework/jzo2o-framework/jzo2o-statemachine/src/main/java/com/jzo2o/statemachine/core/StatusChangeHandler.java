package com.jzo2o.statemachine.core;

/**
 * 状态变化处理器
 * 说明:
 * 1、功能说明:状态变更时执行的业务逻辑
 * 2、接口实现类的bean名称规则为:状态机名称_状态变更事件名称
 * 例如：在实现类上添加 @Component("order_close_dispatching_order") order为状态机名称，close_dispatching_order为取消正常派单订单的事件名称
 *
 * @author itcast
 */
public interface StatusChangeHandler<T extends StateMachineSnapshot> {

    /**
     * 状态变化处理逻辑
     *
     * @param bizId 业务id
     * @param statusChangeEventEnum 状态变更事件
     * @param bizSnapshot 快照
     */
    void handler(String bizId,StatusChangeEvent statusChangeEventEnum,T bizSnapshot);
}
