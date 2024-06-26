package com.jzo2o.orders.history.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.classmate.util.ClassStack;
import com.jzo2o.common.expcetions.BadRequestException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.*;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.mysql.utils.PageUtils;
import com.jzo2o.orders.history.mapper.HistoryOrdersServeMapper;
import com.jzo2o.orders.history.model.domain.HistoryOrdersServe;
import com.jzo2o.orders.history.model.domain.HistoryOrdersServeSync;
import com.jzo2o.orders.history.model.dto.request.HistoryOrdersServePageQueryReqDTO;
import com.jzo2o.orders.history.model.dto.request.HistoryOrdersServeListQueryReqDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersServeDetailResDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersServeResDTO;
import com.jzo2o.orders.history.service.IHistoryOrdersServeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.orders.history.service.IHistoryOrdersServeSyncService;
import com.jzo2o.orders.history.service.IHistoryOrdersSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务单 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-09-11
 */
@Service
@Slf4j
public class HistoryOrdersServeServiceImpl extends ServiceImpl<HistoryOrdersServeMapper, HistoryOrdersServe> implements IHistoryOrdersServeService {

    @Resource
    private IHistoryOrdersServeSyncService historyOrdersServeSyncService;
    @Override
    public PageResult<HistoryOrdersServeResDTO> queryForPage(HistoryOrdersServePageQueryReqDTO historyOrdersServePageQueryReqDTO) {
        // 1.校验日期
        checkSortTime(historyOrdersServePageQueryReqDTO.getMinSortTime(), historyOrdersServePageQueryReqDTO.getMaxSortTime());
        // 2.条件查询
        PageResult<HistoryOrdersServeResDTO> historyOrdersServeResDTOPageResult = queryForPageByCondition(historyOrdersServePageQueryReqDTO);
        return historyOrdersServeResDTOPageResult;
    }

    @Override
    public List<HistoryOrdersServeResDTO> queryForList(HistoryOrdersServeListQueryReqDTO historyOrdersServeListQueryReqDTO) {
        // 1.查询订单id列表
        // 1.1.校验时间
        checkSortTime(historyOrdersServeListQueryReqDTO.getMinSortTime(), historyOrdersServeListQueryReqDTO.getMaxSortTime());
        // 1.2.比较时间查询下限和上次查询最后一条数据的排序时间字段
        LocalDateTime maxSortTime = historyOrdersServeListQueryReqDTO.getMaxSortTime();
        if(ObjectUtils.isNotNull(historyOrdersServeListQueryReqDTO.getLastSortTime()) &&
                historyOrdersServeListQueryReqDTO.getLastSortTime().isBefore(historyOrdersServeListQueryReqDTO.getMaxSortTime())){
            maxSortTime = historyOrdersServeListQueryReqDTO.getLastSortTime();
        }
        // 1.3.查询条件,使用联合索引
        LambdaQueryWrapper<HistoryOrdersServe> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HistoryOrdersServe::getServeProviderId, UserContext.currentUserId())
                .ge(HistoryOrdersServe::getSortTime, historyOrdersServeListQueryReqDTO.getMinSortTime())
                .le(HistoryOrdersServe::getSortTime, maxSortTime);
        // 1.4.查询字段
        lambdaQueryWrapper.select(HistoryOrdersServe::getId);
        // 1.5.查询条数限制
        lambdaQueryWrapper.last(" limit 10 ");
        // 1.6.列表查询
        List<HistoryOrdersServe> historyOrdersServes = baseMapper.selectList(lambdaQueryWrapper);

