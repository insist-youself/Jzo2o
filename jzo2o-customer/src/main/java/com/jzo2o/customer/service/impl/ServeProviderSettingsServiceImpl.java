package com.jzo2o.customer.service.impl;

import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.enums.EnableStatusEnum;
import com.jzo2o.common.expcetions.DBException;
import com.jzo2o.common.utils.*;
import com.jzo2o.customer.model.domain.*;
import com.jzo2o.customer.mapper.ServeProviderSettingsMapper;
import com.jzo2o.customer.model.dto.request.ServePickUpReqDTO;
import com.jzo2o.customer.model.dto.request.ServeScopeSetReqDTO;
import com.jzo2o.customer.model.dto.response.CertificationStatusDTO;
import com.jzo2o.customer.model.dto.response.ServeProviderSettingsGetResDTO;
import com.jzo2o.customer.model.dto.response.ServeSettingsStatusResDTO;
import com.jzo2o.customer.service.*;
import com.jzo2o.mvc.utils.UserContext;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务人员/机构附属信息 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-07-20
 */
@Service
public class ServeProviderSettingsServiceImpl extends ServiceImpl<ServeProviderSettingsMapper, ServeProviderSettings> implements IServeProviderSettingsService {

    @Resource
    private IServeProviderService serveProviderService;

