package com.jzo2o.foundations.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jzo2o.api.foundations.dto.response.ServeItemResDTO;
import com.jzo2o.api.foundations.dto.response.ServeTypeCategoryResDTO;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.domain.ServeType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 服务表 Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
public interface ServeItemMapper extends BaseMapper<ServeItem> {

//    @Select("SELECT type.*  FROM serve_type AS type \n" +
//            "LEFT JOIN serve_item AS item ON type.id = item.serve_type_id \n" +
//            "WHERE item.id = #{id}")
//    ServeType findServeTypeById(@Param("id") Long id);

    /**
     * 根据条件查询服务项列表
     *
     * @param serveTypeId  服务类型id
     * @param name         服务项名称
     * @param activeStatus 活动状态，0：草稿，1禁用，2启用
     * @return 服务项列表
     */
    List<ServeItemResDTO> queryList(@Param("serveTypeId") Long serveTypeId, @Param("name") String name, @Param("activeStatus") Integer activeStatus);

    /**
     * 根据id查询服务项和服务类型信息
     *
     * @param id 服务项id
     * @return 服务项和服务类型信息
     */
    ServeItemResDTO queryServeItemAndTypeById(@Param("id") Long id);

    /**
     * 根据服务id查询服务项
     *
     * @param id 服务id
     * @return 服务项
     */
    ServeItem queryServeItemByServeId(@Param("id") Long id);

    /**
     * 查询启用状态的服务项目录
     *
     * @return 服务项目录
     */
    List<ServeTypeCategoryResDTO> queryActiveServeItemCategory();
}
