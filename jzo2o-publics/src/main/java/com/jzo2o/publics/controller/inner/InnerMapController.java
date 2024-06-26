package com.jzo2o.publics.controller.inner;

import com.jzo2o.api.publics.MapApi;
import com.jzo2o.api.publics.dto.response.LocationResDTO;
import com.jzo2o.thirdparty.core.map.MapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 内部接口 - 地址相关接口
 *
 * @author itcast
 * @create 2023/7/10 09:57
 **/
@RestController
@RequestMapping("/inner/map")
@Api(tags = "内部接口 - 地图服务相关接口")
public class InnerMapController implements MapApi {
    @Resource
    private MapService mapService;

    @Override
    @GetMapping("/getLocationByAddress")
    @ApiOperation("根据地址查询经纬度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address", value = "地址", required = true, dataTypeClass = String.class)
    })
    public LocationResDTO getLocationByAddress(@RequestParam("address") String address) {
        String location = mapService.getLocationByAddress(address);
        return new LocationResDTO(location);
    }
}
