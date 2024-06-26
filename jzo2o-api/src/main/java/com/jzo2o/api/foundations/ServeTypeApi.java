package com.jzo2o.api.foundations;

import com.jzo2o.api.foundations.dto.response.ServeTypeSimpleResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 内部接口 - 服务类型相关接口
 *
 * @author itcast
 */
@FeignClient(contextId = "jzo2o-foundations", value = "jzo2o-foundations", path = "/foundations/inner/serve-type")
public interface ServeTypeApi {

    @GetMapping("/listByIds")
    List<ServeTypeSimpleResDTO> listByIds(@RequestParam("ids") List<Long> ids);
}
