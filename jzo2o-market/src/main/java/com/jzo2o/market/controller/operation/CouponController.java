package com.jzo2o.market.controller.operation;


import com.jzo2o.common.model.PageResult;
import com.jzo2o.market.model.dto.request.CouponOperationPageQueryReqDTO;
import com.jzo2o.market.model.dto.response.CouponInfoResDTO;
import com.jzo2o.market.service.ICouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("operationCouponController")
@RequestMapping("/operation/coupon")
@Api(tags = "运营端-优惠券相关接口")
public class CouponController {

    @Resource
    private ICouponService couponService;

    @GetMapping("/page")
    @ApiOperation("根据活动id查询")
    public PageResult<CouponInfoResDTO> queryForPage(CouponOperationPageQueryReqDTO couponOperationPageQueryReqDTO) {
        return couponService.queryForPageOfOperation(couponOperationPageQueryReqDTO);
    }
}