    @Resource
    private IServeProviderSyncService serveProviderSyncService;



    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void add(Long id, Integer serveProviderType) {
        ServeProviderSettings serveProviderSettings = new ServeProviderSettings();
        serveProviderSettings.setId(id);
        if(baseMapper.insert(serveProviderSettings) <= 0 || serveProviderSyncService.add(id, serveProviderType) <= 0){
            throw new DBException("请求失败");
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void setServeScope(ServeScopeSetReqDTO serveScopeSetReqDTO) {
        String[] latAndLon = serveScopeSetReqDTO.getLocation().split(",");
        // 经度
        double lon = NumberUtils.parseDouble(latAndLon[0]);
        // 纬度
        double lat = NumberUtils.parseDouble(latAndLon[1]);
        // 1.更新服务范围
        ServeProviderSettings serveProviderSettings = BeanUtils.copyBean(serveScopeSetReqDTO, ServeProviderSettings.class);
        serveProviderSettings.setId(UserContext.currentUserId());
        serveProviderSettings.setLat(lat);
        serveProviderSettings.setLon(lon);
        baseMapper.updateById(serveProviderSettings);

//        // 2.初次设置完成配置校验
//        serveProviderService.settingStatus(UserContext.currentUserId());

        // 3.同步服务信息
        // 经纬度
        ServeProviderSync serveProviderSync = ServeProviderSync.builder()
                .id(UserContext.currentUserId())
                .cityCode(serveScopeSetReqDTO.getCityCode())
                .lon(lon)
                .lat(lat)
                .build();
        serveProviderSyncService.updateById(serveProviderSync);
    }

    @Override
    public ServeProviderSettingsGetResDTO getServeScope() {
        Long currentUserId = UserContext.currentUserId();
        ServeProviderSettings serveProviderSettings = baseMapper.selectById(currentUserId);
        if(serveProviderSettings == null) {
            return new ServeProviderSettingsGetResDTO();
        }
        ServeProviderSettingsGetResDTO serveScopeResDTO = BeanUtils.toBean(serveProviderSettings, ServeProviderSettingsGetResDTO.class);
        if(ObjectUtils.isNotNull(serveProviderSettings.getLon())) {
            serveScopeResDTO.setLocation(serveProviderSettings.getLon() + "," + serveProviderSettings.getLat());
        }

        return serveScopeResDTO;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void setPickUp(ServePickUpReqDTO servePickUpReqDTO) {
        // 1.更新接单信息
        ServeProviderSettings serveProviderSettings = new ServeProviderSettings();
        serveProviderSettings.setId(UserContext.currentUserId());
        serveProviderSettings.setCanPickUp(servePickUpReqDTO.getCanPickUp());
        baseMapper.updateById(serveProviderSettings);
//        // 2.初次设置完成配置校验
//        serveProviderService.settingStatus(UserContext.currentUserId());
        // 3.同步es
        ServeProviderSync serveProviderSync = ServeProviderSync.builder().id(UserContext.currentUserId())
                .pickUp(servePickUpReqDTO.getCanPickUp())
                .build();
        serveProviderSyncService.updateById(serveProviderSync);

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void setPickUp(Long id, Integer canPickUp) {
        // 1.更新接单信息
        ServeProviderSettings serveProviderSettings = new ServeProviderSettings();
        serveProviderSettings.setId(id);
        serveProviderSettings.setCanPickUp(canPickUp);
        baseMapper.updateById(serveProviderSettings);
//        // 2.初次设置完成配置校验
//        serveProviderService.settingStatus(UserContext.currentUserId());
        // 3.同步es
        ServeProviderSync serveProviderSync = ServeProviderSync.builder().id(id)
                .pickUp(canPickUp)
                .build();
        serveProviderSyncService.updateById(serveProviderSync);
    }

    //返回的设置状态在前端需要使用
    @Override
    public ServeSettingsStatusResDTO getSettingStatus() {


        //当前用户id
        long currentUserId = UserContext.currentUserId();

        //服务人员或机构的配置信息
        ServeProviderSettings serveProviderSettings = this.baseMapper.selectById(currentUserId);
        //服务人员或机构信息
        ServeProvider serveProvider = serveProviderService.findById(currentUserId);
        //是否开启接单
        boolean canPickUp = EnableStatusEnum.ENABLE.equals(serveProviderSettings.getCanPickUp());
        //是否设置服务范围
        boolean serveScopeSetted = ObjectUtils.isNotEmpty(serveProviderSettings.getLon()) &&
                    ObjectUtils.isNotEmpty(serveProviderSettings.getLat()) &&
                    ObjectUtils.isNotEmpty(serveProviderSettings.getIntentionScope()) ;
        //是否设置技能
        boolean serveSkillSetted = serveProviderSettings.getHaveSkill() > 0;

        //获取认证状态
        CertificationStatusDTO certificationStatusDTO = serveProviderService.getCertificationStatus(serveProvider.getType(), currentUserId);
        //获取认证状态
        Integer certificationStatus = ObjectUtils.get(certificationStatusDTO,CertificationStatusDTO::getCertificationStatus);
        //如果certificationStatus为空则初始状态为0
        if(ObjectUtils.isNull(certificationStatus)){
            certificationStatus = 0;
        }
        int settingsStatus = 0;
        //认证通过，设置服务技能，设置服务范围 更新 首次设置状态为完成
        if(serveSkillSetted && serveScopeSetted && certificationStatus==2){
            settingsStatus = 1;//完成首次设置状态
            ServeProvider updateServeProvider = new ServeProvider();
            updateServeProvider.setSettingsStatus(settingsStatus);
            updateServeProvider.setId(currentUserId);
            serveProviderService.updateById(updateServeProvider);
            //插入同步表
            ServeProviderSync serveProviderSync =
                    ServeProviderSync.builder()
                            .id(currentUserId)
                            .settingStatus(settingsStatus)
                            .build();
            serveProviderSyncService.updateById(serveProviderSync);
        }


        ServeSettingsStatusResDTO serveSettingsStatusResDTO =
                ServeSettingsStatusResDTO.builder()
                        .certificationStatus(certificationStatus)//认证状态
                        .settingsStatus(settingsStatus)//首先设置状态是否完成
                        .serveSkillSetted(serveSkillSetted)//是否设置服务技能
                        .serveScopeSetted(serveScopeSetted)//是否设置服务范围
                        .canPickUp(canPickUp)//开启接单状态
                        .build();

        return serveSettingsStatusResDTO;
    }

    @Override
    public ServeProviderSettings findById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void setHaveSkill(Long currentUserId) {
        lambdaUpdate().set(ServeProviderSettings::getHaveSkill, true)
                .update();
    }

    @Override
    public Map<Long, String> findManyCityCodeOfServeProvider(List<Long> ids) {
        if(CollUtils.isEmpty(ids)) {
            return new HashMap<>();
        }
        List<ServeProviderSettings> serveProviderSettings = baseMapper.batchQueryCityCodeByIds(ids);
        return CollUtils.isEmpty(serveProviderSettings) ? new HashMap<>() :
                serveProviderSettings.stream().collect(Collectors.toMap(ServeProviderSettings::getId,ServeProviderSettings::getCityCode));

    }


}
