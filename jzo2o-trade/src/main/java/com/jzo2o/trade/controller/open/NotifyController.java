package com.jzo2o.trade.controller.open;

import cn.hutool.core.map.MapUtil;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.trade.service.NotifyService;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import io.swagger.annotations.Api;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 支付结果的通知
 */
@RestController("openNotifyController")
@Api(tags = "白名单接口 - 支付通知")
@RequestMapping("/open/notify")
public class NotifyController {

    @Resource
    private NotifyService notifyService;

    /**
     * 微信支付成功回调（成功后无需响应内容）
     *
     * @param httpEntity   微信请求信息
     * @param enterpriseId 商户id
     * @return 正常响应200，否则响应500
     */
    @PostMapping("wx/{enterpriseId}")
    public ResponseEntity<Object> wxPayNotify(HttpEntity<String> httpEntity, @PathVariable("enterpriseId") Long enterpriseId) {
        try {
            //获取请求头
            HttpHeaders headers = httpEntity.getHeaders();

            //构建微信请求数据对象
            NotificationRequest request = new NotificationRequest.Builder()
                    .withSerialNumber(headers.getFirst("Wechatpay-Serial")) //证书序列号（微信平台）
                    .withNonce(headers.getFirst("Wechatpay-Nonce"))  //随机串
                    .withTimestamp(headers.getFirst("Wechatpay-Timestamp")) //时间戳
                    .withSignature(headers.getFirst("Wechatpay-Signature")) //签名字符串
                    .withBody(httpEntity.getBody())
                    .build();

            //微信通知的业务处理
            this.notifyService.wxPayNotify(request, enterpriseId);

        } catch (CommonException e) {
            Map<String, Object> result = MapUtil.<String, Object>builder()
                    .put("code", "FAIL")
                    .put("message", e.getMessage())
                    .build();
            //响应500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        return ResponseEntity.ok(null);
    }

    /**
     * 支付宝支付成功回调（成功后需要响应success）
     *
     * @param enterpriseId 商户id
     * @return 正常响应200，否则响应500
     */
    @PostMapping("alipay/{enterpriseId}")
    public ResponseEntity<String> aliPayNotify(HttpServletRequest request,
                                               @PathVariable("enterpriseId") Long enterpriseId) {
        try {
            //支付宝通知的业务处理
            this.notifyService.aliPayNotify(request, enterpriseId);
        } catch (CommonException e) {
            //响应500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok("success");
    }
}
