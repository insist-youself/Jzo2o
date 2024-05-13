package com.jzo2o.foundations.controller.inner;


import com.jzo2o.api.foundations.RegionApi;
import com.jzo2o.api.foundations.dto.response.ConfigRegionInnerResDTO;
import com.jzo2o.api.foundations.dto.response.RegionServeInfoResDTO;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.foundations.model.dto.response.ConfigRegionResDTO;
import com.jzo2o.foundations.service.IConfigRegionService;
import com.jzo2o.foundations.service.IRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 区域表 前端控制器
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
@RestController
@RequestMapping("/inner/region")
@Api(tags = "内部接口 - 区域相关接口")
public class InnerRegionController implements RegionApi {
    @Resource
    private IConfigRegionService configRegionService;

    @Override
    @GetMapping("/findAllConfigRegion")
    @ApiOperation("查询所有区域配置")
    public List<ConfigRegionInnerResDTO> findAll() {
        return configRegionService.queryAll();
    }

    @Override
    @GetMapping("/findConfigRegionById/{id}")
    @ApiOperation("根据区域id查询区域配置")
    @ApiImplicitParam(name = "id", value = "区域id", required = true, dataTypeClass = Long.class)
    public ConfigRegionInnerResDTO findConfigRegionById(@PathVariable("id") Long id) {
        ConfigRegionResDTO configRegionResDTO = configRegionService.queryById(id);
        return BeanUtils.toBean(configRegionResDTO, ConfigRegionInnerResDTO.class);
    }

    @Override
    @GetMapping("/findConfigRegionByCityCode")
    @ApiOperation("根据城市编码获取区域配置信息")
    @ApiImplicitParam(name = "cityCode", value = "城市编码", required = true, dataTypeClass = String.class)
    public ConfigRegionInnerResDTO findConfigRegionByCityCode(@RequestParam("cityCode") String cityCode) {
        return configRegionService.queryByCityCode(cityCode);
    }
}
