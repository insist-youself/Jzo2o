package com.jzo2o.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.trade.constant.Constants;
import com.jzo2o.trade.mapper.PayChannelMapper;
import com.jzo2o.trade.model.domain.PayChannel;
import com.jzo2o.trade.model.dto.PayChannelDTO;
import com.jzo2o.trade.service.PayChannelService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author itcast
 * @Description： 服务实现类
 */
@Service
public class PayChannelServiceImpl extends ServiceImpl<PayChannelMapper, PayChannel> implements PayChannelService {

    @Override
    public Page<PayChannel> findPayChannelPage(PayChannelDTO payChannelDTO, int pageNum, int pageSize) {
        Page<PayChannel> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PayChannel> queryWrapper = new LambdaQueryWrapper<>();

        //设置条件
        queryWrapper.eq(StrUtil.isNotEmpty(payChannelDTO.getChannelLabel()), PayChannel::getChannelLabel, payChannelDTO.getChannelLabel());
        queryWrapper.likeRight(StrUtil.isNotEmpty(payChannelDTO.getChannelName()), PayChannel::getChannelName, payChannelDTO.getChannelName());
        queryWrapper.eq(StrUtil.isNotEmpty(payChannelDTO.getEnableFlag()), PayChannel::getEnableFlag, payChannelDTO.getEnableFlag());
        //设置排序
        queryWrapper.orderByAsc(PayChannel::getCreateTime);

        return super.page(page, queryWrapper);
    }

    @Override
    public PayChannel findByEnterpriseId(Long enterpriseId, String channelLabel) {
        LambdaQueryWrapper<PayChannel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayChannel::getEnterpriseId, enterpriseId)
                .eq(PayChannel::getChannelLabel, channelLabel)
                .eq(PayChannel::getEnableFlag, Constants.YES);
        return super.getOne(queryWrapper);
    }

    @Override
    public PayChannel createPayChannel(PayChannelDTO payChannelDTO) {
        PayChannel payChannel = BeanUtil.toBean(payChannelDTO, PayChannel.class);
        boolean flag = super.save(payChannel);
        if (flag) {
            return payChannel;
        }
        return null;
    }

    @Override
    public Boolean updatePayChannel(PayChannelDTO payChannelDTO) {
        PayChannel payChannel = BeanUtil.toBean(payChannelDTO, PayChannel.class);
        return super.updateById(payChannel);
    }

    @Override
    public Boolean deletePayChannel(String[] checkedIds) {
        List<String> ids = Arrays.asList(checkedIds);
        return super.removeByIds(ids);
    }

    @Override
    public List<PayChannel> findPayChannelList(String channelLabel) {
        LambdaQueryWrapper<PayChannel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayChannel::getChannelLabel, channelLabel)
                .eq(PayChannel::getEnableFlag, Constants.YES);
        return list(queryWrapper);
    }
}
