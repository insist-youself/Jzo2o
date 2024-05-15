package com.jzo2o.customer.controller.agency;

import com.jzo2o.customer.model.dto.request.ServePickUpReqDTO;
import com.jzo2o.customer.model.dto.request.ServeScopeSetReqDTO;
import com.jzo2o.customer.model.dto.response.ServeProviderSettingsGetResDTO;
import com.jzo2o.customer.model.dto.response.ServeSettingsStatusResDTO;
import com.jzo2o.customer.service.IServeProviderSettingsService;
import com.jzo2o.mvc.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 86188
 */

@RestController("agencyServeProviderSettingsController")
@RequestMapping("/agency/serve-settings")
@Api(tags = "机构端 - 服务设置相关接口")
public class ServeProviderSettingsController {

    @Resource
    private IServeProviderSettingsService serveProviderSettingsService;

    @PutMapping("/serve-scope")
    @ApiOperation("服务范围设置")
    public void setServeScope(@RequestBody @Validated ServeScopeSetReqDTO serveScopeSetReqDTO) {
        serveProviderSettingsService.setServeScope(serveScopeSetReqDTO);
    }

    @GetMapping
    @ApiOperation("获取设置")
    public ServeProviderSettingsGetResDTO getServeScope() {
        return serveProviderSettingsService.getServeScope();
    }

    @PutMapping("/pick-up")
    @ApiOperation("接单设置")
    public void setPickUp(@RequestBody ServePickUpReqDTO servePickUpReqDTO) {
//        serveProviderSettingsService.setPickUp(servePickUpReqDTO);
        serveProviderSettingsService.setPickUp(UserContext.currentUserId(), servePickUpReqDTO.getCanPickUp());
    }

    @GetMapping("/status")
    @ApiOperation("获取所有设置状态")
    public ServeSettingsStatusResDTO getStatus() {
        return serveProviderSettingsService.getSettingStatus();
    }
}
