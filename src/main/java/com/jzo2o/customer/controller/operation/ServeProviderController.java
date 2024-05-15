package com.jzo2o.customer.controller.operation;


import com.jzo2o.api.customer.dto.request.ServerProviderUpdateStatusReqDTO;
import com.jzo2o.api.customer.dto.response.ServeProviderResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.dto.request.ServeProviderPageQueryReqDTO;
import com.jzo2o.customer.model.dto.response.ServeProviderBasicInformationResDTO;
import com.jzo2o.customer.model.dto.response.ServeProviderListResDTO;
import com.jzo2o.customer.service.IServeProviderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 服务人员/机构相关接口
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
@Validated
@RestController("operationServeProviderController")
@RequestMapping("/operation/serve-provider")
@Api(tags = "运营端 - 服务人员或机构相关接口")
public class ServeProviderController {
    @Resource
    private IServeProviderService serveProviderService;

    @GetMapping("/pageQueryAgency")
    @ApiOperation("机构分页查询")
    public PageResult<ServeProviderListResDTO> pageQueryAgency(ServeProviderPageQueryReqDTO serveProviderPageQueryReqDTO) {
        return serveProviderService.pageQueryAgency(serveProviderPageQueryReqDTO);
    }

    @GetMapping("/pageQueryWorker")
    @ApiOperation("服务人员分页查询")
    public PageResult<ServeProviderListResDTO> pageQueryWorker(ServeProviderPageQueryReqDTO serveProviderPageQueryReqDTO) {
        return serveProviderService.pageQueryWorker(serveProviderPageQueryReqDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation("服务人员/机构详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务人员/机构id", required = true, dataTypeClass = Long.class)
    })
    public ServeProviderResDTO detail(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return serveProviderService.findServeProviderInfo(id);
    }

    @PutMapping("/updateStatus")
    @ApiOperation("账号冻结/解冻")
    public void updateStatus(@RequestBody ServerProviderUpdateStatusReqDTO serverProviderUpdateStatusReqDTO) {
        serveProviderService.updateStatus(serverProviderUpdateStatusReqDTO);
    }

    @GetMapping("/basicInformation/{id}")
    @ApiOperation("服务人员/机构基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务人员/机构id", required = true, dataTypeClass = Long.class)
    })
    public ServeProviderBasicInformationResDTO findBasicInformationById(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return serveProviderService.findBasicInformationById(id);
    }
}
