package com.jzo2o.foundations.controller.operation;

import com.jzo2o.api.foundations.dto.response.ServeTypeSimpleResDTO;
import com.jzo2o.common.enums.EnableStatusEnum;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.dto.request.ServeTypePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeTypeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeTypeResDTO;
import com.jzo2o.foundations.service.IServeTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 服务类型相关接口
 *
 * @author itcast
 * @create 2023/7/26 14:16
 **/
@RestController("operationServeTypeController")
@RequestMapping("/operation/serve-type")
@Api(tags = "运营端 - 服务类型相关接口")
public class ServeTypeController {
    @Resource
    private IServeTypeService serveTypeService;

    @GetMapping("/queryServeTypeListByActiveStatus")
    @ApiOperation("根据活动状态查询服务类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activeStatus", value = "活动状态，0：草稿，1：禁用，:2：启用", dataTypeClass = Integer.class)
    })
    public List<ServeTypeSimpleResDTO> queryServeTypeListByActiveStatus(@RequestParam(value = "activeStatus", required = false) Integer activeStatus) {
        return serveTypeService.queryServeTypeListByActiveStatus(activeStatus);
    }

    @PostMapping
    @ApiOperation("服务类型新增")
    public void add(@RequestBody ServeTypeUpsertReqDTO serveTypeUpsertReqDTO) {
        serveTypeService.add(serveTypeUpsertReqDTO);
    }

    @PutMapping("/{id}")
    @ApiOperation("服务类型修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务类型id", required = true, dataTypeClass = Long.class)
    })
    public void update(@NotNull(message = "id不能为空") @PathVariable("id") Long id,
                       @RequestBody ServeTypeUpsertReqDTO serveTypeUpsertReqDTO) {
        serveTypeService.update(id, serveTypeUpsertReqDTO);
    }

    @PutMapping("/activate/{id}")
    @ApiOperation("服务类型启用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务类型id", required = true, dataTypeClass = Long.class),
    })
    public void activate(@PathVariable("id") Long id) {
        serveTypeService.activate(id);
    }

    @PutMapping("/deactivate/{id}")
    @ApiOperation("服务类型禁用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务类型id", required = true, dataTypeClass = Long.class),
    })
    public void deactivate(@PathVariable("id") Long id) {
        serveTypeService.deactivate(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("服务类型删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务类型id", required = true, dataTypeClass = Long.class)
    })
    public void delete(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        serveTypeService.deleteById(id);
    }

    @GetMapping("/page")
    @ApiOperation("服务类型分页查询")
    public PageResult<ServeTypeResDTO> page(ServeTypePageQueryReqDTO serveTypePageQueryReqDTO) {
        return serveTypeService.page(serveTypePageQueryReqDTO);
    }
}
