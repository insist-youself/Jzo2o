package com.jzo2o.trade.controller.inner;

import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.api.trade.RefundRecordApi;
import com.jzo2o.api.trade.dto.response.ExecutionResultResDTO;
import com.jzo2o.trade.model.domain.RefundRecord;
import com.jzo2o.trade.service.BasicPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 基础支付控制器
 *
 * @author itcast
 */
@RequestMapping("/inner/refund-record")
@RestController("innerRefundRecordController")
@Api(tags = "内部接口 - 退款")
public class RefundRecordController implements RefundRecordApi {

    @Resource
    private BasicPayService basicPayService;

    /***
     * 统一收单交易退款接口
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
     * 将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * @param tradingOrderNo 交易单号
     * @param refundAmount 退款金额
     * @return
     */
    @Override
    @PostMapping("refund")
    @ApiOperation(value = "统一收单交易退款", notes = "统一收单交易退款")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tradingOrderNo", value = "交易单号", required = true),
            @ApiImplicitParam(name = "refundAmount", value = "退款金额", required = true)
    })
    public ExecutionResultResDTO refundTrading(@RequestParam("tradingOrderNo") Long tradingOrderNo,
                                               @RequestParam("refundAmount") BigDecimal refundAmount) {
        RefundRecord refundRecord = this.basicPayService.refundTrading(tradingOrderNo, refundAmount);
        return BeanUtil.toBean(refundRecord,ExecutionResultResDTO.class);
    }
}
