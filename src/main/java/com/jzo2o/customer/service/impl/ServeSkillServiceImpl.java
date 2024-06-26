package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.foundations.ServeItemApi;
import com.jzo2o.api.foundations.ServeTypeApi;
import com.jzo2o.api.foundations.dto.response.ServeItemSimpleResDTO;
import com.jzo2o.api.foundations.dto.response.ServeTypeCategoryResDTO;
import com.jzo2o.api.foundations.dto.response.ServeTypeSimpleResDTO;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.customer.mapper.ServeSkillMapper;
import com.jzo2o.customer.model.domain.ServeProviderSync;
import com.jzo2o.customer.model.domain.ServeSkill;
import com.jzo2o.customer.model.dto.request.ServeSkillAddReqDTO;
import com.jzo2o.customer.model.dto.response.ServeSkillCategoryResDTO;
import com.jzo2o.customer.model.dto.response.ServeSkillItemResDTO;
import com.jzo2o.customer.service.IServeProviderService;
import com.jzo2o.customer.service.IServeProviderSettingsService;
import com.jzo2o.customer.service.IServeProviderSyncService;
import com.jzo2o.customer.service.IServeSkillService;
import com.jzo2o.mvc.utils.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务技能表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-07-18
 */
@Service
public class ServeSkillServiceImpl extends ServiceImpl<ServeSkillMapper, ServeSkill> implements IServeSkillService {
    @Resource
    private ServeTypeApi serveTypeApi;
    @Resource
    private ServeItemApi serveItemApi;
    @Resource
    private IServeProviderService serveProviderService;
    @Resource
    private IServeProviderSettingsService serveProviderSettingsService;

    @Resource
    private IServeProviderSyncService serveProviderSyncService;

    /**
     * 批量新增或修改
     *
     * @param serveSkillAddReqDTOList 批量新增或修改数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpsert(List<ServeSkillAddReqDTO> serveSkillAddReqDTOList) {
        CurrentUserInfo currentUserInfo = UserContext.currentUser();

        //1.删除上一次该用户设置的服务技能
        LambdaQueryWrapper<ServeSkill> queryWrapper = Wrappers.<ServeSkill>lambdaQuery()
                .eq(ServeSkill::getServeProviderType, currentUserInfo.getUserType())
                .eq(ServeSkill::getServeProviderId, currentUserInfo.getId());
        baseMapper.delete(queryWrapper);

        //2.添加新的服务技能
        List<ServeSkill> serveSkillList = BeanUtil.copyToList(serveSkillAddReqDTOList, ServeSkill.class);
        serveSkillList.forEach(s -> {
            s.setServeProviderId(currentUserInfo.getId());
            s.setServeProviderType(currentUserInfo.getUserType());
        });
        super.saveBatch(serveSkillList);

        // 3.设置技能
        serveProviderSettingsService.setHaveSkill(UserContext.currentUserId());
//        // 4.校验并设置初次设置完成
//        serveProviderService.settingStatus(UserContext.currentUserId());
        // 5.格式化服务技能，准备插入同步表
        List<Long> serveItemIds = serveSkillAddReqDTOList.stream()
                .map(ServeSkillAddReqDTO::getServeItemId)
                .collect(Collectors.toList());
        //写入服务提供者同步表，将来由同步任务同步到ES
        ServeProviderSync serveProviderSync = ServeProviderSync.builder()
                .id(UserContext.currentUserId())
                .serveItemIds(serveItemIds)
                .build();
        serveProviderSyncService.updateById(serveProviderSync);
    }

    /**
     * 查询服务技能目录
     *
     * @return 服务技能目录
     */
    @Override
    public List<ServeSkillCategoryResDTO> category() {
        //1.查询当前用户的服务技能
        CurrentUserInfo currentUserInfo = UserContext.currentUser();
        LambdaQueryWrapper<ServeSkill> queryWrapper = Wrappers.<ServeSkill>lambdaQuery()
                .eq(ServeSkill::getServeProviderType, currentUserInfo.getUserType())
                .eq(ServeSkill::getServeProviderId, currentUserInfo.getId());
        List<ServeSkill> serveSkillList = baseMapper.selectList(queryWrapper);

        //2.查询启用状态的服务项目录
        List<ServeTypeCategoryResDTO> serveTypeCategory = serveItemApi.queryActiveServeItemCategory();
        if (ObjectUtil.isEmpty(serveTypeCategory)) {
            return Collections.emptyList();
        }

        //3.封装数据，计算服务类型下属服务技能数量、判断技能是否选中
        List<ServeSkillCategoryResDTO> list = BeanUtils.copyToList(serveTypeCategory, ServeSkillCategoryResDTO.class, (origin, target) -> {
            target.setServeSkillItemResDTOList(BeanUtils.copyToList(origin.getServeItemList(), ServeSkillItemResDTO.class));
        });
        Map<Long, Long> skillTypeCount = CollUtils.defaultIfEmpty(serveSkillList.stream().collect(Collectors.groupingBy(ServeSkill::getServeTypeId, Collectors.counting())), new HashMap<>());
        List<Long> serveItemIds = CollUtils.defaultIfEmpty(serveSkillList.stream().map(ServeSkill::getServeItemId).collect(Collectors.toList()), new ArrayList<>());
        list.forEach(type -> {
            Long count = skillTypeCount.get(type.getServeTypeId());
            type.setCount(null == count ? 0 : count.intValue());
            type.getServeSkillItemResDTOList().forEach(item -> item.setIsSelected(serveItemIds.contains(item.getServeItemId())));
        });
        return list;
    }

