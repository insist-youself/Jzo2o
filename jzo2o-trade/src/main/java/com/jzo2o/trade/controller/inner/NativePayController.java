package com.jzo2o.trade.controller.inner;

import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.api.trade.NativePayApi;
import com.jzo2o.api.trade.dto.request.NativePayReqDTO;
import com.jzo2o.api.trade.dto.response.NativePayResDTO;
import com.jzo2o.api.trade.enums.PayChannelEnum;
import com.jzo2o.trade.model.domain.Trading;
import com.jzo2o.trade.service.NativePayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Native支付方式Face接口：商户生成二维码，用户扫描支付
 *
 * @author itcast
 */
@Validated
@RestController("innerNativePayController")
@Api(tags = "内部接口 - Native支付")
@RequestMapping("/inner/native")
public class NativePayController implements NativePayApi {

    @Resource
    private NativePayService nativePayService;

    /***
     * 扫码支付，收银员通过收银台或商户后台调用此接口，生成二维码后，展示给用户，由用户扫描二维码完成订单支付。
     *
     * @param nativePayDTO 扫码支付提交参数
     * @return 扫码支付响应数据，其中包含二维码路径
     */
    @Override
    @PostMapping
    @ApiOperation(value = "统一收单线下交易", notes = "统一收单线下交易")
    @ApiImplicitParam(name = "nativePayDTO", value = "扫码支付提交参数", required = true)
    public NativePayResDTO createDownLineTrading(@RequestBody NativePayReqDTO nativePayDTO) {
        Trading tradingEntity = BeanUtil.toBean(nativePayDTO, Trading.class);
        Trading trading = this.nativePayService.createDownLineTrading(nativePayDTO.isChangeChannel(),tradingEntity);
        return BeanUtil.toBean(trading, NativePayResDTO.class);
    }

}
