package com.jzo2o.market.service;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.market.model.domain.Activity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.market.model.dto.request.ActivityQueryForPageReqDTO;
import com.jzo2o.market.model.dto.request.ActivitySaveReqDTO;
import com.jzo2o.market.model.dto.response.ActivityInfoResDTO;
import com.jzo2o.market.model.dto.response.SeizeCouponInfoResDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itcast
 * @since 2023-09-16
 */
public interface IActivityService extends IService<Activity> {


    /**
     *  保存优惠券活动
     * @param activitySaveReqDTO 活动保存请求模型
     */
    void saveOrUpActivity(ActivitySaveReqDTO activitySaveReqDTO);

    /**
     * 分页查询优惠券活动
     * @param activityQueryForPageReqDTO ActivityQueryForPageReqDTO
     * @return 活动分页字段模型
     */
    PageResult<ActivityInfoResDTO> queryForPage(ActivityQueryForPageReqDTO activityQueryForPageReqDTO);

    /**
     * 查询优惠券活动详情
     * @param id 活动id
     * @return 活动分页字段模型
     */
    ActivityInfoResDTO queryByActivityId(Long id);

    /**
     *  撤销活动
     * @param id 活动id
     */
    void revokeById(Long id);
}