    /**
     * 查询服务者的服务技能
     *
     * @param providerId   服务者id
     * @param providerType 服务者类型
     * @param cityCode     城市编码
     * @return 服务技能列表
     */
    @Override
    public List<Long> queryServeSkillListByServeProvider(Long providerId, Integer providerType, String cityCode) {
        //1.获取服务者的所有服务技能
        LambdaQueryWrapper<ServeSkill> queryWrapper = Wrappers.<ServeSkill>lambdaQuery()
                .eq(ServeSkill::getServeProviderType, providerType)
                .eq(ServeSkill::getServeProviderId, providerId);
        List<ServeSkill> serveSkillList = baseMapper.selectList(queryWrapper);
        if (ObjectUtil.isEmpty(serveSkillList)) {
            return Collections.emptyList();
        }

        //2.从技能中提取服务项列表
        List<Long> skillServeItemIds = serveSkillList.stream().map(ServeSkill::getServeItemId).collect(Collectors.toList());

        return skillServeItemIds;
    }

    /**
     * 获取服务者的技能分类
     *
     * @return 技能分类列表
     */
    @Override
    public List<ServeTypeSimpleResDTO> queryCurrentUserServeSkillTypeList() {
        //1.查询当前用户的服务技能
        CurrentUserInfo currentUserInfo = UserContext.currentUser();
        LambdaQueryWrapper<ServeSkill> queryWrapper = Wrappers.<ServeSkill>lambdaQuery()
                .eq(ServeSkill::getServeProviderType, currentUserInfo.getUserType())
                .eq(ServeSkill::getServeProviderId, currentUserInfo.getId());
        List<ServeSkill> serveSkillList = baseMapper.selectList(queryWrapper);
        if (ObjectUtil.isEmpty(serveSkillList)) {
            return Collections.emptyList();
        }

        //2.提取出服务类型id，并去重
        List<Long> skillServeTypeId = serveSkillList.stream().map(ServeSkill::getServeTypeId).distinct().collect(Collectors.toList());

        //3.根据id列表查询服务类型
        return serveTypeApi.listByIds(skillServeTypeId);
    }

    /**
     * 获取服务者的所有技能
     *
     * @return 技能列表
     */
    @Override
    public List<ServeItemSimpleResDTO> queryCurrentUserServeSkillItemList() {
        //1.查询当前用户的服务技能
        CurrentUserInfo currentUserInfo = UserContext.currentUser();
        LambdaQueryWrapper<ServeSkill> queryWrapper = Wrappers.<ServeSkill>lambdaQuery()
                .eq(ServeSkill::getServeProviderType, currentUserInfo.getUserType())
                .eq(ServeSkill::getServeProviderId, currentUserInfo.getId())
                .select(ServeSkill::getServeItemId);
        List<ServeSkill> serveSkillList = baseMapper.selectList(queryWrapper);
        if (ObjectUtil.isEmpty(serveSkillList)) {
            return Collections.emptyList();
        }

        //2.提取服务项id列表查询名称信息
        List<Long> serveItemIds = serveSkillList.stream().map(ServeSkill::getServeItemId).collect(Collectors.toList());
        return serveItemApi.listByIds(serveItemIds);
    }
}
