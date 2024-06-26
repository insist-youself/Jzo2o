package com.jzo2o.trade.service;

import com.jzo2o.common.expcetions.CommonException;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付通知
 *
 * @author zzj
 * @version 1.0
 */
public interface NotifyService {


    /**
     * 微信支付通知，官方文档：https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_4_5.shtml
     *
     * @param request      微信请求对象
     * @param enterpriseId 商户id
     * @throws CommonException 抛出异常，通过异常决定是否响应200
     */
    void wxPayNotify(NotificationRequest request, Long enterpriseId) throws CommonException;

    /**
     * 支付宝支付通知，官方文档：https://opendocs.alipay.com/open/194/103296?ref=api
     *
     * @param request      请求对象
     * @param enterpriseId 商户id
     * @throws CommonException 抛出异常，通过异常决定是否响应200
     */
    void aliPayNotify(HttpServletRequest request, Long enterpriseId) throws CommonException;
}
