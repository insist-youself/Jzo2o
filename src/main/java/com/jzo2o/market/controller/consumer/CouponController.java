package com.jzo2o.market.controller.consumer;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.market.model.dto.request.CouponOperationPageQueryReqDTO;
import com.jzo2o.market.model.dto.response.CouponInfoResDTO;
import com.jzo2o.market.service.ICouponService;
import com.jzo2o.mvc.utils.UserContext;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author linger
 * @date 2024/6/11 15:33
 */

@Validated
@RestController("consumerCouponController")
@RequestMapping("/consumer/coupon")
@Api(tags = "运营端 - 优惠券管理相关接口")
public class CouponController {

    @Resource
    private ICouponService couponService;

    @GetMapping("/my")
    @ApiOperation("用户查询自己领取的优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "优惠券状态，1：未使用，2：已使用，3：已过期", required = true, dataTypeClass = Long.class),
            @ApiImplicitParam(name = "lastId", value = "上一次查询最后一张优惠券id", required = true, dataTypeClass = Long.class)
    })
    public List<CouponInfoResDTO> myCoupon(Long status, Long lastId) {
        Long userId = UserContext.currentUserId();
        return couponService.queryMyCoupon(userId, status, lastId);
    }

}
