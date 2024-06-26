package com.jzo2o.orders.base.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.customer.InstitutionStaffApi;
import com.jzo2o.api.customer.ServeProviderApi;
import com.jzo2o.api.customer.dto.response.InstitutionStaffResDTO;
import com.jzo2o.api.customer.dto.response.ServeProviderResDTO;
import com.jzo2o.api.orders.OrdersHistoryApi;
import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.orders.base.enums.OrderStatusEnum;
import com.jzo2o.orders.base.enums.ServeStatusEnum;
import com.jzo2o.orders.base.mapper.HistoryOrdersSyncMapper;
import com.jzo2o.orders.base.mapper.OrdersCanceledMapper;
import com.jzo2o.orders.base.mapper.OrdersMapper;
import com.jzo2o.orders.base.mapper.OrdersServeMapper;
import com.jzo2o.orders.base.model.domain.*;
import com.jzo2o.orders.base.service.IHistoryOrdersServeSyncCommonService;
import com.jzo2o.orders.base.service.IHistoryOrdersSyncCommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class HistoryOrdersSyncCommonServiceImpl extends ServiceImpl<HistoryOrdersSyncMapper, HistoryOrdersSync> implements IHistoryOrdersSyncCommonService {

    @Resource
    private IHistoryOrdersSyncCommonService historyOrdersSyncCommonService;

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
    private IHistoryOrdersServeSyncCommonService historyOrdersServeSyncCommonService;


    @Override
    public void writeHistorySync(Long orderId) {
        Orders orders = ordersMapper.selectById(orderId);
        if(ObjectUtil.isNull(orders)){
            log.error("插入历史同步失败,订单找不到,订单id:{}",orderId);
            return ;
        }
        // 历史订单信息转换
        HistoryOrdersSync historyOrdersSync = BeanUtils.toBean(orders, HistoryOrdersSync.class);
        // 下单时间
        historyOrdersSync.setPlaceOrderTime(orders.getCreateTime());
        // 年
        historyOrdersSync.setYear(DateUtils.getIntFormatDate(orders.getUpdateTime(), "yyyy"));
        // 月
        historyOrdersSync.setMonth(DateUtils.getIntFormatDate(orders.getUpdateTime(), "yyyyMM"));
        // 日
        historyOrdersSync.setDay(DateUtils.getIntFormatDate(orders.getUpdateTime(), "yyyyMMdd"));
        // 小时
        historyOrdersSync.setHour(DateUtils.getIntFormatDate(orders.getUpdateTime(), "yyyyMMddHH"));
        // 交易完成时间
        historyOrdersSync.setTradeFinishTime(orders.getUpdateTime());
        // 排序时间(15天后数据无法再次修改，并迁移到历史订单中)
        historyOrdersSync.setSortTime(orders.getUpdateTime().plusDays(15));
        // 取消信息
        OrdersCanceled ordersCanceled = null;
        if(ObjectUtil.equal(orders.getOrdersStatus(),OrderStatusEnum.CANCELED.getStatus()) || ObjectUtil.equal(orders.getOrdersStatus(), OrderStatusEnum.CLOSED.getStatus())){
            // 订单取消信息
            ordersCanceled = ordersCanceledMapper.selectById(orders.getId());
            if(ObjectUtil.isNotNull(ordersCanceled)){
                // 取消人
                historyOrdersSync.setCancelerName(ordersCanceled.getCancelerName());
                // 理由
                historyOrdersSync.setCancelReason(ordersCanceled.getCancelReason());
                // 取消时间
                historyOrdersSync.setCancelTime(ordersCanceled.getCancelTime());
            }
        }
        // 获取服务单信息
        OrdersServe ordersServe = ordersServeMapper.selectById(orders.getId());
        if(ordersServe != null) {
            // 复制同名属性
            BeanUtils.copyProperties(ordersServe, historyOrdersSync, CopyOptions.create().ignoreNullValue());
            // 派单时间
            historyOrdersSync.setDispatchTime(ObjectUtils.get(ordersServe, OrdersServe::getCreateTime));

            // 服务机构或服务人员id
            Long serveProviderId = ordersServe.getServeProviderId();
            //服务机构或服务人员信息
            ServeProviderResDTO serveProviderResDTO = serveProviderApi.getDetail(serveProviderId);


            // 写入服务人员信息
            if(ObjectUtil.isNotNull(serveProviderResDTO)) {
                if (ordersServe.getServeProviderType() == UserType.WORKER) {
                    // 服务人员姓名+手机号
                    historyOrdersSync.setServeProviderStaffName(serveProviderResDTO.getName());
                    historyOrdersSync.setServeProviderStaffPhone(serveProviderResDTO.getPhone());
                } else  {
                    // 机构
                    historyOrdersSync.setInstitutionName(serveProviderResDTO.getName());
                    historyOrdersSync.setInstitutionPhone(serveProviderResDTO.getPhone());
                    if(ObjectUtil.isNotEmpty(ordersServe.getInstitutionStaffId())){
                        // 机构服务人员
                        InstitutionStaffResDTO institutionStaffResDTO = institutionStaffApi.findById(ordersServe.getInstitutionStaffId());

                        // 机构服务人员姓名+手机号
                        historyOrdersSync.setServeProviderStaffName(ObjectUtils.get(institutionStaffResDTO, InstitutionStaffResDTO::getName));
                        historyOrdersSync.setServeProviderStaffPhone(ObjectUtils.get(institutionStaffResDTO, InstitutionStaffResDTO::getPhone));
                    }

                }
            }
        }
        // 插入历史订单同步
        historyOrdersSyncCommonService.saveOrUpdate(historyOrdersSync);

        //下边向HistoryOrdersServeSync写入
        if(ordersServe != null) {
            HistoryOrdersServeSync historyOrdersServeSync = BeanUtils.toBean(historyOrdersSync, HistoryOrdersServeSync.class);
            // 排序时间(15天后数据无法再次修改，并迁移到历史订单中)
//            historyOrdersServeSync.setSortTime(DateUtils.now().plusDays(15));
            // 机构服务人员姓名
            if(historyOrdersServeSync.getServeProviderType() == UserType.INSTITUTION) {
                historyOrdersServeSync.setInstitutionStaffName(historyOrdersSync.getServeProviderStaffName());
            }
            // 复制同名属性
            BeanUtils.copyProperties(ordersServe, historyOrdersServeSync, CopyOptions.create().ignoreNullValue());
            historyOrdersServeSync.setServeNum(orders.getPurNum());
            // 订单退款且服务单未在取消状态，手动设置服务单的服务状态
            if(OrderStatusEnum.CLOSED.getStatus().equals(orders.getOrdersStatus()) &&
                    !ServeStatusEnum.CANCLE.equals(ordersServe.getServeStatus())) {
                historyOrdersServeSync.setServeStatus(ServeStatusEnum.CANCLE.getStatus());
            }
            //下边向HistoryOrdersServeSync写入
            historyOrdersServeSyncCommonService.saveOrUpdate(historyOrdersServeSync);
        }

    }
}
