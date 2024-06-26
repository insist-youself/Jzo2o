package com.jzo2o.api.customer;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(contextId = "customer", value = "jzo2o-customer")
public interface CustomerApi {



}
