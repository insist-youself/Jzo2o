package com.jzo2o.foundations.controller.operation;


import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.api.foundations.dto.response.RegionSimpleResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.dto.request.RegionPageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.RegionUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.RegionResDTO;
import com.jzo2o.foundations.service.IRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 区域表 前端控制器
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
@Validated
@RestController("operationRegionController")
@RequestMapping("/operation/region")
@Api(tags = "运营端 - 区域相关接口")
public class RegionController {
    @Resource
    private IRegionService regionService;

    @GetMapping("/activeRegionList")
    @ApiOperation("已开通服务区域列表")
    public List<RegionSimpleResDTO> activeRegionList() {
        return regionService.queryActiveRegionList();
    }

    @PostMapping
    @ApiOperation("区域新增")
    public void add(@RequestBody RegionUpsertReqDTO regionUpsertReqDTO) {
        regionService.add(regionUpsertReqDTO);
    }

    @PutMapping("/{id}")
    @ApiOperation("区域修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "区域id", required = true, dataTypeClass = Long.class),
            @ApiImplicitParam(name = "managerName", value = "负责人名称", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "managerPhone", value = "负责人电话", required = true, dataTypeClass = String.class)
    })
    public void update(@NotNull(message = "id不能为空") @PathVariable("id") Long id,
                       @RequestParam("managerName") String managerName,
                       @RequestParam("managerPhone") String managerPhone) {
        regionService.update(id, managerName, managerPhone);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("区域删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "区域id", required = true, dataTypeClass = Long.class)
    })
    public void delete(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        regionService.deleteById(id);
    }

    @GetMapping("/page")
    @ApiOperation("区域分页查询")
    public PageResult<RegionResDTO> page(RegionPageQueryReqDTO regionPageQueryReqDTO) {
        return regionService.page(regionPageQueryReqDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "区域id", required = true, dataTypeClass = Long.class)
    })
    public RegionResDTO findById(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        Region region = regionService.getById(id);
        return BeanUtil.toBean(region, RegionResDTO.class);
    }

    @PutMapping("/activate/{id}")
    @ApiOperation("区域启用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "区域id", required = true, dataTypeClass = Long.class),
    })
    public void activate(@PathVariable("id") Long id) {
        regionService.active(id);
    }

    @PutMapping("/deactivate/{id}")
    @ApiOperation("区域禁用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "区域id", required = true, dataTypeClass = Long.class),
    })
    public void deactivate(@PathVariable("id") Long id) {
        regionService.deactivate(id);
    }

    @PutMapping("/refreshRegionRelateCaches/{id}")
    @ApiOperation("刷新区域相关缓存")
    public void refreshRegionRelateCaches(@PathVariable("id") Long id) {
        //todo
//        homeService.refreshRegionRelateCaches(id);
    }
}
