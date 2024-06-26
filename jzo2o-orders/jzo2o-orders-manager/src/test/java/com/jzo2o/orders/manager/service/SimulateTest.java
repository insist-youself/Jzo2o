//package com.jzo2o.orders.manager.service;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.thread.ThreadUtil;
//import com.jzo2o.api.customer.InstitutionStaffApi;
//import com.jzo2o.api.customer.ServeProviderApi;
//import com.jzo2o.api.customer.dto.request.InstitutionStaffAddReqDTO;
//import com.jzo2o.api.customer.dto.response.InstitutionStaffResDTO;
//import com.jzo2o.api.orders.dto.request.OrderCancelReqDTO;
//import com.jzo2o.common.constants.UserType;
//import com.jzo2o.common.model.CurrentUserInfo;
//import com.jzo2o.common.utils.CollUtils;
//import com.jzo2o.common.utils.DateUtils;
//import com.jzo2o.common.utils.IdUtils;
//import com.jzo2o.common.utils.ObjectUtils;
//import com.jzo2o.mvc.utils.UserContext;
//import com.jzo2o.orders.base.config.OrderStateMachine;
//import com.jzo2o.orders.base.enums.OrderStatusEnum;
//import com.jzo2o.orders.base.mapper.OrdersMapper;
//import com.jzo2o.orders.base.mapper.OrdersServeMapper;
//import com.jzo2o.orders.base.model.domain.Orders;
//import com.jzo2o.orders.base.model.domain.OrdersServe;
//import com.jzo2o.orders.manager.handler.OrdersHandler;
//import com.jzo2o.orders.manager.model.dto.OrderCancelDTO;
//import com.jzo2o.orders.manager.model.dto.request.PlaceOrderReqDTO;
//import com.jzo2o.orders.manager.model.dto.request.ServeFinishedReqDTO;
//import com.jzo2o.orders.manager.model.dto.request.ServeStartReqDTO;
//import com.jzo2o.orders.manager.model.dto.response.PlaceOrderResDTO;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import javax.annotation.Resource;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//@SpringBootTest
//public class SimulateTest {
//    @Resource
//    private IOrdersCreateService ordersCreateService;
//    @Resource
//    private IOrdersManagerService ordersManagerService;
//    @Resource
//    private OrderStateMachine orderStateMachine;
//
//    @Resource
//    private IOrdersServeManagerService ordersServeManagerService;
//    @Resource
//    private OrdersMapper ordersMapper;
//
//    @Resource
//    private OrdersServeMapper ordersServeMapper;
//    @Resource
//    private InstitutionStaffApi institutionStaffApi;
//
//    @Resource
//    private ServeProviderApi serveProviderApi;
//
//    @Resource
//    private SimulateService simulateService;
//
//    @Resource
//    private OrdersHandler ordersHandler;
//
//    @BeforeEach
//    void setUp() {
//        CurrentUserInfo currentUserInfo = new CurrentUserInfo(1695339358949949440L, "张三", null, UserType.C_USER);
//        UserContext.set(currentUserInfo);
//    }
//
//    @AfterEach
//    void tearDown() {
//        UserContext.clear();
//    }
//
//    @Test
//    public void testSimulateFinished() {
//        int number = 50;
//        LocalDateTime localDateTime = DateUtils.now().plusHours(1);
//
//        ordersHandler.simulateFinished("2");
////        for (int count = 0; count < number; count++) {
////            simulateFinished(1693815624114970626L, 1695353353577459714L, localDateTime.withMinute(0).withSecond(0));
////            simulateService.simulateFinished();
//
////        }
//
//    }
//
//    @Test
//    public void testSimulateCancel() {
//        int number = 1;
//        LocalDateTime localDateTime = DateUtils.now().plusHours(4);
//
//        for (int count = 0; count < number; count++) {
//            log.info("----------------------------------------");
//            simulateCancel(1693815624114970626L, 1695353353577459714L, localDateTime.withMinute(0).withSecond(0));
//        }
//
//    }
//
//    /**
//     * 模拟交易开始到交易完结
//     *
//     * @param serveId
//     * @param addressBookId
//     * @param serveStartTime
//     */
//    private void simulateFinished(Long serveId, Long addressBookId, LocalDateTime serveStartTime) {
//
//        // 下单
//        PlaceOrderReqDTO placeOrderReqDTO = new PlaceOrderReqDTO();
//        placeOrderReqDTO.setAddressBookId(addressBookId);
//        placeOrderReqDTO.setServeId(serveId);
//        placeOrderReqDTO.setServeStartTime(serveStartTime);
//        placeOrderReqDTO.setPurNum(1);
//        PlaceOrderResDTO placeOrderResDTO = ordersCreateService.placeOrder(placeOrderReqDTO);
//
//        // 等待派单
//        while (ObjectUtils.get(ordersMapper.selectById(placeOrderResDTO.getId()), Orders::getOrdersStatus).equals(OrderStatusEnum.DISPATCHING.getStatus())) {
//            log.debug("等待派单");
//            ThreadUtil.sleep(3, TimeUnit.SECONDS);
//        }
//
//        OrdersServe ordersServe = ordersServeMapper.selectById(placeOrderResDTO.getId());
//        // 机构接单 人员分配
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
//
//        // 开始服务
//        ServeStartReqDTO serveStartReqDTO = new ServeStartReqDTO();
//        serveStartReqDTO.setId(ordersServe.getId());
//        serveStartReqDTO.setServeBeforeImgs(Arrays.asList("https://yjy-xzbjzfw-oss.oss-cn-hangzhou.aliyuncs.com/0f2653d7-1d61-4014-9fc8-0c440f562eac.png", "https://yjy-xzbjzfw-oss.oss-cn-hangzhou.aliyuncs.com/0f2653d7-1d61-4014-9fc8-0c440f562eac.png"));
//        serveStartReqDTO.setServeBeforeIllustrate("开始服务了" + DateUtils.now());
//        ordersServeManagerService.serveStart(serveStartReqDTO, ordersServe.getServeProviderId());
//
//        // 完成服务
//        ServeFinishedReqDTO serveFinishedReqDTO = new ServeFinishedReqDTO();
//        serveFinishedReqDTO.setId(ordersServe.getId());
//        serveFinishedReqDTO.setServeAfterImgs(serveStartReqDTO.getServeBeforeImgs());
//        serveFinishedReqDTO.setServeAfterIllustrate("服务完成了" + DateUtils.now());
//        ordersServeManagerService.serveFinished(serveFinishedReqDTO, ordersServe.getServeProviderId(), ordersServe.getServeProviderType());
//
//        // 完成评价
//        ordersManagerService.evaluationOrder(placeOrderResDTO.getId());
//    }
//
//    /**
//     * 模拟交易开始到交易完结
//     *
//     * @param serveId
//     * @param addressBookId
//     * @param serveStartTime
//     */
//    private void simulateCancel(Long serveId, Long addressBookId, LocalDateTime serveStartTime) {
//
//        // 下单
//        PlaceOrderReqDTO placeOrderReqDTO = new PlaceOrderReqDTO();
//        placeOrderReqDTO.setAddressBookId(addressBookId);
//        placeOrderReqDTO.setServeId(serveId);
//        placeOrderReqDTO.setServeStartTime(serveStartTime);
//        placeOrderReqDTO.setPurNum(1);
//        PlaceOrderResDTO placeOrderResDTO = ordersCreateService.placeOrder(placeOrderReqDTO);
//
//        OrderCancelReqDTO orderCancelReqDTO = new OrderCancelReqDTO();
//        orderCancelReqDTO.setId(placeOrderResDTO.getId());
//        orderCancelReqDTO.setCancelReason("模拟取消");
//        OrderCancelDTO orderCancelDTO = BeanUtil.toBean(orderCancelReqDTO, OrderCancelDTO.class);
//        CurrentUserInfo currentUserInfo = UserContext.currentUser();
//        orderCancelDTO.setCurrentUserId(currentUserInfo.getId());
//        orderCancelDTO.setCurrentUserName(currentUserInfo.getName());
//        orderCancelDTO.setCurrentUserType(currentUserInfo.getUserType());
//        ordersManagerService.cancel(orderCancelDTO);
//    }
//}
