package com.jzo2o.foundations.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.foundations.dto.response.ServeItemResDTO;
import com.jzo2o.api.foundations.dto.response.ServeItemSimpleResDTO;
import com.jzo2o.api.foundations.dto.response.ServeTypeCategoryResDTO;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.ServeItemMapper;
import com.jzo2o.foundations.mapper.ServeTypeMapper;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.domain.ServeType;
import com.jzo2o.foundations.model.dto.request.ServeItemPageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeItemUpsertReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeSyncUpdateReqDTO;
import com.jzo2o.foundations.service.IServeItemService;
import com.jzo2o.foundations.service.IServeSyncService;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
@Service
public class ServeItemServiceImpl extends ServiceImpl<ServeItemMapper, ServeItem> implements IServeItemService {
    @Resource
    private IServeSyncService serveSyncService;

    @Resource
    private ServeTypeMapper serveTypeMapper;

    /**
     * 服务项新增
     *
     * @param serveItemUpsertReqDTO 新增服务项
     */
    @Override
    public void add(ServeItemUpsertReqDTO serveItemUpsertReqDTO) {
        //校验名称是否重复
        LambdaQueryWrapper<ServeItem> queryWrapper = Wrappers.<ServeItem>lambdaQuery().eq(ServeItem::getName, serveItemUpsertReqDTO.getName());
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ForbiddenOperationException("服务项名称不可重复");
        }

