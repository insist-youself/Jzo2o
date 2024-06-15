package com.jzo2o.market.controller.operation;

import com.jzo2o.api.market.dto.response.CouponUseResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.market.model.dto.request.ActivityQueryForPageReqDTO;
import com.jzo2o.market.model.dto.request.ActivitySaveReqDTO;
import com.jzo2o.market.model.dto.request.CouponOperationPageQueryReqDTO;
import com.jzo2o.market.model.dto.response.ActivityInfoResDTO;
import com.jzo2o.market.model.dto.response.CouponInfoResDTO;
import com.jzo2o.market.service.IActivityService;
import com.jzo2o.market.service.ICouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author linger
 * @date 2024/6/11 15:33
 */

@Validated
@RestController("operationCouponController")
@RequestMapping("/operation/coupon")
@Api(tags = "运营端 - 优惠券管理相关接口")
public class CouponController {

    @Resource
    private ICouponService couponService;

    @GetMapping("/page")
    @ApiOperation("根据活动ID查询优惠券领取记录")
    public PageResult<CouponInfoResDTO> page(CouponOperationPageQueryReqDTO couponOperationPageQueryReqDTO) {
        return couponService.queryByActivityId(couponOperationPageQueryReqDTO);
    }

}
