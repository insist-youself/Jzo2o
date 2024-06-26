package com.jzo2o.orders.manager.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jzo2o.api.customer.InstitutionStaffApi;
import com.jzo2o.api.customer.ServeProviderApi;
import com.jzo2o.api.customer.dto.response.InstitutionStaffResDTO;
import com.jzo2o.api.customer.dto.response.ServeProviderResDTO;
import com.jzo2o.api.customer.dto.response.ServeProviderSimpleResDTO;
import com.jzo2o.api.orders.OrdersHistoryApi;
import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.orders.base.enums.OrderStatusEnum;
import com.jzo2o.orders.base.mapper.OrdersCanceledMapper;
import com.jzo2o.orders.base.mapper.OrdersMapper;
import com.jzo2o.orders.base.mapper.OrdersServeMapper;
import com.jzo2o.orders.base.model.domain.HistoryOrdersServeSync;
import com.jzo2o.orders.base.mapper.HistoryOrdersServeSyncMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.base.model.domain.OrdersCanceled;
import com.jzo2o.orders.base.model.domain.OrdersServe;
import com.jzo2o.orders.manager.service.IHistoryOrdersServeSyncService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.jzo2o.orders.base.enums.ServeStatusEnum.CANCLE;
import static com.jzo2o.orders.base.enums.ServeStatusEnum.SERVE_FINISHED;

/**
 * <p>
 * 服务任务 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-09-14
 */
@Service
public class HistoryOrdersServeSyncServiceImpl extends ServiceImpl<HistoryOrdersServeSyncMapper, HistoryOrdersServeSync> implements IHistoryOrdersServeSyncService {

    @Resource
    private IHistoryOrdersServeSyncService historyOrdersServeSyncService;

    @Resource
    private OrdersHistoryApi ordersHistoryApi;

    @Resource
    private InstitutionStaffApi institutionStaffApi;

    @Resource
    private OrdersServeMapper ordersServeMapper;

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private ServeProviderApi serveProviderApi;

    @Resource
    private OrdersCanceledMapper ordersCanceledMapper;

