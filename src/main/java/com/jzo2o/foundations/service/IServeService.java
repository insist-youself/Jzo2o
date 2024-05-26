package com.jzo2o.foundations.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
public interface IServeService extends IService<Serve> {

    /**
     * 查询区域服务信息并进行缓存
     * @param id 对应serve表的主键
     * @return 区域服务信息
     */

    Serve queryServeByIdCache(Long id);

    /**
     * 分页查询服务列表
     * @param servePageQueryReqDTO 查询条件
     * @return 分页结果
     */
    PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO);

    /**
     * 批量新增
     *
     * @param serveUpsertReqDTOList 批量新增数据
     */
    void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList);

    /**
     * 服务价格修改
     *
     * @param id    服务id
     * @param price 价格
     * @return 服务
     */
    Serve update(Long id, BigDecimal price);

    /**
     * 删除服务
     *
     * @param id 服务id
     */
    void deleteById(Long id);

    /**
     * 上架
     *
     * @param id         服务id
     */
    Serve onSale(Long id);

    /**
     * 下架
     *
     * @param id         服务id
     */
    Serve offSale(Long id);

    /**
     * 服务设置热门/取消
     *
     * @param id   服务id
     * @param flag 是否为热门，0：非热门，1：热门
     */
    void changeHotStatus(Long id, Integer flag);


    /**
     * 根据区域id和售卖状态查询关联服务数量
     *
     * @param regionId   区域id
     * @param saleStatus 售卖状态，0：草稿，1下架，2上架。可传null，即查询所有状态
     * @return 服务数量
     */
    int queryServeCountByRegionIdAndSaleStatus(Long regionId, Integer saleStatus);

    /**
     * 根据服务项id和售卖状态查询关联服务数量
     *
     * @param  serveItemId  服务项id
     * @param saleStatus 售卖状态，0：草稿，1下架，2上架。可传null，即查询所有状态
     * @return 服务数量
     */
    int queryServeCountByServeItemIdAndSaleStatus(Long serveItemId, Integer saleStatus);

}
