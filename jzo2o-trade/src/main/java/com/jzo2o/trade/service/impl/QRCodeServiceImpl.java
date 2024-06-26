package com.jzo2o.trade.service.impl;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jzo2o.trade.config.QRCodeConfig;
import com.jzo2o.api.trade.enums.PayChannelEnum;
import com.jzo2o.trade.service.QRCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author itcast
 */
@Service
public class QRCodeServiceImpl implements QRCodeService {

    @Resource
    private QRCodeConfig qrCodeConfig;

    @Override
    public String generate(String content, PayChannelEnum payChannel) {
        QrConfig qrConfig = new QrConfig();
        //设置边距
        qrConfig.setMargin(this.qrCodeConfig.getMargin());
        //二维码颜色
        qrConfig.setForeColor(HexUtil.decodeColor(this.qrCodeConfig.getForeColor()));
        //设置背景色
        qrConfig.setBackColor(HexUtil.decodeColor(this.qrCodeConfig.getBackColor()));
        //纠错级别
        qrConfig.setErrorCorrection(ErrorCorrectionLevel.valueOf(this.qrCodeConfig.getErrorCorrectionLevel()));
        //设置宽
        qrConfig.setWidth(this.qrCodeConfig.getWidth());
        //设置高
        qrConfig.setHeight(this.qrCodeConfig.getHeight());
        if (ObjectUtil.isNotEmpty(payChannel)) {
            //设置logo
            qrConfig.setImg(this.qrCodeConfig.getLogo(payChannel));
        }
        return QrCodeUtil.generateAsBase64(content, qrConfig, ImgUtil.IMAGE_TYPE_PNG);
    }

    @Override
    public String generate(String content) {
        return generate(content, null);
    }
}
