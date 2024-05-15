package com.jzo2o.customer.controller.inner;


import com.jzo2o.api.customer.ServeProviderApi;
import com.jzo2o.api.customer.dto.response.ServeProviderResDTO;
import com.jzo2o.api.customer.dto.response.ServeProviderSimpleResDTO;
import com.jzo2o.customer.service.IServeProviderService;
import com.jzo2o.customer.service.IServeProviderSettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务人员/机构表 前端控制器
 * </p>
 *
 * @author itcast
 * @since 2023-07-17
 */
@RestController
@RequestMapping("/inner/serve-provider")
@Api(tags = "内部接口 - 服务人员、机构相关接口")
public class InnerServeProviderController implements ServeProviderApi {
    @Resource
    private IServeProviderService serveProviderService;

    @Resource
    private IServeProviderSettingsService serveProviderSettingsService;


    @Override
    @GetMapping("/{id}")
    @ApiOperation("服务人员/机构详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务人员/机构id", required = true, dataTypeClass = Long.class)
    })
    public ServeProviderResDTO getDetail(@PathVariable("id") Long id) {
        return serveProviderService.findServeProviderInfo(id);
    }

    @Override
    @GetMapping("/batchCityCode")
    @ApiOperation("批量获取服务人员或机构所在城市编码")
    public Map<Long, String> batchCityCode(@RequestParam(value = "ids") List<Long> ids) {
        return serveProviderSettingsService.findManyCityCodeOfServeProvider(ids);
    }

    @Override
    public Map<Long, Integer> batchGetProviderType(List<Long> ids) {
        return null;
    }

    @Override
    public List<ServeProviderSimpleResDTO> batchGet(List<Long> ids) {
        return serveProviderService.batchGet(ids);
    }
}
