package com.jzo2o.customer.controller.operation;

import com.jzo2o.api.customer.dto.request.EvaluationSubmitReqDTO;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.customer.model.dto.request.AuditReqDTO;
import com.jzo2o.customer.model.dto.request.EvaluationPageByTargetReqDTO;
import com.jzo2o.customer.model.dto.request.LikeOrCancelReqDTO;
import com.jzo2o.customer.model.dto.response.EvaluationAndOrdersResDTO;
import com.jzo2o.customer.model.dto.response.EvaluationResDTO;
import com.jzo2o.customer.model.dto.response.EvaluationTokenDto;
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
@RestController("operationEvaluationController")
@RequestMapping("/operation/evaluation")
@Api(tags = "运营端 - 评价相关接口")
public class EvaluationController {
    @Resource
    private EvaluationService evaluationService;


    @GetMapping("/token")
    @ApiOperation("获取评价系统token")
    public EvaluationTokenDto getToken() {
        return evaluationService.getEvaluationInfo();
    }
}
