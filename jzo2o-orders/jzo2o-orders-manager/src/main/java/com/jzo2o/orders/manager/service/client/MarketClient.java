package com.jzo2o.orders.manager.service.client;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.jzo2o.api.market.CouponApi;
import com.jzo2o.api.market.dto.response.AvailableCouponsResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 *
 * 调用market的客户端类
 * @author linger
 * @date 2024/6/18 21:15
 */
@Component
@Slf4j
public class MarketClient {

    @Resource
    private CouponApi couponApi;


    @SentinelResource(value = "getAvailableByCouponApi", fallback = "getAvailableFallback", blockHandler = "getAvailableBlockHandler")
    public List<AvailableCouponsResDTO> getAvailable(BigDecimal totalAmount) {

        log.error("查询可用优惠券,订单金额:{}",totalAmount);
        //调用可用优惠券
        List<AvailableCouponsResDTO> available = couponApi.getAvailable(totalAmount);
        return available;
    }

    //执行异常走
    public List<AvailableCouponsResDTO> getAvailableFallback(BigDecimal totalAmount, Throwable throwable) {
        log.error("非限流、熔断等导致的异常执行的降级方法，totalAmount:{},throwable:", totalAmount, throwable);
        return Collections.emptyList();
    }

    //熔断后的降级逻辑
    public List<AvailableCouponsResDTO> getAvailableBlockHandler(BigDecimal totalAmount, BlockException blockException) {
        log.error("触发限流、熔断时执行的降级方法，totalAmount:{},blockException:", totalAmount, blockException);
        return Collections.emptyList();
    }

}
