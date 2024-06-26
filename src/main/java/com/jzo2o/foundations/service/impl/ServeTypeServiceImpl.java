package com.jzo2o.foundations.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.foundations.dto.response.ServeTypeSimpleResDTO;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.ServeTypeMapper;
import com.jzo2o.foundations.model.domain.ServeType;
import com.jzo2o.foundations.model.dto.request.ServeSyncUpdateReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeTypePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeTypeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeTypeResDTO;
import com.jzo2o.foundations.service.IServeItemService;
import com.jzo2o.foundations.service.IServeSyncService;
import com.jzo2o.foundations.service.IServeTypeService;
import com.jzo2o.mysql.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务类型
 *
 * @author itcast
 * @create 2023/7/26 14:18
 **/
@Service
public class ServeTypeServiceImpl extends ServiceImpl<ServeTypeMapper, ServeType> implements IServeTypeService {
    @Resource
    private IServeItemService serveItemService;
    @Resource
    private IServeSyncService serveSyncService;

    /**
     * 服务类型新增
     *
     * @param serveTypeUpsertReqDTO 插入更新服务类型
     */
    @Override
    public void add(ServeTypeUpsertReqDTO serveTypeUpsertReqDTO) {
        //校验名称是否重复
        LambdaQueryWrapper<ServeType> queryWrapper = Wrappers.<ServeType>lambdaQuery().eq(ServeType::getName, serveTypeUpsertReqDTO.getName());
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count>0){
            throw new ForbiddenOperationException("服务类型名称不可重复");
        }

        //新增服务类型
        ServeType serveType = BeanUtil.toBean(serveTypeUpsertReqDTO, ServeType.class);
        serveType.setCode(IdUtil.getSnowflakeNextIdStr());
        baseMapper.insert(serveType);
    }

    /**
     * 服务类型修改
     *
     * @param id                    服务类型id
     * @param serveTypeUpsertReqDTO 插入更新服务类型
     */
    @Override
    public void update(Long id, ServeTypeUpsertReqDTO serveTypeUpsertReqDTO) {
        //1.更新服务类型
        ServeType serveType = BeanUtil.toBean(serveTypeUpsertReqDTO, ServeType.class);
        serveType.setId(id);
        baseMapper.updateById(serveType);

        //2.同步数据到es
        ServeSyncUpdateReqDTO serveSyncUpdateReqDTO = new ServeSyncUpdateReqDTO();
        serveSyncUpdateReqDTO.setServeTypeName(serveTypeUpsertReqDTO.getName());
        serveSyncUpdateReqDTO.setServeTypeImg(serveTypeUpsertReqDTO.getImg());
        serveSyncUpdateReqDTO.setServeTypeIcon(serveTypeUpsertReqDTO.getServeTypeIcon());
        serveSyncUpdateReqDTO.setServeTypeSortNum(serveTypeUpsertReqDTO.getSortNum());
        serveSyncService.updateByServeTypeId(id, serveSyncUpdateReqDTO);
    }


    /**
     * 服务类型启用/禁用
     *
     * @param id 服务类型id
     */
    @Override
    @Transactional
    public void activate(Long id) {
        //查询服务类型
        ServeType serveType = baseMapper.selectById(id);
        if (ObjectUtil.isNull(serveType)) {
            throw new ForbiddenOperationException("服务类型不存在");
        }
        //启用状态
        Integer activeStatus = serveType.getActiveStatus();
        //草稿或禁用状态方可启用
        if (!(FoundationStatusEnum.INIT.getStatus() == activeStatus || FoundationStatusEnum.DISABLE.getStatus() == activeStatus)) {
            throw new ForbiddenOperationException("草稿或禁用状态方可启用");
        }
        //更新状态为启用
        LambdaUpdateWrapper<ServeType> updateWrapper = Wrappers.<ServeType>lambdaUpdate()
                .eq(ServeType::getId, id)
                .set(ServeType::getActiveStatus, FoundationStatusEnum.ENABLE.getStatus());
        update(updateWrapper);
    }

    /**
     * 服务类型启用/禁用
     *
     * @param id 服务类型id
     */
    @Override
    @Transactional
    public void deactivate(Long id) {
        //查询服务类型
        ServeType serveType = baseMapper.selectById(id);
        if (ObjectUtil.isNull(serveType)) {
            throw new ForbiddenOperationException("服务类型不存在");
        }
        //启用状态
        Integer activeStatus = serveType.getActiveStatus();
        //启用状态方可禁用
        if (!(FoundationStatusEnum.ENABLE.getStatus() == activeStatus)) {
            throw new ForbiddenOperationException("启用状态方可禁用");
        }
        //下属服务项全部为非启用方可禁用
        int count = serveItemService.queryActiveServeItemCountByServeTypeId(id);
        if (count > 0) {
            throw new ForbiddenOperationException("禁用失败，该服务类型下有启用状态的服务项");
        }
        //更新状态为禁用
        LambdaUpdateWrapper<ServeType> updateWrapper = Wrappers.<ServeType>lambdaUpdate()
                .eq(ServeType::getId, id)
                .set(ServeType::getActiveStatus, FoundationStatusEnum.DISABLE.getStatus());
        update(updateWrapper);
    }

    /**
     * 根据id删除服务类型
     *
     * @param id 服务类型id
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        //查询服务类型
        ServeType serveType = baseMapper.selectById(id);
        if (ObjectUtil.isNull(serveType)) {
            throw new ForbiddenOperationException("服务类型不存在");
        }
        //启用状态
        Integer activeStatus = serveType.getActiveStatus();
        //草稿状态方可删除
        if (!(FoundationStatusEnum.INIT.getStatus() == activeStatus)) {
            throw new ForbiddenOperationException("草稿状态方可删除");
        }
        baseMapper.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param serveTypePageQueryReqDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<ServeTypeResDTO> page(ServeTypePageQueryReqDTO serveTypePageQueryReqDTO) {
        Page<ServeType> page = PageUtils.parsePageQuery(serveTypePageQueryReqDTO, ServeType.class);
        Page<ServeType> serveTypePage = baseMapper.selectPage(page, new QueryWrapper<>());
        return PageUtils.toPage(serveTypePage, ServeTypeResDTO.class);
    }

    /**
     * 根据活动状态查询简略列表
     *
     * @param activeStatus 活动状态，0：草稿，1：禁用，:2：启用
     * @return 服务类型列表
     */
    @Override
    public List<ServeTypeSimpleResDTO> queryServeTypeListByActiveStatus(Integer activeStatus) {
        LambdaQueryWrapper<ServeType> queryWrapper = Wrappers.<ServeType>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(activeStatus), ServeType::getActiveStatus, activeStatus)
                .orderByAsc(ServeType::getSortNum)
                .orderByDesc(ServeType::getUpdateTime);
        List<ServeType> serveTypeList = baseMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(serveTypeList, ServeTypeSimpleResDTO.class);
    }
}
