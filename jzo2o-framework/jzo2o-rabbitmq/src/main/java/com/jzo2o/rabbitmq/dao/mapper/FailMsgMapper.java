package com.jzo2o.rabbitmq.dao.mapper;

import com.jzo2o.rabbitmq.domain.FailMsg;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * 失败消息 Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2023-07-11
 */
public interface FailMsgMapper {
    String BASE_COLUMNS = " id,exchange,routing_key as 'routingKey',msg,reason,delay_msg_execute_time as delayMsgExecuteTime,create_time as createTime, update_time as updateTime,next_fetch_time as nextFetchTime ";

    @Insert("insert into fail_msg(id,exchange,routing_key,msg,reason,delay_msg_execute_time) values(#{domain.id}," +
            "#{domain.exchange},#{domain.routingKey},#{domain.msg},#{domain.reason},#{domain.delayMsgExecuteTime})")
    int insert(@Param("domain") FailMsg failMsg);

    @Select("select" + BASE_COLUMNS + "from fail_msg where id=#{id} limit 1")
    List<FailMsg> queryById(@Param("id") Long id);

    @Update("update fail_msg set exchange=#{domain.exchange},routing_key=#{domain.routingKey},msg=#{domain.msg},reason=#{domain.reason},delay_msg_execute_time=#{domain.delayMsgExecuteTime}" +
            " where id=#{domain.id}")
    int updateById(@Param("domain") FailMsg failMsg);


    @Update("<script>update fail_msg set next_fetch_time=next_fetch_time+#{time} where id in (<foreach collection='ids' item='id' separator=','>#{id}</foreach>)</script>")
    void batchUpdateExecuteTime(@Param("time") Integer time, @Param("ids") List<Long> ids);

    /**
     * 查询要执行的前n条数据
     * @param fetchTime 执行时间
     * @param num 获取num条数据
     * @return 执行失败记录
     */
    @Select("select" + BASE_COLUMNS + "from fail_msg where next_fetch_time<=#{fetchTime} limit #{num} ")
    List<FailMsg> fetch(@Param("fetchTime") Integer fetchTime, @Param("num") Integer num);

    @Delete("delete from fail_msg where id=#{id}")
    int deleteById(@Param("id") Long id);
}
