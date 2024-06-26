package com.jzo2o.statemachine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jzo2o.statemachine.model.BizSnapshot;
import org.apache.ibatis.annotations.Mapper;

/**
 * 业务数据快照数据层
 *
 * @author itcast
 * @create 2023/8/5 15:36
 **/
@Mapper
public interface BizSnapshotMapper extends BaseMapper<BizSnapshot> {
}
