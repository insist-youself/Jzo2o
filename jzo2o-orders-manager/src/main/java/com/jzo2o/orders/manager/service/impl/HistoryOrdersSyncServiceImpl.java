package com.jzo2o.orders.manager.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import com.jzo2o.orders.base.mapper.HistoryOrdersSyncMapper;
import com.jzo2o.orders.base.mapper.OrdersCanceledMapper;
import com.jzo2o.orders.base.mapper.OrdersMapper;
import com.jzo2o.orders.base.mapper.OrdersServeMapper;
import com.jzo2o.orders.base.model.domain.*;
import com.jzo2o.orders.manager.service.IHistoryOrdersServeSyncService;
import com.jzo2o.orders.manager.service.IHistoryOrdersSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.jzo2o.orders.base.enums.ServeStatusEnum.CANCLE;
import static com.jzo2o.orders.base.enums.ServeStatusEnum.SERVE_FINISHED;


@Service
@Slf4j
public class HistoryOrdersSyncServiceImpl extends ServiceImpl<HistoryOrdersSyncMapper, HistoryOrdersSync> implements IHistoryOrdersSyncService {

    @Resource
    private IHistoryOrdersSyncService historyOrdersSyncService;

    @Resource
    private ServeProviderApi serveProviderApi;

    @Resource
    private InstitutionStaffApi institutionStaffApi;

    @Resource
    private OrdersHistoryApi ordersHistoryApi;


    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrdersServeMapper ordersServeMapper;

    @Resource
    private OrdersCanceledMapper ordersCanceledMapper;

    @Resource
    private IHistoryOrdersServeSyncService historyOrdersServeSyncService;

