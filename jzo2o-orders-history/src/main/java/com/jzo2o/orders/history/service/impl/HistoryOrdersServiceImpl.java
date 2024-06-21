package com.jzo2o.orders.history.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jzo2o.common.expcetions.BadRequestException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.*;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.mysql.utils.PageUtils;
import com.jzo2o.orders.history.model.domain.HistoryOrders;
import com.jzo2o.orders.history.mapper.HistoryOrdersMapper;
import com.jzo2o.orders.history.model.domain.HistoryOrdersServe;
import com.jzo2o.orders.history.model.dto.request.HistoryOrdersListQueryReqDTO;
import com.jzo2o.orders.history.model.dto.request.HistoryOrdersPageQueryReqDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersDetailResDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersListResDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersPageResDTO;
import com.jzo2o.orders.history.service.IHistoryOrdersServeSyncService;
import com.jzo2o.orders.history.service.IHistoryOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.orders.history.service.IHistoryOrdersSyncService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-09-11
 */
@Service
public class HistoryOrdersServiceImpl extends ServiceImpl<HistoryOrdersMapper, HistoryOrders> implements IHistoryOrdersService {

    @Resource
    private IHistoryOrdersSyncService historyOrdersSyncService;

    @Override
    public List<HistoryOrdersListResDTO> queryUserOrderForList(HistoryOrdersListQueryReqDTO historyOrdersListQueryReqDTO) {
        // 1.校验查询时间段不能超过1年
        checkSortTime(historyOrdersListQueryReqDTO.getMinSortTime(), historyOrdersListQueryReqDTO.getMaxSortTime());
        // 2.根据条件查询历史订单id列表
        // 2.1.计算排序字段最大值，lastSortTime用于滚动分页
        LocalDateTime maxSortTime = ObjectUtils.isNull(historyOrdersListQueryReqDTO.getLastSortTime()) ?
                historyOrdersListQueryReqDTO.getMaxSortTime() :
                DateUtils.getMin(historyOrdersListQueryReqDTO.getMaxSortTime(), historyOrdersListQueryReqDTO.getLastSortTime());
        LambdaQueryWrapper<HistoryOrders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 2.2.查询条件
        lambdaQueryWrapper.eq(HistoryOrders::getUserId, UserContext.currentUserId())
                .ge(HistoryOrders::getSortTime, historyOrdersListQueryReqDTO.getMinSortTime())
                .le(HistoryOrders::getSortTime, maxSortTime);
        // 2.3.查询目标字段id
        lambdaQueryWrapper.select(HistoryOrders::getId);
        // 2.4.查询排序
        lambdaQueryWrapper.orderByDesc(HistoryOrders::getSortTime);
        // 2.5.数量限制，10条
        lambdaQueryWrapper.last(" limit 10 ");
        // 2.6.查询历史订单，并判空
        List<HistoryOrders> historyOrders = baseMapper.selectList(lambdaQueryWrapper);
        if(CollUtils.isEmpty(historyOrders)) {
            return new ArrayList<>();
        }
        // 3.根据订单id列表查询订单信息
        // 3.1.转换订单id列表
        List<Long> ids = historyOrders.stream().map(HistoryOrders::getId).collect(Collectors.toList());
        // 3.2.根据订单id列表批量查询订单信息
        List<HistoryOrders> historyOrdersAllInfo = baseMapper.selectBatchIds(ids);
        // 3.3.转换成dto，并返回
        return BeanUtils.copyToList(historyOrdersAllInfo, HistoryOrdersListResDTO.class);
    }

    @Override
    public PageResult<HistoryOrdersPageResDTO> queryForPage(HistoryOrdersPageQueryReqDTO historyOrdersPageQueryReqDTO) {
        // 1.校验查询时间段不能超过1年
        checkSortTime(historyOrdersPageQueryReqDTO.getMinSortTime(), historyOrdersPageQueryReqDTO.getMaxSortTime());
        // 2.查询历史订单分页列表数据
        PageResult<HistoryOrdersPageResDTO> historyOrdersPageResDTOPageResult = queryOrderByCondition(historyOrdersPageQueryReqDTO);
        return historyOrdersPageResDTOPageResult;
    }

