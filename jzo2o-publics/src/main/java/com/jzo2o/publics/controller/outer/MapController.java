package com.jzo2o.publics.controller.outer;

import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.publics.model.dto.response.MapLocationResDTO;
import com.jzo2o.thirdparty.core.map.MapService;
import com.jzo2o.thirdparty.dto.MapLocationDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * 用户端 - 地址相关接口
 *
 * @author itcast
 * @create 2023/7/10 09:57
 **/
@RestController
@RequestMapping("/map")
@Api(tags = "地图服务相关接口")
public class MapController {
    @Resource
    private MapService mapService;

    @GetMapping("/regeo")
    @ApiOperation("根据经纬度查询地址信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "location", value = "经纬度", required = true, dataTypeClass = String.class)
    })
    public MapLocationResDTO getCityCodeByLocation(@NotNull(message = "坐标不能为空") @RequestParam("location") String location) {
        MapLocationDTO mapLocationDTO = mapService.getCityCodeByLocation(location);
        return BeanUtil.toBean(mapLocationDTO, MapLocationResDTO.class);
    }
}
