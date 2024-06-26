package com.jzo2o.foundations.controller.inner;

import com.jzo2o.api.foundations.ServeApi;
import com.jzo2o.api.foundations.dto.response.ServeAggregationResDTO;
import com.jzo2o.foundations.service.IServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author itcast
 */
@RestController
@RequestMapping("/inner/serve")
@Api(tags = "内部接口 - 服务相关接口")
public class InnerServeController implements ServeApi {
    @Resource
    private IServeService serveService;

    @Override
    @GetMapping("/{id}")
    @ApiOperation("根据id查询服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务项id", required = true, dataTypeClass = Long.class)
    })
    public ServeAggregationResDTO findById(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return serveService.findServeDetailById(id);
    }
}
