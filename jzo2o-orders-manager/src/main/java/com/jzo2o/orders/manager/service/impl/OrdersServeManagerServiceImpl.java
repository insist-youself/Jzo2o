package com.jzo2o.orders.manager.service.impl;

import cn.hutool.db.DbRuntimeException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.customer.EvaluationApi;
import com.jzo2o.api.customer.InstitutionStaffApi;
import com.jzo2o.api.customer.dto.response.EvaluationScoreResDTO;
import com.jzo2o.api.customer.dto.response.InstitutionStaffResDTO;
import com.jzo2o.api.foundations.RegionApi;
import com.jzo2o.api.foundations.ServeItemApi;
import com.jzo2o.api.foundations.ServeTypeApi;
import com.jzo2o.api.foundations.dto.response.ServeItemResDTO;
import com.jzo2o.api.foundations.dto.response.ServeItemSimpleResDTO;
import com.jzo2o.api.orders.dto.response.InstitutionStaffServeCountResDTO;
import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.*;
import com.jzo2o.mysql.utils.PageUtils;
import com.jzo2o.orders.base.config.OrderStateMachine;
import com.jzo2o.orders.base.enums.BreachHaviorTypeEnum;
import com.jzo2o.orders.base.enums.OrderStatusChangeEventEnum;
import com.jzo2o.orders.base.enums.OrderStatusEnum;
import com.jzo2o.orders.base.enums.ServeStatusEnum;
import com.jzo2o.orders.base.mapper.OrdersServeMapper;
import com.jzo2o.orders.base.model.domain.BreachRecord;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.base.model.domain.OrdersCanceled;
import com.jzo2o.orders.base.model.domain.OrdersServe;
import com.jzo2o.orders.base.model.dto.OrderSnapshotDTO;
import com.jzo2o.orders.base.service.IOrdersDiversionCommonService;
import com.jzo2o.orders.manager.model.dto.request.*;
import com.jzo2o.orders.manager.model.dto.response.OrdersServeDetailResDTO;
import com.jzo2o.orders.manager.model.dto.response.OrdersServeResDTO;
import com.jzo2o.orders.manager.model.dto.response.OrdersServeStatusNumResDTO;
import com.jzo2o.orders.manager.model.dto.response.ServeProviderServeResDTO;
import com.jzo2o.orders.manager.service.*;
import com.jzo2o.orders.manager.strategy.OrderCancelStrategyManager;
import com.jzo2o.redis.annotations.HashCacheClear;
import com.jzo2o.redis.helper.CacheHelper;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.jzo2o.common.constants.ErrorInfo.Code.ORDERS_CANCEL;
import static com.jzo2o.orders.base.constants.FieldConstants.SORT_BY;
import static com.jzo2o.orders.base.constants.RedisConstants.RedisKey.SERVE_ORDERS;
import static com.jzo2o.orders.base.constants.RedisConstants.RedisKey.SERVE_ORDERS_KEY;
import static com.jzo2o.orders.base.constants.RedisConstants.Ttl.SERVE_ORSERS_PAGE_TTL;
import static com.jzo2o.orders.base.enums.ServeStatusEnum.*;

/**
 * <p>
 * 服务服务单 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-07-26
 */
@Service
@Slf4j
public class OrdersServeManagerServiceImpl extends ServiceImpl<OrdersServeMapper, OrdersServe> implements IOrdersServeManagerService {

    @Resource
    private RegionApi regionApi;

    @Resource
    private CacheHelper cacheHelper;

    @Resource
    private ServeItemApi serveItemApi;

    @Resource
    private InstitutionStaffApi institutionStaffApi;


    @Resource
    private IOrdersManagerService ordersManagerService;


    @Resource
    private ServeTypeApi serveTypeApi;

    @Resource
    private IOrdersDiversionCommonService ordersDiversionService;
    @Resource
    private OrderStateMachine orderStateMachine;

    @Resource
    private IServeProviderSyncService serveProviderSyncService;
    @Resource
    private OrderCancelStrategyManager orderCancelStrategyManager;

    @Resource
    private IOrdersCanceledService ordersCanceledService;

    @Resource
    private IOrdersServeManagerService ordersServeManagerService;

    @Resource
    private IBreachRecordService breachRecordService;
    @Resource
    private EvaluationApi evaluationApi;

