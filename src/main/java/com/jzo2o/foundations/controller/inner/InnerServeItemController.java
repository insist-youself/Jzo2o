package com.jzo2o.foundations.controller.inner;


import com.jzo2o.api.foundations.ServeItemApi;
import com.jzo2o.api.foundations.dto.response.ServeItemResDTO;
import com.jzo2o.api.foundations.dto.response.ServeItemSimpleResDTO;
import com.jzo2o.api.foundations.dto.response.ServeTypeCategoryResDTO;
import com.jzo2o.foundations.service.IServeItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 内部接口 - 服务项相关接口
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
@RestController
@RequestMapping("/inner/serve-item")
@Api(tags = "内部接口 - 服务项相关接口")
public class InnerServeItemController implements ServeItemApi {
    @Resource
    private IServeItemService serveItemService;

    @Override
    @GetMapping("/{id}")
    @ApiOperation("根据id查询服务项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务项id", required = true, dataTypeClass = Long.class)
    })
    public ServeItemResDTO findById(@PathVariable("id") Long id) {
        return serveItemService.queryServeItemAndTypeById(id);
    }

    @Override
    @GetMapping("/listByIds")
    @ApiOperation("根据id列表查询服务项")
    public List<ServeItemSimpleResDTO> listByIds(@RequestParam("ids") List<Long> ids) {
        return serveItemService.queryServeItemListByIds(ids);
    }

    @Override
    @GetMapping("/queryActiveServeItemCategory")
    @ApiOperation("查询启用状态的服务项目录")
    public List<ServeTypeCategoryResDTO> queryActiveServeItemCategory() {
        return serveItemService.queryActiveServeItemCategory();
    }
}
