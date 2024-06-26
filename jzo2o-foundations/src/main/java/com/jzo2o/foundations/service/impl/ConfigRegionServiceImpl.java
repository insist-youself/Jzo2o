package com.jzo2o.foundations.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.foundations.dto.response.ConfigRegionInnerResDTO;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.foundations.mapper.ConfigRegionMapper;
import com.jzo2o.foundations.model.domain.ConfigRegion;
import com.jzo2o.foundations.model.dto.request.ConfigRegionSetReqDTO;
import com.jzo2o.foundations.model.dto.response.ConfigRegionResDTO;
import com.jzo2o.foundations.service.IConfigRegionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 区域业务配置 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-08-21
 */
@Service
public class ConfigRegionServiceImpl extends ServiceImpl<ConfigRegionMapper, ConfigRegion> implements IConfigRegionService {

    @Override
    public ConfigRegionResDTO queryById(Long id) {
        ConfigRegion configRegion = baseMapper.selectById(id);
        return BeanUtils.toBean(configRegion, ConfigRegionResDTO.class);
    }

    @Override
    public void setConfigRegionById(Long id, ConfigRegionSetReqDTO configRegionSetReqDTO) {
        ConfigRegion configRegion = BeanUtils.toBean(configRegionSetReqDTO, ConfigRegion.class);
        configRegion.setId(id);
        baseMapper.updateById(configRegion);
    }

    @Override
    public void init(Long id, String cityCode) {
        ConfigRegion configRegion = ConfigRegion.builder()
                .id(id)
                .cityCode(cityCode)
                // 个人接单数量限制，默认10个
                .staffReceiveOrderMax(10)
                // 机构接单数量限制，默认100个
                .institutionReceiveOrderMax(100)
                // 个人接单范围半径 50公里
                .staffServeRadius(50)
                // 机构接单范围半径200公里
                .institutionServeRadius(200)
                // 分流时间间隔120分钟，即下单时间与服务预计开始时间的间隔
                .diversionInterval(120)
                // 抢单超时时间，默认60分钟
                .seizeTimeoutInterval(60)
                // 派单策略默认距离优先策略
                .dispatchStrategy(1)
                // 派单每轮时间间隔，默认180s
                .dispatchPerRoundInterval(180)
                .build();
        baseMapper.insert(configRegion);
    }

    @Override
    public List<ConfigRegionInnerResDTO> queryAll() {
        List<ConfigRegion> list = lambdaQuery().list();
        return BeanUtils.copyToList(list, ConfigRegionInnerResDTO.class);
    }

    @Override
    public ConfigRegionInnerResDTO queryByCityCode(String cityCode) {
        ConfigRegion configRegion = lambdaQuery()
                .eq(ConfigRegion::getCityCode, cityCode)
                .one();
        return BeanUtils.toBean(configRegion, ConfigRegionInnerResDTO.class);
    }
}