        // 1.7.判空
        if(CollUtils.isEmpty(historyOrdersServes)) {
            return new ArrayList<>();
        }
        // 2.根据id查询
        // 2.1.转换出id列表
        List<Long> ids = historyOrdersServes.stream().map(HistoryOrdersServe::getId).collect(Collectors.toList());
        // 2.2.根据订单id获取订单信息列表信息
        List<HistoryOrdersServe> historyOrdersServeList = baseMapper.selectBatchIds(ids);
        // 2.3.按照排序字段排序
        historyOrdersServeList = historyOrdersServeList.stream().sorted(Comparator.comparing(HistoryOrdersServe::getSortTime).reversed()).collect(Collectors.toList());
        // 2.4.转换成分页信息
        return BeanUtils.copyToList(historyOrdersServeList, HistoryOrdersServeResDTO.class);
    }

    @Override
    public HistoryOrdersServeDetailResDTO queryDetailById(Long id) {
        // 1.查询历史订单
        if(ObjectUtils.isNull(id)){
            return HistoryOrdersServeDetailResDTO.empty();
        }
        HistoryOrdersServe historyOrdersServe = baseMapper.selectById(id);
        if(ObjectUtils.isNull(historyOrdersServe)) {
            return HistoryOrdersServeDetailResDTO.empty();
        }
        // 2.数据组装
        HistoryOrdersServeDetailResDTO historyOrdersServeDetailResDTO = new HistoryOrdersServeDetailResDTO();
        historyOrdersServeDetailResDTO.setId(id);
        historyOrdersServeDetailResDTO.setServeStatus(historyOrdersServe.getServeStatus());
        // 服务信息
        historyOrdersServeDetailResDTO.setServeInfo(BeanUtils.toBean(historyOrdersServe, HistoryOrdersServeDetailResDTO.ServeInfo.class));
        // 客户信息
        historyOrdersServeDetailResDTO.setCustomerInfo(BeanUtils.toBean(historyOrdersServe, HistoryOrdersServeDetailResDTO.CustomerInfo.class));
        // 订单信息
        historyOrdersServeDetailResDTO.setOrdersInfo(BeanUtils.toBean(historyOrdersServe, HistoryOrdersServeDetailResDTO.OrdersInfo.class));
        // 退款信息
        historyOrdersServeDetailResDTO.setRefundInfo(BeanUtils.toBean(historyOrdersServe, HistoryOrdersServeDetailResDTO.RefundInfo.class));
        // 取消信息
        historyOrdersServeDetailResDTO.setCancelInfo(BeanUtils.toBean(historyOrdersServe, HistoryOrdersServeDetailResDTO.CancelInfo.class));

        return historyOrdersServeDetailResDTO;
    }

    @Override
    public List<Long> queryExistIdsByIds(List<Long> ids) {
        List<HistoryOrdersServe> historyOrdersServes = baseMapper.selectBatchIds(ids);
        return CollUtils.isEmpty(historyOrdersServes) ? new ArrayList<>()
                : historyOrdersServes.stream()
                .map(HistoryOrdersServe::getId).collect(Collectors.toList());
    }

    @Override
    public void migrate() {
        log.debug("历史服务单迁移开始...");
        // 查询时间开始坐标
        int offset = 0;
        int perNum = 1000;
        // 昨天开始时间
        LocalDateTime yesterDayStartTime = DateUtils.getDayStartTime(DateUtils.now().minusDays(1));
        // 昨天结束时间
        LocalDateTime yesterDayEndTime = DateUtils.getDayEndTime(DateUtils.now().minusDays(1));

        // 统计迁移数据数量
        Integer total = historyOrdersServeSyncService.countBySortTime( yesterDayStartTime,yesterDayEndTime);
        if(total <= 0){
            return;
        }

        // 分批次迁移
        while (offset < total) {
            baseMapper.migrate( yesterDayStartTime,yesterDayEndTime, offset, perNum);
            offset += perNum;
        }
        log.debug("历史服务单迁移结束。");
    }

    @Override
    public void deleteMigrated() {
        // 昨天开始时间
        LocalDateTime yesterDayStartTime = DateUtils.getDayStartTime(DateUtils.now().minusDays(1));
        // 昨天结束时间
        LocalDateTime yesterDayEndTime = DateUtils.getDayEndTime(DateUtils.now().minusDays(1));

        // 1.校验是否可以删除已迁移服务单
        // 查询即将删除的数据数量
        Integer totalOfDelete = historyOrdersServeSyncService.countBySortTime(yesterDayStartTime, yesterDayEndTime);
        if(totalOfDelete <= 0) {
            log.debug("无迁移服务单数据需要删除");
            return;
        }
        // 查询已经迁移的数量
        Integer totalMigrated = lambdaQuery().between(HistoryOrdersServe::getSortTime, yesterDayStartTime, yesterDayEndTime)
                .count();
        if (NumberUtils.null2Zero(totalMigrated) <= 0 || totalOfDelete > totalMigrated) {
            log.error("服务单未完全迁移，同步数据删除失败");
            return;
        }

        // 2.删除已经迁移数据
        historyOrdersServeSyncService.deleteBySortTime(yesterDayStartTime, yesterDayEndTime);
    }

