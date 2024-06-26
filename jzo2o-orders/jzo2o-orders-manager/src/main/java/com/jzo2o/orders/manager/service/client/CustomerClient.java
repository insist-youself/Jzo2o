package com.jzo2o.orders.manager.service.client;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.jzo2o.api.customer.AddressBookApi;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 调用customer的客户端类
 *
 * @author linger
 * @date 2024/5/26 22:07
 */
@Component
@Slf4j
public class CustomerClient {

    @Resource
    private AddressBookApi addressBookApi;

    /**
     * SentinelResource注解的属性说明：
     * value: 用于定义资源的名称，即 Sentinel 会对该资源进行流量控制和熔断降级。
     * fallback ：非限流、熔断等导致的异常执行的降级方法
     * blockHandler :触发限流、熔断时执行的降级方法
     * @param id
     * @return
     */
    @SentinelResource(value = "getAddressBookDetail", fallback = "detailFallback", blockHandler = "detailBlockHandler")
    public AddressBookResDTO getDetail(Long id) {
        log.info("根据id查询地址，id:{}", id);
        // 调用其他微服务方法
        AddressBookResDTO detail = addressBookApi.detail(id);
        return detail;
    }

    // 执行异常走
    public AddressBookResDTO detailFallback(Long id, Throwable throwable) {
        log.info("非限流、熔断等导致的异常执行的降级方法，id:{}, throwable:", id, throwable);
        return null;
    }

    // 熔断后的降级逻辑
    public AddressBookResDTO detailBlockHandler(Long id, BlockException blockException) {
        log.info("触发限流、熔断时执行的降级方法，id:{}, blockException:", id, blockException);
        return null;
    }
}