    @Override
    public HistoryOrdersDetailResDTO getDetailById(Long id) {
        HistoryOrdersDetailResDTO historyOrdersDetailResDTO = new HistoryOrdersDetailResDTO();
        // 1.查询订单信息
        HistoryOrders historyOrders = baseMapper.selectById(id);
        // 判空
        if(ObjectUtils.isNull(historyOrders)){
            return historyOrdersDetailResDTO;
        }
        // 2.数据转换 订单状态 500：订单完成，600：已取消（未支付），700：已关闭（有支付，且退款）
        // 2.1.订单信息
        HistoryOrdersDetailResDTO.OrderInfo orderInfo = BeanUtils.toBean(historyOrders, HistoryOrdersDetailResDTO.OrderInfo.class);
        historyOrdersDetailResDTO.setOrderInfo(orderInfo);

        // 2.2.服务信息、支付信息
        if(historyOrders.getOrdersStatus() == 500 || historyOrders.getOrdersStatus() == 700) {
            HistoryOrdersDetailResDTO.PayInfo payInfo = BeanUtils.toBean(historyOrders, HistoryOrdersDetailResDTO.PayInfo.class);
            historyOrdersDetailResDTO.setPayInfo(payInfo);
        }

        // 2.3.退款信息
        if(historyOrders.getOrdersStatus() == 700){
            HistoryOrdersDetailResDTO.RefundInfo refundInfo = BeanUtils.toBean(historyOrders, HistoryOrdersDetailResDTO.RefundInfo.class);
            historyOrdersDetailResDTO.setRefundInfo(refundInfo);
        }

        // 2.4.取消信息
        if(historyOrders.getOrdersStatus() == 600 || historyOrders.getOrdersStatus() == 700) {
            HistoryOrdersDetailResDTO.CancelInfo cancelInfo = BeanUtils.toBean(historyOrders, HistoryOrdersDetailResDTO.CancelInfo.class);
            historyOrdersDetailResDTO.setCancelInfo(cancelInfo);
        }
        // 2.5.服务信息
        HistoryOrdersDetailResDTO.ServeInfo serveInfo = BeanUtils.toBean(historyOrders, HistoryOrdersDetailResDTO.ServeInfo.class);
        historyOrdersDetailResDTO.setServeInfo(serveInfo);

        // 2.6.状态流程信息
        List<HistoryOrdersDetailResDTO.OrdersProgress> ordersPresses = new ArrayList<>();
        if(historyOrders.getOrdersStatus().equals(600)) {
            // 待支付节点
            ordersPresses.add(new HistoryOrdersDetailResDTO.OrdersProgress(0, historyOrders.getPlaceOrderTime()));
        }
        // 已支付节点
        ordersPresses.add(new HistoryOrdersDetailResDTO.OrdersProgress(100, historyOrders.getPayTime()));
        // 派单完成
        ordersPresses.add(new HistoryOrdersDetailResDTO.OrdersProgress(200, historyOrders.getDispatchTime()));
        // 服务完成时间
        ordersPresses.add(new HistoryOrdersDetailResDTO.OrdersProgress(500, historyOrders.getRealServeEndTime()));
        // 异常节点关闭/取消
        ordersPresses.add(new HistoryOrdersDetailResDTO.OrdersProgress(historyOrders.getOrdersStatus(), historyOrders.getCancelTime()));
        // 2.7.过滤掉无时间的节点
        ordersPresses = ordersPresses.stream()
                .filter(ordersPress -> ordersPress.getDateTime() != null)
                // 按照升序排序
                .sorted(Comparator.comparing(HistoryOrdersDetailResDTO.OrdersProgress::getDateTime))
                .collect(Collectors.toList());
        historyOrdersDetailResDTO.setOrdersProgresses(ordersPresses);

        // 2.8.返回历史订单明细
        return historyOrdersDetailResDTO;
    }

    @Override
    public List<Long> queryExistIdsByIds(List<Long> ids) {
        List<HistoryOrders> historyOrders = baseMapper.selectBatchIds(ids);
        if(CollUtils.isEmpty(historyOrders)) {
            return new ArrayList<>();
        }
        return historyOrders.stream()
                .map(HistoryOrders::getId)
                .collect(Collectors.toList());
    }

    @Override
    public void migrate() {


    }

    @Override
    public void deleteMigrated() {
        // 昨天开始时间
        LocalDateTime yesterDayStartTime = DateUtils.getDayStartTime(DateUtils.now().minusDays(1));
        // 昨天结束时间
        LocalDateTime yesterDayEndTime = DateUtils.getDayEndTime(DateUtils.now().minusDays(1));

        // 1.校验是否可以删除已迁移订单
        // 查询即将删除的数据数量
        Integer totalOfDelete = historyOrdersSyncService.countBySortTime(yesterDayStartTime, yesterDayEndTime);
        if(totalOfDelete <= 0) {
            log.debug("无迁移的订单数据需要删除");
            return;
        }
        // 查询已经迁移的数量
//        Integer totalMigrated = lambdaQuery().le(HistoryOrders::getSortTime, yesterDayEndTime)
//                .count();
        Integer totalMigrated = lambdaQuery().between(HistoryOrders::getSortTime, yesterDayStartTime, yesterDayEndTime)
                .count();
        if (NumberUtils.null2Zero(totalMigrated) <= 0 || totalOfDelete > totalMigrated) {
            log.error("订单未完全迁移，同步数据删除失败");
            return;
        }

        // 2.删除已经迁移数据
        historyOrdersSyncService.deleteBySortTime(yesterDayStartTime, yesterDayEndTime);
    }
//    /**
//     * 根据订单id直接查询历史订单信息
//     * @param historyOrdersPageQueryReqDTO
//     * @return
//     */
//    private PageResult<HistoryOrdersPageResDTO> queryOrderById(HistoryOrdersPageQueryReqDTO historyOrdersPageQueryReqDTO) {
//        // 1.查询订单信息
//        HistoryOrders historyOrders = baseMapper.selectById(historyOrdersPageQueryReqDTO.getId());
//        // 2.判空 + 订单是否符合条件校验
//        if( //判空
//                ObjectUtils.isNull(historyOrders) ||
//                // 订单排序字段值超出查询时间范围
//                historyOrders.getSortTime().isBefore(historyOrdersPageQueryReqDTO.getMinSortTime()) ||
//                historyOrders.getSortTime().isAfter(historyOrdersPageQueryReqDTO.getMaxSortTime()) ||
//                // 订单状态和查询状态不一致
//                ObjectUtils.isNotNull(historyOrdersPageQueryReqDTO.getOrdersStatus()) && !historyOrdersPageQueryReqDTO.getOrdersStatus().equals(historyOrders.getOrdersStatus()) ||
//                // 服务人员手机号和订单服务人员手机号不一致
//                StringUtils.isNotEmpty(historyOrdersPageQueryReqDTO.getServeProviderStaffPhone()) && !historyOrdersPageQueryReqDTO.getServeProviderStaffPhone().equals(historyOrders.getServeProviderStaffPhone())){
//            return PageResult.of(new ArrayList<>(), historyOrdersPageQueryReqDTO.getPageSize().intValue(), 0L, 0L);
//        }
//        // 3.历史订单转化，并返回信息
//        HistoryOrdersPageResDTO historyOrdersPageResDTO = BeanUtils.toBean(historyOrders, HistoryOrdersPageResDTO.class);
//        return PageResult.of(CollUtils.singletonList(historyOrdersPageResDTO), historyOrdersPageQueryReqDTO.getPageSize().intValue(), 1L, 1L);
//    }