    @Override
    public PageResult<OrdersServeResDTO> queryForPage(Long currentUserId, Integer serveProviderType, OrdersServePageQueryReqDTO ordersServePageQueryReqDTO) {


        // 根据条件查询 联合索引查询
        LambdaQueryWrapper<OrdersServe> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 是否是服务人员查询，机构或师傅
        boolean isServe = UserType.INSTITUTION == serveProviderType || UserType.WORKER == serveProviderType;
        // 分库分表需要指定分片键，分片键已经开启范围查询
        lambdaQueryWrapper.ge(OrdersServe::getId, 0)
                .eq(isServe, OrdersServe::getServeProviderId, currentUserId)
                .eq(ObjectUtils.isNotNull(ordersServePageQueryReqDTO.getServeStatus()), OrdersServe::getServeStatus, ordersServePageQueryReqDTO.getServeStatus())
                .eq(ObjectUtils.isNotNull(ordersServePageQueryReqDTO.getServeItemId()), OrdersServe::getServeItemId, ordersServePageQueryReqDTO.getServeItemId())
                .eq(ObjectUtils.isNotNull(ordersServePageQueryReqDTO.getId()), OrdersServe::getId, ordersServePageQueryReqDTO.getId())
                .between(ObjectUtils.isNotNull(ordersServePageQueryReqDTO.getMinServeStartTime()), OrdersServe::getServeStartTime, ordersServePageQueryReqDTO.getMinServeStartTime(), ordersServePageQueryReqDTO.getMaxServeStartTime())
                .eq(isServe, OrdersServe::getDisplay, 1)
                //指定要查询的列
                .select(OrdersServe::getId);
        log.info("lambdaQueryWrapper hashCode : {}", lambdaQueryWrapper.hashCode());

        Integer total = baseMapper.selectCount(lambdaQueryWrapper);

        Page<OrdersServe> queryPage = new Page<>(ordersServePageQueryReqDTO.getPageNo(), ordersServePageQueryReqDTO.getPageSize());
        //排序字段，当前只支持预约时间 serveStartTime
        queryPage.addOrder(PageUtils.getOrderItems(ordersServePageQueryReqDTO, OrdersServe.class));
        // selectPage方法禁用count操作
        queryPage.setSearchCount(false);
        Page<OrdersServe> ordersServePage = baseMapper.selectPage(queryPage, lambdaQueryWrapper);

        // 分页数据空直接返回
        if (PageUtils.isEmpty(ordersServePage)) {
            return PageUtils.toPage(ordersServePage, OrdersServeResDTO.class);
        }
        List<Long> ordersServeIds = CollUtils.getFieldValues(ordersServePage.getRecords(), OrdersServe::getId);

        // 机构端需要做缓存

        List<OrdersServeResDTO> ordersServeResDTOS = cacheHelper.batchGet(String.format(SERVE_ORDERS, currentUserId), ordersServeIds, new TaskBatchDataQueryExecutor(ordersManagerService, ordersServeManagerService, serveTypeApi), OrdersServeResDTO.class, SERVE_ORSERS_PAGE_TTL);
        return PageResult.of(ordersServeResDTOS, ordersServePageQueryReqDTO.getPageSize().intValue(), PageUtils.pages(total.longValue(), ordersServePageQueryReqDTO.getPageSize()), total * 1L);

    }

    @Override
    public List<OrdersServeResDTO> queryForList(Long currentUserId, Integer serveStatus, Long sortBy) {

        LambdaQueryWrapper<OrdersServe> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ObjectUtils.isNotNull(serveStatus), OrdersServe::getServeStatus, serveStatus)
                .lt(ObjectUtils.isNotNull(sortBy), OrdersServe::getSortBy, sortBy)
                .eq(OrdersServe::getServeProviderId, currentUserId)
                .eq(OrdersServe::getDisplay, 1);

        Page<OrdersServe> queryPage = new Page<>();
        queryPage.addOrder(OrderItem.desc(SORT_BY));
        queryPage.setSearchCount(false);
        Page<OrdersServe> ordersServePage = baseMapper.selectPage(queryPage, lambdaQueryWrapper);

