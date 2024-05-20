package com.jzo2o.foundations.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.mapper.ServeItemMapper;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author linger
 * @since 2024-05-14
 */
@Service
public class ServeServiceImpl extends ServiceImpl<ServeMapper, Serve> implements IServeService {

    @Resource
    private ServeItemMapper serveItemMapper;

    @Resource
    private RegionMapper regionMapper;

    /**
     *  区域服务分页查询
     * @param servePageQueryReqDTO
     * @return
     */
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO){
        return PageHelperUtils.selectPage(servePageQueryReqDTO,
                () -> baseMapper.queryServeListByRegionId(servePageQueryReqDTO.getRegionId()));
    }

    @Override
    @Transactional
    public void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList) {
        for (ServeUpsertReqDTO serveUpsertReqDTO : serveUpsertReqDTOList) {
            ServeItem serveItem = serveItemMapper.selectById(serveUpsertReqDTO.getServeItemId());
            //1.全局参数校验
            // 判断对应的 服务项是否为空， 是否启用
            if (ObjectUtil.isNull(serveItem) || serveItem.getActiveStatus() != FoundationStatusEnum.ENABLE.getStatus()) {
                throw new ForbiddenOperationException("该服务不存在或者该服务为启用无法添加到对应区域");
            }
            // 判断是否重复校验
            Integer count = lambdaQuery()
                    .eq(Serve::getServeItemId, serveUpsertReqDTO.getServeItemId())
                    .eq(Serve::getRegionId, serveUpsertReqDTO.getRegionId()).count();
            if(count > 0) {
                throw new ForbiddenOperationException(serveItem.getName()+"服务已存在");
            }
            // 执行插入操作
            Serve serve = BeanUtils.copyBean(serveUpsertReqDTO, Serve.class);
            Region region = regionMapper.selectById(serveUpsertReqDTO.getRegionId());
            serve.setCityCode(region.getCityCode());
            baseMapper.insert(serve);
        }
    }

    @Override
    @Transactional
    public Serve update(Long id, BigDecimal price) {
        // 更新价格
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getPrice, price)
                .update();
        // 判断是否修改成功
        if(!update) {
            throw  new CommonException("修改服务价格失败");
        }
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional
    public Serve onSale(Long id) {
        // 查询当前服务项
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)) {
            throw new ForbiddenOperationException("区域服务不存在");
        }
        Integer saleStatus = serve.getSaleStatus();
        // 判断当前状态是否为 草稿 或 下架
        if(!(saleStatus == FoundationStatusEnum.INIT.getStatus() || saleStatus == FoundationStatusEnum.DISABLE.getStatus())) {
            throw new ForbiddenOperationException("区域服务状态必须为草稿或下架!");
        }
        ServeItem serveItem = serveItemMapper.selectById(serve.getServeItemId());
        Integer activeStatus = serveItem.getActiveStatus();
        // 判断当前服务是否启用
        if(activeStatus != FoundationStatusEnum.ENABLE.getStatus()) {
            throw new ForbiddenOperationException("当前服务未启用, 无法上架");
        }
        // 修改状态为上架
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getSaleStatus, FoundationStatusEnum.ENABLE.getStatus())
                .update();
        if(!update) {
            throw new CommonException("启动服务失败");
        }
        return baseMapper.selectById(id);
    }

    @Override
    public void deleteById(Long id) {

        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)) {
            throw new ForbiddenOperationException("当前服务项不存在");
        }
        // 判断当前服务项是否为草稿状态
        if(serve.getSaleStatus() != FoundationStatusEnum.INIT.getStatus()) {
            throw new ForbiddenOperationException("服务项必须为草稿状态才能删除！");
        }
        // 进行删除操作
        int delete = baseMapper.deleteById(id);
    }

    @Override
    @Transactional
    public Serve offSale(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)) {
            throw new ForbiddenOperationException("当前服务项不存在");
        }
        // 判断当前服务项状态是否为上架状态
        if(serve.getSaleStatus() != FoundationStatusEnum.ENABLE.getStatus()) {
            throw new ForbiddenOperationException("服务项必须为上架状态才能下架！");
        }
        // 执行下架操作
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getSaleStatus, FoundationStatusEnum.DISABLE.getStatus())
                .update();
        if (!update) {
            throw new CommonException("更新失败");
        }
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional
    public Serve onHot(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)) {
            throw new ForbiddenOperationException("当前服务项不存在");
        }
        // 判断当前服务项是否设置为非热门状态
        if(serve.getIsHot() != FoundationStatusEnum.UNHOT.getStatus()) {
            throw new ForbiddenOperationException("服务项必须为非热门状态才能设置为热门！");
        }
        // 设置热门状态操作
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getIsHot, FoundationStatusEnum.HOT.getStatus())
                .update();
        if (!update) {
            throw new CommonException("更新失败");
        }
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional
    public Serve offHot(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)) {
            throw new ForbiddenOperationException("当前服务项不存在");
        }
        // 判断当前服务项是否设置为热门状态
        if(serve.getIsHot() != FoundationStatusEnum.HOT.getStatus()) {
            throw new ForbiddenOperationException("服务项必须为热门状态才能取消！");
        }
        // 设置热门状态操作
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getIsHot, FoundationStatusEnum.UNHOT.getStatus())
                .update();
        if (!update) {
            throw new CommonException("更新失败");
        }
        return baseMapper.selectById(id);
    }
}
