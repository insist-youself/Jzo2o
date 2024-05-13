package com.jzo2o.foundations.controller.worker;

import com.jzo2o.api.foundations.dto.response.ServeTypeSimpleResDTO;
import com.jzo2o.foundations.service.IServeTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务类型相关接口
 *
 * @author itcast
 * @create 2023/7/26 14:16
 **/
@RestController("workerServeTypeController")
@RequestMapping("/worker/serve-type")
@Api(tags = "服务端 - 服务类型相关接口")
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
}
