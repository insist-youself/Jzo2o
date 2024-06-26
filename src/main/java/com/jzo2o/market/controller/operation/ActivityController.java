package com.jzo2o.market.controller.operation;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.market.model.dto.request.ActivityQueryForPageReqDTO;
import com.jzo2o.market.model.dto.request.ActivitySaveReqDTO;
import com.jzo2o.market.model.dto.response.ActivityInfoResDTO;
import com.jzo2o.market.service.IActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("operationActivityController")
@RequestMapping("/operation/activity")
@Api(tags = "运营端-活动相关接口")
public class ActivityController {

    @Resource
    private IActivityService activityService;

    @GetMapping("/page")
    @ApiOperation("运营端分页查询活动")
    public PageResult<ActivityInfoResDTO> queryForPage(ActivityQueryForPageReqDTO activityQueryForPageReqDTO) {
        return activityService.queryForPage(activityQueryForPageReqDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation("查询活动详情")
    @ApiImplicitParam(name = "id", value = "活动id", required = true, dataTypeClass = Long.class)
    public ActivityInfoResDTO getDetail(@PathVariable("id") Long id) {
        return activityService.queryById(id);
    }

    @PostMapping("/save")
    @ApiOperation("活动保存")
    public void save(@Validated @RequestBody ActivitySaveReqDTO activitySaveReqDTO) {
        activityService.save(activitySaveReqDTO);
    }

    @PostMapping("/revoke/{id}")
    @ApiOperation("活动撤销")
    @ApiImplicitParam(name = "id", value = "活动id", required = true, dataTypeClass = Long.class)
    public void revoke(@PathVariable("id") Long id) {
        activityService.revoke(id);

    }


}
