package com.jzo2o.customer.controller.worker;

import com.jzo2o.customer.model.dto.request.EvaluationPageByTargetReqDTO;
import com.jzo2o.customer.model.dto.response.EvaluationAndOrdersResDTO;
import com.jzo2o.customer.model.dto.response.EvaluationResDTO;
import com.jzo2o.customer.service.EvaluationService;
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
 * 评价相关接口
 *
 * @author itcast
 * @create 2023/9/11 16:14
 **/
@RestController("workerEvaluationController")
@RequestMapping("/worker/evaluation")
@Api(tags = "服务端 - 评价相关接口")
public class EvaluationController {
    @Resource
    private EvaluationService evaluationService;

    @GetMapping("/pageByCurrentUserAndScoreLevel")
    @ApiOperation("根据评价等级分页查询当前用户评价列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scoreLevel", value = "评价等级，1差评，2中评，3好评", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageNo", value = "页码，默认为1", defaultValue = "1", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "pageSize", value = "页面大小，默认为10", defaultValue = "10", dataTypeClass = Integer.class)
    })
    public List<EvaluationAndOrdersResDTO> pageByCurrentUserAndScoreLevel(@RequestParam(value = "scoreLevel", required = false) Integer scoreLevel,
                                                                          @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return evaluationService.pageByCurrentUserAndScoreLevel(scoreLevel,pageNo, pageSize);
    }
}