//    private PageResult<HistoryOrdersServeResDTO> queryForPageById(HistoryOrdersServePageQueryReqDTO historyOrdersServePageQueryReqDTO) {
//        // 服务人员或机构id
//        Long serveProviderId = UserContext.currentUserId();
//        // 1.查询历史服务单
//        HistoryOrdersServe historyOrdersServe = baseMapper.selectById(historyOrdersServePageQueryReqDTO.getId());
//        // 2.根据条件判断历史服务单是否符合查询条件
//        if (historyOrdersServe == null || !serveProviderId.equals(historyOrdersServe.getServeProviderId()) ||
//                ObjectUtils.isNotNull(historyOrdersServePageQueryReqDTO.getServeStatus()) && !historyOrdersServe.getServeStatus().equals(historyOrdersServePageQueryReqDTO.getServeStatus())) {
//            return new PageResult(0L, 0L, new ArrayList<>());
//        }
//        // 3.转换成分页数据
//        HistoryOrdersServeResDTO historyOrdersServeResDTO = BeanUtils.toBean(historyOrdersServe, HistoryOrdersServeResDTO.class);
//        return new PageResult<>(1L, 1L, CollUtils.singletonList(historyOrdersServeResDTO));
//
//    }


    private PageResult<HistoryOrdersServeResDTO> queryForPageByCondition(HistoryOrdersServePageQueryReqDTO historyOrdersServePageQueryReqDTO) {

        // 1.查询历史服务单id列表，（使用联合索引提高性能）
        LambdaQueryWrapper<HistoryOrdersServe> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 1.1.查询条件
        lambdaQueryWrapper.eq(HistoryOrdersServe::getServeProviderId, UserContext.currentUserId())
                .eq(ObjectUtils.isNotEmpty(historyOrdersServePageQueryReqDTO.getId()),HistoryOrdersServe::getId,historyOrdersServePageQueryReqDTO.getId())
                .ge(HistoryOrdersServe::getSortTime, historyOrdersServePageQueryReqDTO.getMinSortTime())
                .le(HistoryOrdersServe::getSortTime, historyOrdersServePageQueryReqDTO.getMaxSortTime())
                .eq(ObjectUtils.isNotEmpty(historyOrdersServePageQueryReqDTO.getServeStatus()), HistoryOrdersServe::getServeStatus, historyOrdersServePageQueryReqDTO.getServeStatus());
        // 1.2.只查询id，使用联合索引
        lambdaQueryWrapper.select(HistoryOrdersServe::getId);
        // 1.3.排序
        lambdaQueryWrapper.orderByDesc(HistoryOrdersServe::getSortTime);

        // 1.4.分页查询历史订单id列表
        Page<HistoryOrdersServe> historyOrdersServePage = baseMapper.selectPage(PageUtils.parsePageQuery(historyOrdersServePageQueryReqDTO, HistoryOrdersServe.class), lambdaQueryWrapper);

        // 1.5.分页数据判空
        if(PageUtils.isEmpty(historyOrdersServePage)) {
            return PageResult.of(new ArrayList<>(), historyOrdersServePageQueryReqDTO.getPageSize().intValue(), historyOrdersServePage.getPages(), historyOrdersServePage.getTotal());
        }

        // 2.根据id查询订单列表
        // 2.1.转换出id列表
        List<Long> ids = historyOrdersServePage.getRecords().stream().map(HistoryOrdersServe::getId).collect(Collectors.toList());
        // 2.2.根据订单id获取订单信息列表信息
        List<HistoryOrdersServe> historyOrdersServes = baseMapper.selectBatchIds(ids);
        // 2.3.按照排序字段排序
        historyOrdersServes = historyOrdersServes.stream().sorted(Comparator.comparing(HistoryOrdersServe::getSortTime).reversed()).collect(Collectors.toList());
        // 2.4.转换成分页信息
        return PageResult.of(BeanUtils.copyToList(historyOrdersServes, HistoryOrdersServeResDTO.class),
                historyOrdersServePageQueryReqDTO.getPageSize().intValue(),
                historyOrdersServePage.getPages(),
                historyOrdersServePage.getTotal());
    }

    /**
     * 校验查询时间段是否合法
     * @param minSortTime
     * @param maxSortTime
     */
    private void checkSortTime(LocalDateTime minSortTime, LocalDateTime maxSortTime) {
        // 1.1.校验时间段
        if(maxSortTime == null || minSortTime == null) {
            throw new BadRequestException("查询时间段不能为空");
        }
        // 1.2.查询时间段不合法
        if(minSortTime.isAfter(maxSortTime)){
            throw new BadRequestException("查询时间段不合法");
        }
        // 1.3.查询时间段不得超过365天
        if(DateUtils.between(minSortTime, maxSortTime).toDays() > 365){
            throw new BadRequestException("最多支持查询365天的历史订单");
        }
    }
}
