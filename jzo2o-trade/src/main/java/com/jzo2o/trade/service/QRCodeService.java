package com.jzo2o.trade.service;

import com.jzo2o.api.trade.enums.PayChannelEnum;

public interface QRCodeService {

    /**
     * 生成二维码
     *
     * @param content 二维码中的内容
     * @return 图片base64数据
     */
    String generate(String content);

    /**
     * 生成二维码，带logo
     *
     * @param content    二维码中的内容
     * @param payChannel 付款渠道
     * @return 图片base64数据
     */
    String generate(String content, PayChannelEnum payChannel);
}
