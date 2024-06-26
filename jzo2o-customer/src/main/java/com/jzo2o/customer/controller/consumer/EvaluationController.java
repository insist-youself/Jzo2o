package com.jzo2o.customer.controller.consumer;

import com.jzo2o.api.customer.dto.request.EvaluationSubmitReqDTO;
import com.jzo2o.customer.model.dto.request.AuditReqDTO;
import com.jzo2o.customer.model.dto.request.EvaluationPageByTargetReqDTO;
import com.jzo2o.customer.model.dto.request.LikeOrCancelReqDTO;
import com.jzo2o.customer.model.dto.response.AllEvaluationSystemInfoResDTO;
import com.jzo2o.customer.model.dto.response.BooleanResDTO;
import com.jzo2o.customer.model.dto.response.EvaluationAndOrdersResDTO;
import com.jzo2o.customer.model.dto.response.EvaluationResDTO;
import com.jzo2o.customer.service.EvaluationService;
import com.jzo2o.mvc.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 评价相关接口
 *
 * @author itcast
 * @create 2023/9/11 16:14
 **/
@RestController("consumerEvaluationController")
@RequestMapping("/consumer/evaluation")
@Api(tags = "用户端 - 评价相关接口")
public class EvaluationController {
    @Resource
    private EvaluationService evaluationService;

    @PostMapping
    @ApiOperation("发表评价")
    public BooleanResDTO submit(@RequestBody EvaluationSubmitReqDTO evaluationSubmitReqDTO) {
        evaluationSubmitReqDTO.setCurrentUserInfo(UserContext.currentUser());
        return evaluationService.submit(evaluationSubmitReqDTO);
    }

    @GetMapping("/pageByTarget")
    @ApiOperation("根据对象属性分页查询评价列表")
    public List<EvaluationResDTO> pageByTargetId(EvaluationPageByTargetReqDTO evaluationPageByTargetReqDTO) {
        return evaluationService.pageByTarget(evaluationPageByTargetReqDTO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评价id", required = true, dataTypeClass = String.class)
    })
    public void delete(@PathVariable("id") String id) {
        evaluationService.delete(id);
    }

    @PostMapping("/likeOrCancel")
    @ApiOperation("点赞或取消点赞接口")
    public void likeOrCancel(@RequestBody LikeOrCancelReqDTO likeOrCancelReqDTO) {
        evaluationService.likeOrCancel(likeOrCancelReqDTO);
    }

    @PostMapping("/userReport")
    @ApiOperation("用户举报")
    public void userReport(@RequestBody AuditReqDTO auditReqDTO) {
        evaluationService.userReport(auditReqDTO);
    }

    @GetMapping("/pageByCurrentUser")
    @ApiOperation("分页查询当前用户评价列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码，默认为1", defaultValue = "1", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页面大小，默认为10", defaultValue = "10", dataTypeClass = Integer.class)
    })
    public List<EvaluationAndOrdersResDTO> pageByCurrentUser(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return evaluationService.pageByCurrentUser(pageNo, pageSize);
    }

    @GetMapping("/findAllSystemInfo")
    @ApiOperation("查询评价配置信息")
    public AllEvaluationSystemInfoResDTO findAllSystemInfo() {
        return evaluationService.findAllSystemInfo(UserContext.currentUser());
    }
}
