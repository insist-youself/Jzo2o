package com.jzo2o.trade.controller.inner;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.jzo2o.api.trade.TradingApi;
import com.jzo2o.api.trade.dto.response.TradingResDTO;
import com.jzo2o.api.trade.enums.TradingStateEnum;
import com.jzo2o.trade.model.domain.Trading;
import com.jzo2o.trade.model.dto.TradingDTO;
import com.jzo2o.trade.service.BasicPayService;
import com.jzo2o.trade.service.TradingService;
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
 * @author zzj
 * @version 1.0
 */
@RestController("innerTradingController")
@Api(tags = "内部接口 - 交易单服务")
@RequestMapping("/inner/tradings")
public class TradingController implements TradingApi {

    @Resource
    private TradingService tradingService;
    @Resource
    private BasicPayService basicPayService;

//    /**
//     * 根据订单号查询已结算交易单
//     *
//     * @param productOrderNo 订单号
//     * @return 交易单数据
//     */
//    @Override
//    @GetMapping("/findYjsTradByProductOrderNo")
//    @ApiOperation(value = "根据订单号查询已结算交易单", notes = "根据业务订单号查询交易单")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "productOrderNo", value = "业务订单号", required = true, dataTypeClass = Long.class)
//    })
//    public TradingResDTO findYjsTradByProductOrderNo(@RequestParam("productOrderNo") Long productOrderNo) {
//        List<Trading> yjsTradByProductOrderNo = tradingService.findYjsTradByProductOrderNo(productOrderNo);
//        if(ObjectUtil.isNotEmpty(yjsTradByProductOrderNo)){
//            Trading trading = yjsTradByProductOrderNo.get(0);
//            return BeanUtil.toBean(trading, TradingResDTO.class);
//        }
//        return null;
//    }

//    @Override
//    @GetMapping("/findTradByTradingOrderNo")
//    @ApiOperation(value = "根据交易单号查询交易单", notes = "根据交易单号查询交易单")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "tradingOrderNo", value = "交易单号", required = true, dataTypeClass = Long.class)
//    })
//    public TradingResDTO findTradByTradingOrderNo(Long tradingOrderNo) {
//        Trading tradByTradingOrderNo = tradingService.findTradByTradingOrderNo(tradingOrderNo);
//        TradingResDTO tradingResDTO = BeanUtil.toBean(tradByTradingOrderNo, TradingResDTO.class);
//        return tradingResDTO;
//    }

    @Override
    @GetMapping("/findTradResultByTradingOrderNo")
    @ApiOperation(value = "根据交易单号查询交易单的交易结果", notes = "根据交易单号查询交易单的交易结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tradingOrderNo", value = "交易单号", required = true, dataTypeClass = Long.class)
    })
    public TradingResDTO findTradResultByTradingOrderNo(Long tradingOrderNo) {
        TradingDTO tradingDTO = basicPayService.queryTradingResult(tradingOrderNo);
        TradingResDTO tradingResDTO = BeanUtil.toBean(tradingDTO, TradingResDTO.class);
        return tradingResDTO;
    }

}