    private PageResult<HistoryOrdersPageResDTO> queryOrderByCondition(HistoryOrdersPageQueryReqDTO historyOrdersPageQueryReqDTO) {
        // 1.根据条件查询订单id列表
        LambdaQueryWrapper<HistoryOrders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 1.1.查询条件
        lambdaQueryWrapper.ge(HistoryOrders::getSortTime, historyOrdersPageQueryReqDTO.getMinSortTime())
                .le(HistoryOrders::getSortTime, historyOrdersPageQueryReqDTO.getMaxSortTime())
                .eq(ObjectUtils.isNotEmpty(historyOrdersPageQueryReqDTO.getId()),HistoryOrders::getId,historyOrdersPageQueryReqDTO.getId())
                .eq(StringUtils.isNotEmpty(historyOrdersPageQueryReqDTO.getServeProviderStaffPhone()), HistoryOrders::getServeProviderStaffPhone, historyOrdersPageQueryReqDTO.getServeProviderStaffPhone())
                .eq(ObjectUtils.isNotNull(historyOrdersPageQueryReqDTO.getOrdersStatus()), HistoryOrders::getOrdersStatus, historyOrdersPageQueryReqDTO.getOrdersStatus())
                .eq(ObjectUtils.isNotNull(historyOrdersPageQueryReqDTO.getPayStatus()), HistoryOrders::getPayStatus, historyOrdersPageQueryReqDTO.getPayStatus())
                .eq(ObjectUtils.isNotNull(historyOrdersPageQueryReqDTO.getRefundStatus()), HistoryOrders::getRefundStatus, historyOrdersPageQueryReqDTO.getRefundStatus());
        // 1.2.查询目标字段
        lambdaQueryWrapper.select(HistoryOrders::getId);

        // 1.3.排序字段
        lambdaQueryWrapper.orderByDesc(HistoryOrders::getSortTime);

        // 1.4.历史订单分页信息
        Page<HistoryOrders> historyOrdersPage = PageUtils.parsePageQuery(historyOrdersPageQueryReqDTO, HistoryOrders.class);

        // 1.5.查询历史订单列表（订单信息只包含id）
        historyOrdersPage = baseMapper.selectPage(historyOrdersPage, lambdaQueryWrapper);

        // 1.6.订单信息判空
        if(PageUtils.isEmpty(historyOrdersPage)) {
            return PageResult.of(new ArrayList<>(),
                    historyOrdersPageQueryReqDTO.getPageSize().intValue(),
                    historyOrdersPage.getPages(),
                    historyOrdersPage.getTotal());
        }
        // 2.更加历史订单id列表查询订单信息
        // 2.1.转化历史订单id列表
        List<Long> ids = historyOrdersPage.getRecords()
                .stream().map(HistoryOrders::getId)
                .collect(Collectors.toList());
        // 2.2.查询历史订单列表
        List<HistoryOrders> historyOrders = baseMapper.selectBatchIds(ids);
        // 2.3.历史订单列表转化成dto
        List<HistoryOrdersPageResDTO> historyOrdersPageResDTOS = historyOrders.stream()
                .sorted(Comparator.comparing(HistoryOrders::getSortTime).reversed())
                .map(historyOrder -> BeanUtils.toBean(historyOrder, HistoryOrdersPageResDTO.class))
                .collect(Collectors.toList());
        // 2.4.返回分页数据
        return PageResult.of(historyOrdersPageResDTOS,
                historyOrdersPageQueryReqDTO.getPageSize().intValue(),
                historyOrdersPage.getPages(),
                historyOrdersPage.getTotal());
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
