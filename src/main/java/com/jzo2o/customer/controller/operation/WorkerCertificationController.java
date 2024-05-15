package com.jzo2o.customer.controller.operation;

import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.customer.model.dto.response.WorkerCertificationResDTO;
import com.jzo2o.customer.service.IWorkerCertificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author itcast
 */
@RestController("operationWorkerCertificationController")
@RequestMapping("/operation/worker-certification")
@Api(tags = "运营端 - 服务人员认证信息相关接口")
public class WorkerCertificationController {

    @Resource
    private IWorkerCertificationService workerCertificationService;

    @GetMapping("/{id}")
    @ApiOperation("根据服务人员id查询认证信息")
    public WorkerCertificationResDTO queryById(@PathVariable("id") Long id) {
        return BeanUtil.toBean(workerCertificationService.getById(id), WorkerCertificationResDTO.class);
    }
}