        ServeItem serveItem = BeanUtil.toBean(serveItemUpsertReqDTO, ServeItem.class);
        serveItem.setCode(IdUtil.getSnowflakeNextIdStr());
        baseMapper.insert(serveItem);
    }

    /**
     * 服务项修改
     *
     * @param id                    服务项id
     * @param serveItemUpsertReqDTO 插入更新服务项
     * @return 服务项
     */
    @Override
    @CachePut(value = RedisConstants.CacheName.SERVE_ITEM, key = "#id", unless = "#result.activeStatus != 2", cacheManager = RedisConstants.CacheManager.ONE_DAY)
    public ServeItem update(Long id, ServeItemUpsertReqDTO serveItemUpsertReqDTO) {
        //1.更新服务项
        ServeItem serveItem = BeanUtil.toBean(serveItemUpsertReqDTO, ServeItem.class);
        serveItem.setId(id);
        baseMapper.updateById(serveItem);

        //2.同步数据到es
        ServeSyncUpdateReqDTO serveSyncUpdateReqDTO = BeanUtil.toBean(serveItemUpsertReqDTO, ServeSyncUpdateReqDTO.class);
        serveSyncUpdateReqDTO.setServeItemName(serveItemUpsertReqDTO.getName());
        serveSyncUpdateReqDTO.setServeItemImg(serveItemUpsertReqDTO.getImg());
        serveSyncUpdateReqDTO.setServeItemIcon(serveItemUpsertReqDTO.getServeItemIcon());
        serveSyncUpdateReqDTO.setServeItemSortNum(serveItemUpsertReqDTO.getSortNum());
        serveSyncService.updateByServeItemId(id, serveSyncUpdateReqDTO);

        //用于更新缓存
        return baseMapper.selectById(id);
    }


    /**
     * 启用服务项
     *
     * @param id 服务项id
     * @return
     */
    @Override
    @Transactional
    @CachePut(value = RedisConstants.CacheName.SERVE_ITEM, key = "#id", cacheManager = RedisConstants.CacheManager.ONE_DAY)
    public ServeItem activate(Long id) {

        //查询服务项
        ServeItem serveItem = baseMapper.selectById(id);
        if (ObjectUtil.isNull(serveItem)) {
            throw new ForbiddenOperationException("服务项不存在");
        }
        //启用状态
        Integer activeStatus = serveItem.getActiveStatus();
        //草稿或禁用状态方可启用
        if (!(FoundationStatusEnum.INIT.getStatus() == activeStatus || FoundationStatusEnum.DISABLE.getStatus() == activeStatus)) {
            throw new ForbiddenOperationException("草稿或禁用状态方可启用");
        }
        //服务类型id
        Long serveTypeId = serveItem.getServeTypeId();
        //服务类型信息
        ServeType serveType = serveTypeMapper.selectById(serveTypeId);
        if (ObjectUtil.isNull(serveType)) {
            throw new ForbiddenOperationException("所属服务类型不存在");
        }
        //所属服务类型为启用状态时方可启用
        if (!(FoundationStatusEnum.ENABLE.getStatus() == serveType.getActiveStatus())) {
            throw new ForbiddenOperationException("所属服务类型为启用状态时方可启用");
        }

        if (ObjectUtil.equal(FoundationStatusEnum.DISABLE.getStatus(), serveType.getActiveStatus())) {
            throw new ForbiddenOperationException("服务所属的服务类型已禁用，启用后方可操作。");
        }
        //更新启用状态
        LambdaUpdateWrapper<ServeItem> updateWrapper = Wrappers.<ServeItem>lambdaUpdate().eq(ServeItem::getId, id).set(ServeItem::getActiveStatus, FoundationStatusEnum.ENABLE.getStatus());
        update(updateWrapper);

        return baseMapper.selectById(id);
    }

    /**
     * 禁用服务项
     *
     * @param id 服务项id
     * @return
     */
    @Override
    @Transactional
    @CacheEvict(value = RedisConstants.CacheName.SERVE_ITEM, key = "#id", beforeInvocation = true)
    public void deactivate(Long id) {
        //查询服务项
        ServeItem serveItem = baseMapper.selectById(id);
        if (ObjectUtil.isNull(serveItem)) {
            throw new ForbiddenOperationException("服务项不存在");
        }
        //启用状态
        Integer activeStatus = serveItem.getActiveStatus();
        //启用状态方可禁用
        if (!(FoundationStatusEnum.ENABLE.getStatus() == activeStatus)) {
            throw new ForbiddenOperationException("启用状态方可禁用");
        }

        //有区域在使用该服务将无法禁用（存在关联的区域服务且状态为上架表示有区域在使用该服务项）
        //todo

        //更新禁用状态
        LambdaUpdateWrapper<ServeItem> updateWrapper = Wrappers.<ServeItem>lambdaUpdate().eq(ServeItem::getId, id).set(ServeItem::getActiveStatus, FoundationStatusEnum.DISABLE.getStatus());
        update(updateWrapper);
    }


    /**
     * 服务项删除
     *
     * @param id 服务项id
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        ServeItem serveItem = baseMapper.selectById(id);
        if (ObjectUtil.isNull(serveItem)) {
            throw new ForbiddenOperationException("服务项不存在");
        }
        //启用状态
        Integer activeStatus = serveItem.getActiveStatus();

        //1.删除校验：只有草稿状态方可删除
        if (!(FoundationStatusEnum.INIT.getStatus() == activeStatus)) {
            throw new ForbiddenOperationException("只有草稿状态方可删除");
        }

        //2.根据id删除
        baseMapper.deleteById(id);
    }

    /**
     * 根据服务类型id查询关联的启用状态服务项数量
     *
     * @param serveTypeId 服务类型id
     * @return 服务项数量
     */
    @Override
    public int queryActiveServeItemCountByServeTypeId(Long serveTypeId) {
        LambdaQueryWrapper<ServeItem> queryWrapper = Wrappers.<ServeItem>lambdaQuery()
                .eq(ServeItem::getServeTypeId, serveTypeId)
                .eq(ServeItem::getActiveStatus, FoundationStatusEnum.ENABLE.getStatus());
        return baseMapper.selectCount(queryWrapper);
    }

    /**
     * 分页查询
     *
     * @param serveItemPageQueryReqDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<ServeItemResDTO> page(ServeItemPageQueryReqDTO serveItemPageQueryReqDTO) {
        return PageHelperUtils.selectPage(serveItemPageQueryReqDTO,
                () -> baseMapper.queryList(serveItemPageQueryReqDTO.getServeTypeId(), serveItemPageQueryReqDTO.getName(), serveItemPageQueryReqDTO.getActiveStatus()));
    }

    /**
     * 根据id查询
     *
     * @param id 服务项id
     * @return 服务项详细信息
     */
    @Override
    public ServeItemResDTO queryServeItemAndTypeById(Long id) {
        return baseMapper.queryServeItemAndTypeById(id);
    }

    /**
     * 根据id列表批量查询
     *
     * @param ids 服务项id列表
     * @return 服务项简略列表
     */
    @Override
    public List<ServeItemSimpleResDTO> queryServeItemListByIds(List<Long> ids) {
        List<ServeItem> list = lambdaQuery().in(ServeItem::getId, ids).orderByAsc(ServeItem::getCreateTime).list();
        return BeanUtil.copyToList(list, ServeItemSimpleResDTO.class);
    }


    /**
     * 查询启用状态的服务项目录
     *
     * @return 服务项目录
     */
    @Override
    public List<ServeTypeCategoryResDTO> queryActiveServeItemCategory() {
        return baseMapper.queryActiveServeItemCategory();
    }
}
