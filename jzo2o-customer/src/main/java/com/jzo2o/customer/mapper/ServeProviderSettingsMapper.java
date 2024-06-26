package com.jzo2o.customer.mapper;

import com.jzo2o.customer.model.domain.ServeProviderSettings;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 服务人员/机构附属信息 Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2023-07-20
 */
public interface ServeProviderSettingsMapper extends BaseMapper<ServeProviderSettings> {

    @Select("<script>select id,city_code as cityCode from serve_provider_settings where id in (<foreach collection='ids' item='id' separator=','>#{id}</foreach>)</script>")
    List<ServeProviderSettings> batchQueryCityCodeByIds(@Param("ids") List<Long> ids);

}