    @Override
    public void deleteFinished() {

        try{
            log.debug("今日之前完结的服务单删除开始...");
            LocalDateTime yesterdayEndTime = DateUtils.getDayEndTime(DateUtils.now().minusDays(1));
            lambdaUpdate().le(HistoryOrdersServeSync::getSortTime, yesterdayEndTime)
                            .remove();
        }finally {
            log.debug("今日之前完结的服务单删除结束。");
        }
    }

//    @Override
//    public List<OrdersServe> queryFinishedOrdersServe(Long lastId) {
//        // lastFinishedTime不为空，该值作为查询的下限值；15天前的开始时间
//        LocalDateTime minFinishedTime = DateUtils.getDayStartTime(DateUtils.now()).minusDays(16) ;
//        LocalDateTime maxFinishedTime = DateUtils.getDayStartTime(DateUtils.now()).minusDays(0);
//        // 查询已完成交易的数据
//        LambdaQueryWrapper<OrdersServe> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.ge(OrdersServe::getCreateTime, minFinishedTime)
//                .le(OrdersServe::getCreateTime, maxFinishedTime)
//                .gt(ObjectUtils.isNotNull(lastId), OrdersServe::getId, lastId)
//                .in(OrdersServe::getServeStatus, Arrays.asList(SERVE_FINISHED.getStatus(), CANCLE.getStatus()))
//                .orderByAsc(OrdersServe::getId)
//                .last(" limit 100 ");
//        return ordersServeMapper.selectList(lambdaQueryWrapper);
//    }
//
//
//    @Override
//    public void batchWriteHistory(List<OrdersServe> ordersServeList) {
//
//
//        // 1.数据准备
//        // 1.1.获取服务单id列表
//        List<Long> idsList = ordersServeList.stream()
//                .map(OrdersServe::getId)
//                .collect(Collectors.toList());
//        // 1.2.获取订单信息(订单id和服务单id一致)
//        List<Orders> ordersList = ordersMapper.selectBatchIds(idsList);
//        Map<Long, OrdersServe> ordersServeMap = ordersServeList.stream().collect(Collectors.toMap(OrdersServe::getId, p ->p));
//        // 1.3.服务机构或服务人员姓名
//        Map<Long, ServeProviderSimpleResDTO> serveProviderMap = new HashMap<>(32);
//        List<Long> serveProviderIds = CollUtils.getFieldValues(ordersServeList, OrdersServe::getServeProviderId);
//        List<ServeProviderSimpleResDTO> serveProviderSimpleResDTOList = serveProviderApi.batchGet(serveProviderIds);
//        if(CollUtils.isNotEmpty(serveProviderSimpleResDTOList)) {
//            serveProviderMap.putAll(serveProviderSimpleResDTOList.stream().collect(Collectors.toMap(ServeProviderSimpleResDTO::getId, p ->p)));
//        }
//        // 1.4.机构服务人员
//        Map<Long, InstitutionStaffResDTO> institutionStaffResDTOMap = new HashMap<>(10);
//        List<Long> institutionStaffIds = CollUtils.getFieldValues(ordersServeList, OrdersServe::getInstitutionStaffId);
//        if(CollUtils.isNotEmpty(institutionStaffIds)) {
//            List<InstitutionStaffResDTO> institutionStaffResDTOS = institutionStaffApi.findByIds(institutionStaffIds);
//            institutionStaffResDTOMap.putAll(institutionStaffResDTOS.stream().collect(Collectors.toMap(InstitutionStaffResDTO::getId, p -> p)));
//        }
//
//        // 1.5.订单取消信息
//        Map<Long, OrdersCanceled> ordersCanceledMap = new HashMap<>();
//        List<OrdersCanceled> ordersCanceleds = ordersCanceledMapper.selectBatchIds(idsList);
//        if(CollUtils.isNotEmpty(ordersCanceleds)) {
//            ordersCanceledMap.putAll(ordersCanceleds.stream().collect(Collectors.toMap(OrdersCanceled::getId, p -> p)));
//        }
//
//        // 2.历史订单信息转换
//        List<HistoryOrdersServeSync> historyOrdersSyncList = ordersList.stream().map(orders -> {
//            HistoryOrdersServeSync historyOrdersServeSync = BeanUtils.toBean(orders, HistoryOrdersServeSync.class);
//            // 排序时间
//            historyOrdersServeSync.setSortTime(DateUtils.now());
//
//            // 取消信息
//            if(ordersCanceledMap.containsKey(orders.getId())) {
//                OrdersCanceled ordersCanceled = ordersCanceledMap.get(orders.getId());
//                // 取消时间
//                historyOrdersServeSync.setRefundTime(ordersCanceled.getCancelTime());
//                // 理由
//                historyOrdersServeSync.setRefundReason(ordersCanceled.getCancelReason());
//            }
//            // 服务单位
//            historyOrdersServeSync.setServeNum(orders.getPurNum());
//            OrdersServe ordersServe = ordersServeMap.get(orders.getId());
//            if(ordersServe != null) {
//                // 复制同名属性
//                BeanUtils.copyProperties(ordersServe, historyOrdersServeSync, CopyOptions.create().ignoreNullValue());
//                // 订单完成
//                if(OrderStatusEnum.FINISHED.getStatus().equals(orders.getOrdersStatus())) {
//                    ServeProviderSimpleResDTO serveProviderSimpleResDTO = serveProviderMap.get(ordersServe.getServeProviderId());
//                    if (ordersServe.getServeProviderType() == UserType.WORKER) {
//                        // 服务人员姓名+手机号
//                        historyOrdersServeSync.setServeProviderStaffName(serveProviderSimpleResDTO.getName());
//                        historyOrdersServeSync.setServeProviderStaffPhone(serveProviderSimpleResDTO.getPhone());
//                    } else {
//                        // 订单已完成
//                        InstitutionStaffResDTO institutionStaffResDTO = institutionStaffResDTOMap.get(ordersServe.getInstitutionStaffId());
//                        // 机构服务人员姓名+手机号
//                        historyOrdersServeSync.setServeProviderStaffName(ObjectUtils.get(institutionStaffResDTO, InstitutionStaffResDTO::getName));
//                        historyOrdersServeSync.setServeProviderStaffPhone(ObjectUtils.get(institutionStaffResDTO, InstitutionStaffResDTO::getPhone));
//                        historyOrdersServeSync.setInstitutionStaffName(historyOrdersServeSync.getServeProviderStaffName());
//                    }
//                }
//                // 退款
//
//            }
//            return historyOrdersServeSync;
//        }).collect(Collectors.toList());
//
//        // 3.批量插入历史订单
//        this.saveBatch(historyOrdersSyncList);
//
//    }

//
//    @Override
//    public void startProcessHistoryOrdersServeTask() {
//        // 3天前开始时间
//        LocalDateTime startTimeBefore3Day = DateUtils.getDayStartTime(DateUtils.now()).minusDays(3);
//        while (true){
//            // 1.查询历史服务单同步表中的100条数据
//            List<HistoryOrdersServeSync> historyOrdersServeSyncs = lambdaQuery().lt(HistoryOrdersServeSync::getUpdateTime, startTimeBefore3Day)
//                    .last(" limit 100")
//                    .list();
//            if(CollUtils.isEmpty(historyOrdersServeSyncs)){
//                break;
//            }
//
//            // 2.查询历史订单同步情况
//            // 2.1.订单id列表
//            List<Long> ids = historyOrdersServeSyncs.stream()
//                    .map(HistoryOrdersServeSync::getId)
//                    .collect(Collectors.toList());
//            // 2.2.批量查询历史订单同步状况
//            // 已经同步的历史订单id列表
//            List<Long> syncedIds = ordersHistoryApi.batchGetSyncedOrdersServeIds(ids);
//            // 未完成同步的历史订单id列表
//            List<Long> noSyncedIds = CollUtils.subtractToList(ids, syncedIds);
//
//            // 3.处理已经同步完成和未完成的订单列表，完成的删除，未完成的修改更新时间重新同步
//            historyOrdersServeSyncService.processHistoryOrdersServe(syncedIds, noSyncedIds);
//        }
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void processHistoryOrdersServe(List<Long> syncedIds, List<Long> noSyncedIds) {
//        // 1.删除已经同步的历史订单同步记录
//        if(CollUtils.isNotEmpty(syncedIds)) {
//            this.removeByIds(syncedIds);
//        }
//        // 2.更新已经同步的历史订单同步记录更新时间，以便重新同步
//        if(CollUtils.isNotEmpty(noSyncedIds)) {
//            HistoryOrdersServeSync historyOrdersServeSync = new HistoryOrdersServeSync();
//            historyOrdersServeSync.setUpdateTime(DateUtils.now());
//            LambdaUpdateWrapper<HistoryOrdersServeSync> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
//            lambdaUpdateWrapper.in(HistoryOrdersServeSync::getId, noSyncedIds);
//            this.update(historyOrdersServeSync, lambdaUpdateWrapper);
//        }
//    }

}
