package com.jzo2o.statemachine.core;

/**
 * 状态变量事件抽象接口
 *
 * @author itcast
 */

public interface StatusChangeEvent {

    /**
     * 原始状态
     */
    StatusDefine getSourceStatus();

    /**
     * 变更后的状态
     */
    StatusDefine getTargetStatus();

    /**
     * @return 返回事件描述
     */
    String getDesc();

    /**
     * @return 返回事件代码
     */
    String getCode();

}