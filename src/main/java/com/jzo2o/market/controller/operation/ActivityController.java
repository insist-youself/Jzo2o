package com.jzo2o.market.controller.operation;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.market.model.dto.request.ActivityQueryForPageReqDTO;
import com.jzo2o.market.model.dto.request.ActivitySaveReqDTO;
import com.jzo2o.market.model.dto.request.CouponOperationPageQueryReqDTO;
import com.jzo2o.market.model.dto.response.ActivityInfoResDTO;
import com.jzo2o.market.service.IActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author linger
 * @date 2024/6/11 15:33
 */

@Validated
@RestController("operationActivityController")
@RequestMapping("/operation/activity")
@Api(tags = "运营端 - 优惠券活动管理相关接口")
public class ActivityController {

    @Resource
    private IActivityService activityService;

    @PostMapping("/save")
    @ApiOperation("保存优惠券活动")
    public void saveOrUpdateActivity(@RequestBody ActivitySaveReqDTO activitySaveReqDTO) {
        activityService.saveOrUpActivity(activitySaveReqDTO);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询优惠券活动")
    public PageResult<ActivityInfoResDTO> page(ActivityQueryForPageReqDTO activityQueryForPageReqDTO) {
       return activityService.queryForPage(activityQueryForPageReqDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation("查询优惠券活动详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动id", required = true, dataTypeClass = Long.class)
    })
    public ActivityInfoResDTO queryByActivityId(@PathVariable("id") Long id) {
        return activityService.queryByActivityId(id);
    }

    @PostMapping("/revoke/{id}")
    @ApiOperation("撤销活动")
    public void revokeById(@PathVariable("id") Long id) {
        activityService.revokeById(id);
    }
}
