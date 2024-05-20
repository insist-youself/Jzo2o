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
 * 服务表 服务类
 * </p>
 *
 * @author linger
 * @since 2024-05-14
 */
public interface IServeService extends IService<Serve> {

    /**
     * 区域服务分页查询
     * @param servePageQueryReqDTO
     * @return
     */
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO);

    /**
     * 批量新增
     * @param serveUpsertReqDTOList
     */
    void  batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList);

    /**
     * 价格修改
     * @param id
     * @param price
     * @return
     */
    Serve update(Long id, BigDecimal price);


    /**
     * 服务上架
     *
     * @param id
     * @return
     */
    Serve onSale(Long id);

    /**
     * 区域服务删除
     * @param id
     */
    void deleteById(Long id);


    /**
     * 区域服务下架
     * @param id
     * @return
     */
    Serve offSale(Long id);

    /**
     * 区域服务设置热门
     * @param id
     */
    Serve onHot(Long id);

    /**
     * 区域服务取消热门
     * @param id
     */
    Serve offHot(Long id);
}
