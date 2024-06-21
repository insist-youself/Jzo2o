//package com.jzo2o.orders.manager.service.impl;
//
//import cn.hutool.core.thread.ThreadUtil;
//import cn.hutool.core.util.IdUtil;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.jzo2o.api.customer.AddressBookApi;
//import com.jzo2o.api.customer.CommonUserApi;
//import com.jzo2o.api.customer.EvaluationApi;
//import com.jzo2o.api.customer.InstitutionStaffApi;
//import com.jzo2o.api.customer.dto.request.EvaluationSubmitReqDTO;
//import com.jzo2o.api.customer.dto.request.InstitutionStaffAddReqDTO;
//import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
//import com.jzo2o.api.customer.dto.response.CommonUserResDTO;
//import com.jzo2o.api.customer.dto.response.InstitutionStaffResDTO;
//import com.jzo2o.api.foundations.RegionApi;
//import com.jzo2o.api.foundations.ServeApi;
//import com.jzo2o.api.foundations.ServeItemApi;
//import com.jzo2o.api.foundations.dto.response.ConfigRegionInnerResDTO;
//import com.jzo2o.api.foundations.dto.response.ServeTypeCategoryResDTO;
//import com.jzo2o.api.trade.enums.TradingStateEnum;
//import com.jzo2o.common.constants.UserType;
//import com.jzo2o.common.expcetions.CommonException;
//import com.jzo2o.common.model.CurrentUserInfo;
//import com.jzo2o.common.model.msg.TradeStatusMsg;
//import com.jzo2o.common.utils.CollUtils;
//import com.jzo2o.common.utils.DateUtils;
//import com.jzo2o.common.utils.IdUtils;
//import com.jzo2o.common.utils.ObjectUtils;
//import com.jzo2o.mvc.utils.UserContext;
//import com.jzo2o.orders.base.config.OrderStateMachine;
//import com.jzo2o.orders.base.enums.OrderStatusChangeEventEnum;
//import com.jzo2o.orders.base.enums.OrderStatusEnum;
//import com.jzo2o.orders.base.mapper.OrdersMapper;
//import com.jzo2o.orders.base.model.domain.Orders;
//import com.jzo2o.orders.base.model.domain.OrdersServe;
//import com.jzo2o.orders.manager.model.dto.request.PlaceOrderReqDTO;
//import com.jzo2o.orders.manager.model.dto.request.ServeFinishedReqDTO;
//import com.jzo2o.orders.manager.model.dto.request.ServeStartReqDTO;
//import com.jzo2o.orders.manager.model.dto.response.PlaceOrderResDTO;
//import com.jzo2o.orders.manager.service.IOrdersCreateService;
//import com.jzo2o.orders.manager.service.IOrdersManagerService;
//import com.jzo2o.orders.manager.service.IOrdersServeManagerService;
//import com.jzo2o.orders.manager.service.SimulateService;
//import com.jzo2o.redis.annotations.Lock;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//public class SimulateServiceImpl implements SimulateService {
//
//    @Resource
//    private IOrdersCreateService ordersCreateService;
//    @Resource
//    private IOrdersManagerService ordersManagerService;
//
//    @Resource
//    private IOrdersServeManagerService ordersServeManagerService;
//
//    @Resource
//    private OrdersMapper ordersMapper;
//
//    @Resource
//    private InstitutionStaffApi institutionStaffApi;
//
//    @Resource
//    private ServeApi serveApi;
//
//    @Resource
//    private ServeItemApi serveItemApi;
//
//    @Resource
//    private CommonUserApi commonUserApi;
//
//    @Resource
//    private AddressBookApi addressBookApi;
//
//    @Resource
//    private RegionApi regionApi;
//
//    @Resource
//    private EvaluationApi evaluationApi;
//
//    @Resource
//    private OrderStateMachine orderStateMachine;
//
//
//    @Override
//    public void simulateFinished() {
//
//        // 获取用户 (只模拟北京的订单)
//        Orders historyOrders = getRandomHistoryOrders("010");
//        if(historyOrders == null) {
//            return;
//        }
//
//
//        try {
//            // 用户信息
//            UserContext.set(getUserInfo(historyOrders.getUserId()));
//
//
//            List<AddressBookResDTO> addressBookResDTOList = addressBookApi.getByUserIdAndCity(historyOrders.getUserId(), "北京市");
//
//            if (CollUtils.isEmpty(addressBookResDTOList)) {
//                return;
//            }
//            // 下单
//            Long ordersId = placeOrder(historyOrders.getServeId(), addressBookResDTOList.get(0).getId(), DateUtils.now().plusHours(1));
//            // 支付
//            pay(ordersId);
//            // 等待派单
//            waitDispatch(ordersId);
//            OrdersServe ordersServe = ordersServeManagerService.queryById(ordersId);
//            // 人员分配
//            allocation(ordersServe);
//
//            // 开始服务
//            ServeStartReqDTO serveStartReqDTO = new ServeStartReqDTO();
//            serveStartReqDTO.setId(ordersServe.getId());
//            serveStartReqDTO.setServeBeforeImgs(Arrays.asList("https://yjy-xzbjzfw-oss.oss-cn-hangzhou.aliyuncs.com/dbccb449-110a-4f10-b050-ab39413d71f3.jpeg"));
//            serveStartReqDTO.setServeBeforeIllustrate("开始服务了");
//            ordersServeManagerService.serveStart(serveStartReqDTO, ordersServe.getServeProviderId());
//
//            // 完成服务
//            ServeFinishedReqDTO serveFinishedReqDTO = new ServeFinishedReqDTO();
//            serveFinishedReqDTO.setId(ordersServe.getId());
//            serveFinishedReqDTO.setServeAfterImgs(Arrays.asList("https://yjy-xzbjzfw-oss.oss-cn-hangzhou.aliyuncs.com/5038d57d-45e1-4741-8d5a-39aaee92cf2e.jpg"));
//            serveFinishedReqDTO.setServeAfterIllustrate("服务完成了");
//            ordersServeManagerService.serveFinished(serveFinishedReqDTO, ordersServe.getServeProviderId(), ordersServe.getServeProviderType());
//
//            // 完成评价
////            this.evaluate(ordersId);
//            ordersManagerService.evaluationOrder(ordersId);
//
//
//        }catch (Exception e) {
//            log.error("模拟单据异常，e:",e);
//        }finally {
//            UserContext.clear();
//        }
//    }
//
//
//    /**
//     * 下单方法
//     *
//     * @param serveId
//     * @param addressBookId
//     * @param serveStartTime
//     * @return
//     */
//    private Long placeOrder(Long serveId, Long addressBookId, LocalDateTime serveStartTime) {
//        // 下单
//        PlaceOrderReqDTO placeOrderReqDTO = new PlaceOrderReqDTO();
//        placeOrderReqDTO.setAddressBookId(addressBookId);
//        placeOrderReqDTO.setServeId(serveId);
//        placeOrderReqDTO.setServeStartTime(serveStartTime);
//        placeOrderReqDTO.setPurNum(1);
//        PlaceOrderResDTO placeOrderResDTO = ordersCreateService.placeOrder(placeOrderReqDTO);
//        return ObjectUtils.get(placeOrderResDTO, PlaceOrderResDTO::getId);
//    }
//
//    /**
//     * 模拟支付
//     * @param id
//     */
//    private void pay(Long id) {
//        Orders orders = ordersMapper.selectById(id);
//        // 待支付
//        if(OrderStatusEnum.NO_PAY.getStatus().equals(orders.getOrdersStatus())) {
//            TradeStatusMsg msg = TradeStatusMsg.builder()
//                    .productOrderNo(orders.getId())
//                    .tradingChannel("WECHAT_PAY")
//                    .statusCode(TradingStateEnum.YJS.getCode())
//                    .tradingOrderNo(IdUtil.getSnowflakeNextId())
//                    .transactionId(IdUtils.getSnowflakeNextIdStr())
//                    .build();
//            ordersCreateService.paySuccess(msg);
//        }
//
//    }
//
//    /**
//     * 等待派单
//     *
//     * @param id
//     */
//    private void waitDispatch(Long id) {
//
//        // 等待派单
//        while (ObjectUtils.get(ordersMapper.selectById(id), Orders::getOrdersStatus).equals(OrderStatusEnum.DISPATCHING.getStatus())) {
//            log.debug("等待派单");
//            ThreadUtil.sleep(3, TimeUnit.SECONDS);
//        }
//    }
//
//    // 人员分配
//    private void allocation(OrdersServe ordersServe) {
//        Long institutionStaffId = null;
//        if (ordersServe.getServeProviderType() == UserType.INSTITUTION) {
//
//            List<InstitutionStaffResDTO> institutionStaffs = institutionStaffApi.findByInstitutionId(ordersServe.getServeProviderId());
//            if(CollUtils.isEmpty(institutionStaffs)) {
//                // 当前机构没有服务人员，添加服务人员
//                InstitutionStaffAddReqDTO institutionStaffAddReqDTO = new InstitutionStaffAddReqDTO();
//                institutionStaffAddReqDTO.setId(IdUtils.getSnowflakeNextId());
//                institutionStaffAddReqDTO.setInstitutionId(ordersServe.getServeProviderId());
//                institutionStaffAddReqDTO.setName("模拟" + IdUtils.getSnowflakeNextIdStr().substring(10));
//                institutionStaffAddReqDTO.setPhone(IdUtils.getSnowflakeNextIdStr().substring(0, 11));
//                institutionStaffAddReqDTO.setIdCardNo(IdUtils.getSnowflakeNextIdStr().substring(0,18));
//                institutionStaffAddReqDTO.setCertificationImgs(Arrays.asList("https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132"));
//                institutionStaffApi.add(institutionStaffAddReqDTO);
//                institutionStaffId = institutionStaffAddReqDTO.getId();
//            }else {
//                institutionStaffId = institutionStaffs.get(0).getId();
//            }
//
//            ordersServeManagerService.allocation(ordersServe.getId(), ordersServe.getServeProviderId(), institutionStaffId);
//        }
//    }
//
//    /**
//     * 随机获取一个过往订单，通过该订单的用户，服务，下单地址进行下单
//     * @return
//     */
//    private Orders getRandomHistoryOrders(String cityCode) {
//        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper
//                .eq(Orders::getOrdersStatus, OrderStatusEnum.FINISHED.getStatus())
//                .eq(Orders::getCityCode, cityCode)
//                .last("limit 100");
//        List<Orders> orders = ordersMapper.selectList(lambdaQueryWrapper);
//        if(CollUtils.isEmpty(orders)) {
//            throw new CommonException("未查到订单");
//        }
//        return orders.get((int)(Math.random() * orders.size()));
//    }
//
//    private CurrentUserInfo getUserInfo(Long userId) {
//        CommonUserResDTO commonUserResDTO = commonUserApi.findById(userId);
//        return new CurrentUserInfo(userId, commonUserResDTO.getNickname(), commonUserResDTO.getAvatar(), UserType.C_USER);
//    }
//
////    private void evaluate(Long ordersId){
////
////        // 获取订单信息
////        Orders orders = ordersMapper.selectById(ordersId);
////        //发起评价
////        EvaluationSubmitReqDTO evaluationSubmitReqDTO = new EvaluationSubmitReqDTO();
////        evaluationSubmitReqDTO.setUserType(UserType.C_USER);
////        evaluationSubmitReqDTO.setUserId(orders.getUserId());
////        evaluationSubmitReqDTO.setOrdersId(orders.getId());
////        evaluationApi.autoEvaluate(evaluationSubmitReqDTO);
////
////
////        //订单状态变更
////        orderStateMachine.changeStatus(orders.getUserId(), orders.getId().toString(), OrderStatusChangeEventEnum.EVALUATE);
////    }
//}