        if (PageUtils.isEmpty(ordersServePage)) {
            return new ArrayList<>();
        }
        List<Long> ordersServeIds = CollUtils.getFieldValues(ordersServePage.getRecords(), OrdersServe::getId);
        if (CollUtils.isEmpty(ordersServeIds)) {
            return new ArrayList<>();
        }
        // 服务单列表
        List<OrdersServeResDTO> ordersServeResDTOS = cacheHelper.batchGet(String.format(SERVE_ORDERS, currentUserId), ordersServeIds, new TaskBatchDataQueryExecutor(ordersManagerService, ordersServeManagerService, serveTypeApi), OrdersServeResDTO.class, SERVE_ORSERS_PAGE_TTL);
        return ordersServeResDTOS;
    }


    @Override
    @HashCacheClear(key = SERVE_ORDERS_KEY, fieldId = "#{id}")
    public void allocation(Long id, Long serveProviderId, Long institutionStaffId) {
        // 1.操作校验
        // 1.1.服务单是否属于当前服务机构

        OrdersServe ordersServe = queryById(id);
        AssertUtils.isNotNull(ordersServe, "操作失败");
        AssertUtils.equals(serveProviderId, ordersServe.getServeProviderId(), "操作失败");

        // 1.2.校验服务单状态,已经分配过人
        AssertUtils.equals(ordersServe.getServeStatus(), ServeStatusEnum.NO_ALLOCATION.getStatus(), "操作失败");

        // 1.3.人员合法校验
        // 获取服务人员信息，获取到视为合法
        InstitutionStaffResDTO institutionStaffResDTO = institutionStaffApi.findByIdAndInstitutionId(institutionStaffId, serveProviderId);
        AssertUtils.isNotNull(institutionStaffResDTO, "操作失败");

        // 2.更新机构服务人员
        lambdaUpdate()
                .set(OrdersServe::getInstitutionStaffId, institutionStaffId)
                .set(OrdersServe::getServeStatus, ServeStatusEnum.NO_SERVED.getStatus())
                .eq(OrdersServe::getId, id)
                .update();
    }

    @Override
    @HashCacheClear(key = SERVE_ORDERS_KEY, fieldId = "#{id}")
    public void deleteServe(Long id, Long serveProviderId, Integer serveProviderType) {

        // 1.校验服务单是否可以删除
        // 1.1.校验服务单是否所欲当前机构
        OrdersServe ordersServe = queryById(id);
        // 服务单判空
        AssertUtils.isNotNull(ordersServe, "操作失败");
        AssertUtils.equals(serveProviderId, ordersServe.getServeProviderId(), "操作失败");

        // 1.3.服务单状态，已取消订单、已退单状态的订单是可以删除的
        AssertUtils.in(ordersServe.getServeStatus(), "操作失败", ServeStatusEnum.CANCLE.getStatus());


        // 2.删除服务单
        lambdaUpdate()
                .set(OrdersServe::getDisplay, 0)
                .eq(OrdersServe::getId, id)
                .update();

    }

    @Override
    @HashCacheClear(key = SERVE_ORDERS_KEY, fieldId = "#{serveStartReqDTO.id}")
    @Transactional(rollbackFor = Exception.class)
    public void serveStart(ServeStartReqDTO serveStartReqDTO, Long serveProviderId) {
        // 1.校验服务单是否可以开始
        // 1.1.校验服务单是否所欲当前机构,或服务人员
        OrdersServe ordersServe = queryByIdAndServeProviderId(serveStartReqDTO.getId(), serveProviderId);
        // 服务单判空
        AssertUtils.isNotNull(ordersServe, "操作失败");
        AssertUtils.equals(serveProviderId, ordersServe.getServeProviderId(), "操作失败");
        // 1.2.校验订单状态是否是待待服务
        AssertUtils.equals(ordersServe.getServeStatus(), ServeStatusEnum.NO_SERVED.getStatus());

        // 2.订单开始服务
        OrdersServe updateOrderServe = BeanUtils.copyBean(serveStartReqDTO, OrdersServe.class);
        updateOrderServe.setServeStatus(ServeStatusEnum.SERVING.getStatus());
        updateOrderServe.setRealServeStartTime(DateUtils.now());

        boolean updateResult = updateByIdAndServeProviderId(serveStartReqDTO.getId(), serveProviderId, updateOrderServe);
        if (!updateResult) {
            throw new DbRuntimeException("更新失败");
        }

        // 3.订单状态机推动订单修改状态
//        OrderSnapshotDTO orderSnapshotDTO = OrderSnapshotDTO.builder().ordersStatus(OrderStatusEnum.SERVING.getStatus()).build();
        Orders orders = ordersManagerService.queryById(ordersServe.getId());
        orderStateMachine.changeStatus(orders.getUserId(), ordersServe.getId().toString(), OrderStatusChangeEventEnum.START_SERVE);
    }

    @Override
    @HashCacheClear(key = SERVE_ORDERS_KEY, fieldId = "#{serveFinishedReqDTO.id}")
    @Transactional(rollbackFor = Exception.class)
    public void serveFinished(ServeFinishedReqDTO serveFinishedReqDTO, Long serveProviderId, Integer serveProviderType) {
        // 1.校验服务单是否可以开始
        // 1.1.校验服务单是否所欲当前机构,或服务人员
        OrdersServe ordersServe = queryById(serveFinishedReqDTO.getId());
        // 服务单判空
        AssertUtils.isNotNull(ordersServe, "操作失败");
        AssertUtils.equals(serveProviderId, ordersServe.getServeProviderId(), "操作失败");
        // 1.2.校验订单状态是否是服务中
        AssertUtils.equals(ordersServe.getServeStatus(), ServeStatusEnum.SERVING.getStatus());

        // 2.订单完成服务
        OrdersServe updateOrderServe = BeanUtils.copyBean(serveFinishedReqDTO, OrdersServe.class);
        updateOrderServe.setServeStatus(ServeStatusEnum.SERVE_FINISHED.getStatus());
        updateOrderServe.setRealServeEndTime(DateUtils.now());
        boolean updateResult = updateById(updateOrderServe);
        if (!updateResult) {
            throw new DbRuntimeException("更新失败");
        }
        // 3.服务完成订单数
        serveProviderSyncService.countServeTimesAndAcceptanceNum(serveProviderId, serveProviderType);

        // 4.订单状态机推动订单修改状态
        OrderSnapshotDTO orderSnapshotDTO = OrderSnapshotDTO.builder().realServeEndTime(LocalDateTime.now()).build();
        Orders orders = ordersManagerService.queryById(ordersServe.getId());
        orderStateMachine.changeStatus(orders.getUserId(), ordersServe.getId().toString(), OrderStatusChangeEventEnum.COMPLETE_SERVE,orderSnapshotDTO);


    }


    @Override
    public OrdersServeDetailResDTO getDetail(Long id, Long serveProviderId) {
        // 1.校验服务单是否可以查询
        OrdersServe ordersServe = queryById(id);
        // 未查询到服务单
        AssertUtils.isNotNull(ordersServe, "查询失败");
        // 校验服务单是否属于当前服务人员或机构
//        AssertUtils.equals(ordersServe.getServeProviderId(), serveProviderId, "查询失败");

        // 订单信息
        Orders orders = ordersManagerService.queryById(ordersServe.getId());
        ServeItemResDTO serveItemResDTO = serveItemApi.findById(ordersServe.getServeItemId());

        OrdersServeDetailResDTO ordersServeDetailResDTO = new OrdersServeDetailResDTO();
        ordersServeDetailResDTO.setId(ordersServe.getId());
        ordersServeDetailResDTO.setServeStatus(ordersServe.getServeStatus());
        // 服务信息
        OrdersServeDetailResDTO.ServeInfo serveInfo = BeanUtils.toBean(ordersServe, OrdersServeDetailResDTO.ServeInfo.class);
        ordersServeDetailResDTO.setServeInfo(serveInfo);
        serveInfo.setServeItemName(ObjectUtils.get(orders, Orders::getServeItemName));
        serveInfo.setServeTypeName(ObjectUtils.get(serveItemResDTO, ServeItemResDTO::getServeTypeName));
        serveInfo.setUnit(ObjectUtils.get(serveItemResDTO, ServeItemResDTO::getUnit));
        // 服务数量
        serveInfo.setServeNum(ObjectUtils.get(ordersServe, OrdersServe::getPurNum));

        // 订单信息
        ordersServeDetailResDTO.setOrdersInfo(new OrdersServeDetailResDTO.OrdersInfo(ordersServe.getId(), ordersServe.getServeStartTime(), ordersServe.getOrdersAmount()));

        // 客户信息
        ordersServeDetailResDTO.setCustomerInfo(BeanUtils.toBean(orders, OrdersServeDetailResDTO.CustomerInfo.class));

        // 取消信息
        // 取消原因
        if (ServeStatusEnum.CANCLE.equals(ordersServe.getServeStatus())) {
            OrdersCanceled ordersCanceled = ordersCanceledService.getById(orders.getId());
            OrdersServeDetailResDTO.CancelInfo cancelInfo = BeanUtils.toBean(ordersCanceled, OrdersServeDetailResDTO.CancelInfo.class);
            ordersServeDetailResDTO.setCancelInfo(cancelInfo);

        }
        if (ordersServe.getServeProviderType().equals(UserType.INSTITUTION)) {
            // 机构人员
            // 机构服务人
            if (ordersServe.getInstitutionStaffId() != null) {
                InstitutionStaffResDTO institutionStaffResDTO = institutionStaffApi.findByIdAndInstitutionId(ordersServe.getInstitutionStaffId(), ordersServe.getServeProviderId());
                serveInfo.setInstitutionStaffName(ObjectUtils.get(institutionStaffResDTO, InstitutionStaffResDTO::getName));
            }
        }

        return ordersServeDetailResDTO;
    }

    @Override
    @HashCacheClear(key = SERVE_ORDERS_KEY, fieldId = "#{orderServeCancelReqDTO.id}")
    @Transactional(rollbackFor = Exception.class)
    public void cancelByProvider(OrderServeCancelReqDTO orderServeCancelReqDTO, Long serveProviderId) {

        // 1.取消服务校验
        OrdersServe ordersServe = queryById(orderServeCancelReqDTO.getId());
        // 1.1.校验服务所属机构、服务人员
        AssertUtils.isNotNull(ordersServe, "服务订单不存在");
        AssertUtils.equals(serveProviderId, ordersServe.getServeProviderId(), "只允许取消自己的服务订单");
        // 1.2.校验状态
        AssertUtils.in(ordersServe.getServeStatus(), "当前不可自行取消订单，如需取消需拨打客服热线", ServeStatusEnum.NO_ALLOCATION.getStatus(), ServeStatusEnum.NO_SERVED.getStatus());
        if (DateUtils.between(DateUtils.now(), ordersServe.getServeStartTime()).toMinutes() < 120) {
            throw new CommonException(ORDERS_CANCEL, "当前不可自行取消订单，如需取消需拨打客服热线");
        }
        // 1.3.校验取消次数
        // 1.4.每日取消次数限制 todo 暂时不放开
//        String cancelTimesOfEveryDayRedisKey = String.format(RedisConstants.RedisKey.SERVE_CANCEL_TIMES_EVERY_DAY, DateUtils.format(LocalDateTime.now(), DateUtils.DEFAULT_DATE_FORMAT_COMPACT));
//        if(redisTemplate.opsForValue().get(cancelTimesOfEveryDayRedisKey) >= 3) {
//            throw new DbRuntimeException("取消失败，超过每天最大取消次数3次");
//        }

        // 2.删除服务单
        deleteByIdAndServeProviderId(ordersServe.getId(), ordersServe.getServeProviderId());
        // 3.重新分流
        Orders orders = ordersManagerService.queryById(ordersServe.getId());
        ordersDiversionService.diversion(orders);
        // 4.违约记录
        BreachRecord breachRecord = toBreachRecord(orders, ordersServe.getServeProviderId(), ordersServe.getServeProviderType(), orderServeCancelReqDTO.getCancelReason(), BreachHaviorTypeEnum.CANCEL_NO_SERVE);
        breachRecordService.add(breachRecord);

        //5.状态机更新订单状态
//        OrderSnapshotDTO orderSnapshotDTO = OrderSnapshotDTO.builder()
//                .ordersStatus(OrderStatusEnum.DISPATCHING.getStatus()).build();

        //6.订单状态变更
        orderStateMachine.changeStatus(orders.getUserId(), String.valueOf(ordersServe.getId()), OrderStatusChangeEventEnum.SERVE_PROVIDER_CANCEL);

        // 3.服务时间重新统计
        serveProviderSyncService.countServeTimesAndAcceptanceNum(ordersServe.getServeProviderId(), ordersServe.getServeProviderType());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelByUserAndOperation(Long ordersId) {


        // 1.操作准备
        OrdersServe ordersServe = queryById(ordersId);

        // 2.修改服务单
        boolean update = lambdaUpdate()
                .set(OrdersServe::getServeStatus, ServeStatusEnum.CANCLE.getStatus())
                .eq(OrdersServe::getId, ordersServe.getId())
                .update();
        if (!update) {
            throw new DbRuntimeException("操作失败");
        }
        // 3.服务时间重新统计
        serveProviderSyncService.countServeTimesAndAcceptanceNum(ordersServe.getServeProviderId(), ordersServe.getServeProviderType());
        // 4.清理缓存
        String redisKey = String.format(SERVE_ORDERS, ordersServe.getServeProviderId());
        cacheHelper.remove(redisKey, ordersId);
    }

    @Override
    public OrdersServeStatusNumResDTO countServeStatusNum(Long serveProviderId) {

        LambdaQueryWrapper<OrdersServe> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.ge(OrdersServe::getId, 0)
                .in(OrdersServe::getServeStatus, Arrays.asList(ServeStatusEnum.NO_ALLOCATION.getStatus(), ServeStatusEnum.NO_SERVED.getStatus(), ServeStatusEnum.SERVING.getStatus()))
                .eq(OrdersServe::getServeProviderId, serveProviderId)
                .select(OrdersServe::getId, OrdersServe::getServeStatus);
        List<OrdersServe> ordersServes = baseMapper.selectList(lambdaQueryWrapper);
        if (CollUtils.isEmpty(ordersServes)) {
            return OrdersServeStatusNumResDTO.empty();
        }
        Map<Integer, Long> statusAndNumMap = ordersServes.stream().collect(Collectors.groupingBy(OrdersServe::getServeStatus, Collectors.counting()));
        // 待分配
        Long noAllocationNum = statusAndNumMap.get(ServeStatusEnum.NO_ALLOCATION.getStatus());
        // 待服务
        Long noServed = statusAndNumMap.get(ServeStatusEnum.NO_SERVED.getStatus());
        // 服务中
        Long serving = statusAndNumMap.get(ServeStatusEnum.SERVING.getStatus());
        return new OrdersServeStatusNumResDTO(NumberUtils.null2Zero(noAllocationNum), NumberUtils.null2Zero(noServed), NumberUtils.null2Zero(serving));
    }

    /**
     * 查询服务人员/机构服务数据
     *
     * @param ordersServePageQueryByCurrentUserReqDTO 分页条件
     * @return 分页结果
     */
    @Override
    public PageResult<ServeProviderServeResDTO> pageQueryByServeProvider(OrdersServePageQueryByServeProviderReqDTO ordersServePageQueryByCurrentUserReqDTO) {
        //1.构件查询条件
        Page<OrdersServe> page = PageUtils.parsePageQuery(ordersServePageQueryByCurrentUserReqDTO, OrdersServe.class);
        LambdaQueryWrapper<OrdersServe> queryWrapper = Wrappers.<OrdersServe>lambdaQuery()
                .eq(OrdersServe::getServeProviderId, ordersServePageQueryByCurrentUserReqDTO.getServeProviderId())
                .eq(OrdersServe::getServeStatus, ServeStatusEnum.SERVE_FINISHED.getStatus())
//                .lt(OrdersServe::getRealServeEndTime, LocalDateTime.now().minusDays(15))
                //TODO 测试暂时查询1小时以后的服务数据
                .lt(OrdersServe::getRealServeEndTime, LocalDateTime.now().minusHours(1));

        //2.分页查询
        Page<OrdersServe> pageResult = page(page, queryWrapper);
        if (ObjectUtils.isEmpty(pageResult.getRecords())) {
            return PageUtils.toPage(pageResult, ServeProviderServeResDTO.class);
        }

        List<OrdersServe> ordersServeList = pageResult.getRecords();

        //3.服务项数据
        List<Long> serveItemIds = ordersServeList.stream().map(OrdersServe::getServeItemId).collect(Collectors.toList());
        List<ServeItemSimpleResDTO> serveItemSimpleResDTOList = serveItemApi.listByIds(serveItemIds);
        Map<Long, String> serveItemMap = serveItemSimpleResDTOList.stream().collect(Collectors.toMap(ServeItemSimpleResDTO::getId, ServeItemSimpleResDTO::getName));

        //4.评分数据
        List<Long> ordersIds = ordersServeList.stream().map(OrdersServe::getId).collect(Collectors.toList());
        EvaluationScoreResDTO evaluationScoreResDTO = evaluationApi.queryServeProviderScoreByOrdersId(ordersIds);
        Map<String, Double> scoreMap = evaluationScoreResDTO.getScoreMap();

        //5.组装服务项、评分数据
        List<ServeProviderServeResDTO> list = ordersServeList.stream().map(o -> {
            ServeProviderServeResDTO serveProviderServeResDTO = BeanUtils.toBean(o, ServeProviderServeResDTO.class);
            serveProviderServeResDTO.setServeItemName(serveItemMap.get(o.getServeItemId()));
            if (ObjectUtils.isNotEmpty(scoreMap)) {
                serveProviderServeResDTO.setScore(scoreMap.get(o.getId().toString()));
            }
            return serveProviderServeResDTO;
        }).collect(Collectors.toList());


        //6.如果是机构，查询机构下属员工信息
        if (ObjectUtils.equal(UserType.INSTITUTION, ordersServePageQueryByCurrentUserReqDTO.getUserType())) {
            List<Long> institutionStaffIds = pageResult.getRecords().stream().map(OrdersServe::getInstitutionStaffId).collect(Collectors.toList());
            List<InstitutionStaffResDTO> institutionStaffResDTOList = institutionStaffApi.findByIds(institutionStaffIds);
            Map<Long, InstitutionStaffResDTO> institutionStaffMap = institutionStaffResDTOList.stream().collect(Collectors.toMap(InstitutionStaffResDTO::getId, i -> i));

            list.forEach(s -> {
                InstitutionStaffResDTO institutionStaffResDTO = institutionStaffMap.get(s.getInstitutionStaffId());
                s.setInstitutionStaffName(institutionStaffResDTO.getName());
                s.setInstitutionStaffPhone(institutionStaffResDTO.getPhone());
            });
        }

        //7.返回分页结果
        return PageResult.<ServeProviderServeResDTO>builder()
                .list(list)
                .pages(pageResult.getPages())
                .total(pageResult.getTotal())
                .build();
    }

//    @Override
//    public void serveTimeout() {
//        // 1.查询已经到预约服务时间，还没开始服务的服务单
//        LambdaQueryWrapper<OrdersServe> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.in(OrdersServe::getServeStatus, Arrays.asList(ServeStatusEnum.NO_SERVED.getStatus(), ServeStatusEnum.NO_ALLOCATION.getStatus()))
//                .lt(OrdersServe::getServeStartTime, LocalDateTime.now());
//        List<OrdersServe> list = ordersServeService.list(lambdaQueryWrapper);
//        if(CollUtils.isEmpty(list)) {
//            return;
//        }
//
//        for (OrdersServe ordersServe : list) {
//            try {
//                ordersServeManagerService.processTimeoutOrders(ordersServe);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//    }


//    @GlobalTransactional
//    @Lock(formatter = RedisConstants.RedisFormatter.ORDERS_SERVE_TIMEOUT, startDog = true)
//    public void processTimeoutOrders(OrdersServe ordersServe) {
//        // 1.修改取消订单信息
//        OrdersServe updateOrdersServe = new OrdersServe();
//        updateOrdersServe.setServeStatus(ServeStatusEnum.CANCLE.getStatus());
//        if(!ordersServeService.updateByIdAndServeProviderId(ordersServe.getId(), ordersServe.getServeProviderId(), updateOrdersServe)){
//            return;
//        }
//        // 2.违约记录
//        Orders orders = ordersService.queryById(ordersServe.getId());
//        BreachRecord breachRecord = toBreachRecord(orders, ordersServe.getServeProviderId(), ordersServe.getServeProviderType(), "系统自动取消订单", BreachHaviorTypeEnum.CANCEL_NO_SERVE);
//        breachRecordService.add(breachRecord);
//
//        // 3.统计当前服务单数
//        serveProviderSyncService.countServeTimesAndAcceptanceNum(ordersServe.getServeProviderId(), ordersServe.getServeProviderType());
//
////        // 4.取消订单，并退款
////        OrderCancelDTO orderCancelDTO = new OrderCancelDTO();
////        orderCancelDTO.setCurrentUserId(0L);
////        orderCancelDTO.setCurrentUserType(0);
////        orderCancelDTO.setCurrentUserName("系统");
////        orderCancelDTO.setCancelReason("系统自动取消订单");
////        orderCancelDTO.setId(ordersServe.getId());
////        orderCancelDTO.setServeStartTime(ordersServe.getServeStartTime());
////        orderCancelDTO.setUserId(orders.getUserId());
////        orderCancelStrategyManager.cancel(orderCancelDTO);
//
//    }


    private static class TaskBatchDataQueryExecutor implements CacheHelper.BatchDataQueryExecutor<Long, OrdersServeResDTO> {

        private IOrdersManagerService ordersManagerService;
        private IOrdersServeManagerService ordersServeManagerService;
        private ServeTypeApi serveTypeApi;

        public TaskBatchDataQueryExecutor(IOrdersManagerService ordersManagerService, IOrdersServeManagerService ordersServeManagerService, ServeTypeApi serveTypeApi) {
            this.ordersManagerService = ordersManagerService;
            this.ordersServeManagerService = ordersServeManagerService;
            this.serveTypeApi = serveTypeApi;
        }

        @Override
        public Map<Long, OrdersServeResDTO> execute(List<Long> objectIds, Class<OrdersServeResDTO> clazz) {

            List<OrdersServe> ordersServes = ordersServeManagerService.batchQuery(objectIds);
            if (CollUtils.isEmpty(ordersServes)) {
                return new HashMap<>();
            }
            // 服务单列表对应的订单id列表
            List<Long> ordersIds = CollUtils.getFieldValues(ordersServes, OrdersServe::getId);
            //查询订单信息
            Map<Long, Orders> ordersMap = ordersManagerService.batchQuery(ordersIds).stream().collect(Collectors.toMap(Orders::getId, p -> p));
            // 组合数据
            return ordersServes.stream().collect(Collectors.toMap(OrdersServe::getId, ordersServe -> {
                Orders orders = ordersMap.get(ordersServe.getId());
                OrdersServeResDTO ordersServeResDTO = BeanUtils.toBean(ordersServe, clazz);
                ordersServeResDTO.setServeTypeName(orders.getServeTypeName());
                ordersServeResDTO.setServeItemName(orders.getServeItemName());
                ordersServeResDTO.setServeAddress(orders.getServeAddress());
                return ordersServeResDTO;
            }));
        }
    }

    private BreachRecord toBreachRecord(Orders orders, Long serveProviderId, Integer serveProviderType, String reason, BreachHaviorTypeEnum behaviorType) {
        BreachRecord breachRecord = new BreachRecord();
        breachRecord.setId(IdUtils.getSnowflakeNextId());
        breachRecord.setServeProviderId(serveProviderId);
        breachRecord.setServeProviderType(serveProviderType);
        breachRecord.setBehaviorType(behaviorType.getType());
        breachRecord.setServeItemName(orders.getServeItemName());
        breachRecord.setServeAddress(orders.getServeAddress());
        breachRecord.setServedUserId(orders.getUserId());
        breachRecord.setServedPhone(orders.getContactsPhone());
        breachRecord.setBreachReason(reason);
        breachRecord.setOrdersId(orders.getId());
        breachRecord.setBreachDay(DateUtils.getDay());
        return breachRecord;
    }

    @Override
    public List<OrdersServe> batchQuery(List<Long> ids) {
        return lambdaQuery()
                .in(OrdersServe::getId, ids)
                .ge(OrdersServe::getServeProviderId, 0)
                .list();
    }

    @Override
    public OrdersServe queryById(Long id) {
        List<OrdersServe> list = lambdaQuery()
                .eq(OrdersServe::getId, id)
                .ge(OrdersServe::getServeProviderId, 0)
                .last(" limit 1")
                .list();
        return CollUtils.getFirst(list);
    }

    @Override
    public OrdersServe queryByIdAndServeProviderId(Long id, Long serveProviderId) {
        return lambdaQuery()
                .eq(OrdersServe::getId, id)
                .eq(OrdersServe::getServeProviderId, serveProviderId)
                .one();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateByIdAndServeProviderId(Long id, Long serveProviderId, OrdersServe ordersServe) {
        LambdaUpdateWrapper<OrdersServe> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(OrdersServe::getId, id)
                .eq(OrdersServe::getServeProviderId, serveProviderId);
        return baseMapper.update(ordersServe, lambdaUpdateWrapper) > 0;
    }

    @Override
    public List<Integer> countServeTimes(Long serveProviderId) {

        List<OrdersServe> list = lambdaQuery()
                .ge(OrdersServe::getId, 0)
                .eq(OrdersServe::getServeProviderId, serveProviderId)
                .in(OrdersServe::getServeStatus, Arrays.asList(NO_ALLOCATION.getStatus(), NO_SERVED.getStatus(), SERVING.getStatus()))
                .select(OrdersServe::getServeStartTime)
                .list();
        if (CollUtils.isEmpty(list)) {
            return null;
        }
        // 将预约时间转换成指定格式
        return list.stream()
                .map(ordersServe -> DateUtils.getFormatDate(ordersServe.getServeStartTime(), "yyyMMddHH").intValue())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Integer countNoServedNum(Long serveProviderId) {
        return lambdaQuery()
                .ge(OrdersServe::getId, 0)
                .eq(OrdersServe::getServeProviderId, serveProviderId)
                .in(OrdersServe::getServeStatus, Arrays.asList(NO_ALLOCATION.getStatus(), NO_SERVED.getStatus(), SERVING.getStatus()))
                .count();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIdAndServeProviderId(Long id, Long serveProviderId) {

        LambdaUpdateWrapper<OrdersServe> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(OrdersServe::getId, id)
                .eq(OrdersServe::getServeProviderId, serveProviderId);
        int delete = baseMapper.delete(lambdaUpdateWrapper);
        if (delete <= 0) {
            throw new DbRuntimeException("请求失败");
        }
    }

    /**
     * 根据机构服务人员id查询服务数量
     *
     * @param institutionStaffId 机构服务人员id
     * @return 服务数量
     */
    @Override
    public InstitutionStaffServeCountResDTO countByInstitutionStaffId(Long institutionStaffId) {
        LambdaQueryWrapper<OrdersServe> queryWrapper = Wrappers.<OrdersServe>lambdaQuery()
                .eq(OrdersServe::getInstitutionStaffId, institutionStaffId)
                .gt(OrdersServe::getId, 0)
                .gt(OrdersServe::getServeProviderId, 0);
        Integer count = baseMapper.selectCount(queryWrapper);
        return new InstitutionStaffServeCountResDTO(count);
    }
}