    @Override
    public void deleteFinished() {
        try {
            // 昨日结束时间
            log.debug("今日之前完结的订单删除开始...");
            LocalDateTime yesterdayEndTime = DateUtils.getDayEndTime(DateUtils.now().minusDays(1));
            lambdaUpdate()
                    .le(HistoryOrdersSync::getSortTime, yesterdayEndTime)
                    .remove();
        }finally {
            log.debug("今日之前完结的订单删除结束。");
        }
    }

//    @Override
//    public List<Orders> queryFinishedOrders(Long lastId) {
//        // lastUpdateTime不为空，该值作为查询的下限值；15天前的开始时间
//        LocalDateTime minFinishedTime = DateUtils.getDayStartTime(DateUtils.now()).minusDays(16);
//        LocalDateTime maxFinishedTime = DateUtils.getDayStartTime(DateUtils.now()).minusDays(0);
//
//        // 查询已完成交易的数据
//        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.ge(Orders::getUpdateTime, minFinishedTime)
//                .gt(ObjectUtils.isNotNull(lastId), Orders::getId, lastId)
//                .lt(Orders::getUpdateTime, maxFinishedTime)
//                .in(Orders::getOrdersStatus, Arrays.asList(OrderStatusEnum.FINISHED.getStatus(), OrderStatusEnum.CANCELED.getStatus(), OrderStatusEnum.CLOSED.getStatus()))
//                .orderByAsc(Orders::getId)
//                .last(" limit 100 ");
//        return ordersMapper.selectList(lambdaQueryWrapper);
//    }
//
//    @Override
//    public void batchWriteHistory(List<Orders> ordersList) {
//        // 1.数据准备
//        // 1.1.获取订单id列表
//        List<Long> idsList = ordersList.stream()
//                .map(Orders::getId)
//                .collect(Collectors.toList());
//        // 1.2.获取服务单信息
//        List<OrdersServe> ordersServes = ordersServeMapper.selectBatchIds(idsList);
//        Map<Long, OrdersServe> ordersServeMap = CollUtils.isEmpty(ordersServes) ? new HashMap<>() :
//                ordersServes.stream().collect(Collectors.toMap(OrdersServe::getId, p ->p));
//        // 1.3.服务机构或服务人员姓名
//        Map<Long, ServeProviderSimpleResDTO> serveProviderMap = new HashMap<>(32);
//        List<Long> serveProviderIds = CollUtils.getFieldValues(ordersServes, OrdersServe::getServeProviderId);
//        List<ServeProviderSimpleResDTO> serveProviderSimpleResDTOList = serveProviderApi.batchGet(serveProviderIds);
//        if(CollUtils.isNotEmpty(serveProviderSimpleResDTOList)) {
//            serveProviderMap.putAll(serveProviderSimpleResDTOList.stream().collect(Collectors.toMap(ServeProviderSimpleResDTO::getId, p ->p)));
//        }
//
//
//        // 1.4.机构服务人员
//        Map<Long, InstitutionStaffResDTO> institutionStaffResDTOMap = new HashMap<>(10);
//        List<Long> institutionStaffIds = CollUtils.getFieldValues(ordersServes, OrdersServe::getInstitutionStaffId);
//        if(CollUtils.isNotEmpty(institutionStaffIds)) {
//            List<InstitutionStaffResDTO> institutionStaffResDTOS = institutionStaffApi.findByIds(institutionStaffIds);
//            institutionStaffResDTOMap.putAll(institutionStaffResDTOS.stream().collect(Collectors.toMap(InstitutionStaffResDTO::getId, p -> p)));
//        }
//        // 1.5.订单取消信息
//        Map<Long, OrdersCanceled> ordersCanceledMap = new HashMap<>();
//        List<OrdersCanceled> ordersCanceleds = ordersCanceledMapper.selectBatchIds(idsList);
//        if(CollUtils.isNotEmpty(ordersCanceleds)) {
//            ordersCanceledMap.putAll(ordersCanceleds.stream().collect(Collectors.toMap(OrdersCanceled::getId, p -> p)));
//        }
//
//        // 2.历史订单信息转换
//        List<HistoryOrdersSync> historyOrdersSyncList = ordersList.stream().map(orders -> {
//            HistoryOrdersSync historyOrdersSync = BeanUtils.toBean(orders, HistoryOrdersSync.class);
//            // 下单时间
//            historyOrdersSync.setPlaceOrderTime(orders.getCreateTime());
//            // 年
//            historyOrdersSync.setYear(DateUtils.getIntFormatDate(orders.getCreateTime(), "yyyy"));
//            // 月
//            historyOrdersSync.setMonth(DateUtils.getIntFormatDate(orders.getCreateTime(), "yyyyMM"));
//            // 日
//            historyOrdersSync.setDay(DateUtils.getIntFormatDate(orders.getCreateTime(), "yyyyMMdd"));
//            // 小时
//            historyOrdersSync.setHour(DateUtils.getIntFormatDate(orders.getCreateTime(), "yyyyMMddHH"));
//            // 交易完成时间
//            historyOrdersSync.setTradeFinishTime(orders.getUpdateTime());
//            // 排序时间
//            historyOrdersSync.setSortTime(DateUtils.now());
//            // 取消信息
//            if(ordersCanceledMap.containsKey(orders.getId())) {
//                OrdersCanceled ordersCanceled = ordersCanceledMap.get(orders.getId());
//                // 取消人
//                historyOrdersSync.setCancelerName(ordersCanceled.getCancelerName());
//                // 理由
//                historyOrdersSync.setCancelReason(ordersCanceled.getCancelReason());
//                // 取消时间
//                historyOrdersSync.setCancelTime(ordersCanceled.getCancelTime());
//            }
//
//            OrdersServe ordersServe = ordersServeMap.get(orders.getId());
//            if(ordersServe != null) {
//                // 复制同名属性
//                BeanUtils.copyProperties(ordersServe, historyOrdersSync, CopyOptions.create().ignoreNullValue());
//
//                // 派单时间
//                historyOrdersSync.setDispatchTime(ObjectUtils.get(ordersServe, OrdersServe::getCreateTime));
//
//                // 订单完成
//                if(OrderStatusEnum.FINISHED.getStatus().equals(orders.getOrdersStatus()) && serveProviderMap.containsKey(ordersServe.getServeProviderId())) {
//                    ServeProviderSimpleResDTO serveProviderSimpleResDTO = serveProviderMap.get(ordersServe.getServeProviderId());
//                    if (ordersServe.getServeProviderType() == UserType.WORKER) {
//                        // 服务人员姓名+手机号
//                        historyOrdersSync.setServeProviderStaffName(serveProviderSimpleResDTO.getName());
//                        historyOrdersSync.setServeProviderStaffPhone(serveProviderSimpleResDTO.getPhone());
//                    } else {
//                        // 机构
//                        historyOrdersSync.setInstitutionName(serveProviderSimpleResDTO.getName());
//                        // 订单已完成
//                        InstitutionStaffResDTO institutionStaffResDTO = institutionStaffResDTOMap.get(ordersServe.getInstitutionStaffId());
//                        // 机构服务人员姓名+手机号
//                        historyOrdersSync.setServeProviderStaffName(ObjectUtils.get(institutionStaffResDTO, InstitutionStaffResDTO::getName));
//                        historyOrdersSync.setServeProviderStaffPhone(ObjectUtils.get(institutionStaffResDTO, InstitutionStaffResDTO::getPhone));
//                    }
//                }
//
//            }
//            return historyOrdersSync;
//        }).collect(Collectors.toList());
//
//        // 3.批量插入历史订单
//        historyOrdersSyncService.saveBatch(historyOrdersSyncList);
//    }

//    @Override
//    public void writeHistorySync(Long orderId) {
//        Orders orders = ordersMapper.selectById(orderId);
//        if(ObjectUtil.isNull(orders)){
//            log.error("插入历史同步失败,订单找不到,订单id:{}",orderId);
//            return ;
//        }
//        // 历史订单信息转换
//        HistoryOrdersSync historyOrdersSync = BeanUtils.toBean(orders, HistoryOrdersSync.class);
//        // 下单时间
//        historyOrdersSync.setPlaceOrderTime(orders.getCreateTime());
//        // 年
//        historyOrdersSync.setYear(DateUtils.getIntFormatDate(orders.getCreateTime(), "yyyy"));
//        // 月
//        historyOrdersSync.setMonth(DateUtils.getIntFormatDate(orders.getCreateTime(), "yyyyMM"));
//        // 日
//        historyOrdersSync.setDay(DateUtils.getIntFormatDate(orders.getCreateTime(), "yyyyMMdd"));
//        // 小时
//        historyOrdersSync.setHour(DateUtils.getIntFormatDate(orders.getCreateTime(), "yyyyMMddHH"));
//        // 交易完成时间
//        historyOrdersSync.setTradeFinishTime(orders.getUpdateTime());
//        // 排序时间
//        historyOrdersSync.setSortTime(DateUtils.now());
//        // 取消信息
//        OrdersCanceled ordersCanceled = null;
//        if(ObjectUtil.equal(orders.getOrdersStatus(),OrderStatusEnum.CANCELED)) {
//            // 订单取消信息
//            ordersCanceled = ordersCanceledMapper.selectById(orders.getId());
//            if(ObjectUtil.isNotNull(ordersCanceled)){
//                // 取消人
//                historyOrdersSync.setCancelerName(ordersCanceled.getCancelerName());
//                // 理由
//                historyOrdersSync.setCancelReason(ordersCanceled.getCancelReason());
//                // 取消时间
//                historyOrdersSync.setCancelTime(ordersCanceled.getCancelTime());
//            }
//        }
//        // 获取服务单信息
//        OrdersServe ordersServe = ordersServeMapper.selectById(orders.getId());
//        if(ordersServe != null) {
//            // 复制同名属性
//            BeanUtils.copyProperties(ordersServe, historyOrdersSync, CopyOptions.create().ignoreNullValue());
//            // 派单时间
//            historyOrdersSync.setDispatchTime(ObjectUtils.get(ordersServe, OrdersServe::getCreateTime));
//
//            // 服务机构或服务人员id
//            Long serveProviderId = ordersServe.getServeProviderId();
//            //服务机构或服务人员信息
//            ServeProviderResDTO serveProviderResDTO = serveProviderApi.getDetail(serveProviderId);
//            // 机构服务人员
//            InstitutionStaffResDTO institutionStaffResDTO = institutionStaffApi.findById(ordersServe.getInstitutionStaffId());
//
//            // 写入服务人员信息
//            if(ObjectUtil.isNotNull(serveProviderResDTO)) {
//                if (ordersServe.getServeProviderType() == UserType.WORKER) {
//                    // 服务人员姓名+手机号
//                    historyOrdersSync.setServeProviderStaffName(serveProviderResDTO.getName());
//                    historyOrdersSync.setServeProviderStaffPhone(serveProviderResDTO.getPhone());
//                } else if(ObjectUtil.isNotNull(institutionStaffResDTO)) {
//                    // 机构
//                    historyOrdersSync.setInstitutionName(serveProviderResDTO.getName());
//                    // 机构服务人员姓名+手机号
//                    historyOrdersSync.setServeProviderStaffName(ObjectUtils.get(institutionStaffResDTO, InstitutionStaffResDTO::getName));
//                    historyOrdersSync.setServeProviderStaffPhone(ObjectUtils.get(institutionStaffResDTO, InstitutionStaffResDTO::getPhone));
//                }
//            }
//        }
//        // 插入历史订单同步
//        historyOrdersSyncService.saveOrUpdate(historyOrdersSync);
//
//        //下边向HistoryOrdersServeSync写入
//        if(ordersServe != null) {
//            HistoryOrdersServeSync historyOrdersServeSync = BeanUtils.toBean(historyOrdersSync, HistoryOrdersServeSync.class);
//            // 排序时间
//            historyOrdersServeSync.setSortTime(DateUtils.now());
//            // 复制同名属性
//            BeanUtils.copyProperties(ordersServe, historyOrdersServeSync, CopyOptions.create().ignoreNullValue());
//            //下边向HistoryOrdersServeSync写入
//            historyOrdersServeSyncService.saveOrUpdate(historyOrdersServeSync);
//        }
//
//    }

//    public void startProcessHistoryOrdersTask() {
//        // 3天前开始时间
//        LocalDateTime startTimeBefore3Day = DateUtils.getDayStartTime(DateUtils.now()).minusDays(3);
//        while (true){
//            // 1.查询历史服务单同步表中的100条数据
//            List<HistoryOrdersSync> historyOrdersSyncs = lambdaQuery().lt(HistoryOrdersSync::getUpdateTime, startTimeBefore3Day)
//                    .last(" limit 100")
//                    .list();
//            if(CollUtils.isEmpty(historyOrdersSyncs)){
//                break;
//            }
//
//            // 2.查询历史订单同步情况
//            // 2.1.订单id列表
//            List<Long> ids = historyOrdersSyncs.stream()
//                    .map(HistoryOrdersSync::getId)
//                    .collect(Collectors.toList());
//            // 2.2.批量查询历史订单同步状况
//            // 已经同步的历史订单id列表
//            List<Long> syncedIds = ordersHistoryApi.batchGetSyncedOrdersServeIds(ids);
//            // 未完成同步的历史订单id列表
//            List<Long> noSyncedIds = CollUtils.subtractToList(ids, syncedIds);
//
//            // 3.处理已经同步完成和未完成的订单列表，完成的删除，未完成的修改更新时间重新同步
//            historyOrdersSyncService.processHistoryOrders(syncedIds, noSyncedIds);
//        }
//    }

//    /**
//     * 处理历史同步记录，
//     * @param syncedIds 已经同步的历史订单id里诶博爱
//     * @param noSyncedIds 未完成同步的订单id列表
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public void processHistoryOrders(Collection<Long> syncedIds, Collection<Long> noSyncedIds) {
//        // 1.删除已经同步的历史订单同步记录
//        if(CollUtils.isNotEmpty(syncedIds)) {
//            historyOrdersSyncService.removeByIds(syncedIds);
//        }
//        // 2.更新已经同步的历史订单同步记录更新时间，以便重新同步
//        if(CollUtils.isNotEmpty(noSyncedIds)) {
//            HistoryOrdersSync historyOrdersSync = new HistoryOrdersSync();
//            historyOrdersSync.setUpdateTime(DateUtils.now());
//            LambdaUpdateWrapper<HistoryOrdersSync> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
//            lambdaUpdateWrapper.in(HistoryOrdersSync::getId, noSyncedIds);
//            historyOrdersSyncService.update(historyOrdersSync, lambdaUpdateWrapper);
//        }
//    }
}
