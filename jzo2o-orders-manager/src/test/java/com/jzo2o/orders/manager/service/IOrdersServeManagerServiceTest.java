//package com.jzo2o.orders.manager.service;
//
//import com.jzo2o.common.constants.UserType;
//import com.jzo2o.common.model.CurrentUserInfo;
//import com.jzo2o.common.model.PageResult;
//import com.jzo2o.common.utils.IdUtils;
//import com.jzo2o.common.utils.JsonUtils;
//import com.jzo2o.mvc.utils.UserContext;
//import com.jzo2o.orders.manager.model.dto.request.OrderServeCancelReqDTO;
//import com.jzo2o.orders.manager.model.dto.request.OrdersServePageQueryReqDTO;
//import com.jzo2o.orders.manager.model.dto.request.ServeFinishedReqDTO;
//import com.jzo2o.orders.manager.model.dto.request.ServeStartReqDTO;
//import com.jzo2o.orders.manager.model.dto.response.OrdersServeDetailResDTO;
//import com.jzo2o.orders.manager.model.dto.response.OrdersServeResDTO;
//import com.jzo2o.statemachine.mapper.BizSnapshotMapper;
//import com.jzo2o.statemachine.model.BizSnapshot;
//import com.jzo2o.statemachine.snapshot.BizSnapshotService;
//import io.seata.spring.annotation.GlobalTransactional;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@SpringBootTest
//@Slf4j
//public class IOrdersServeManagerServiceTest {
//
//    @Resource
//    private IOrdersServeManagerService ordersServeManagerService;
//
//    @Resource
//    private BizSnapshotMapper bizSnapshotMapper;
//
//    @BeforeEach
//    void setUp() {
//        CurrentUserInfo currentUserInfo = new CurrentUserInfo(1696338624494202882L, null, null, UserType.WORKER);
//
//        UserContext.set(currentUserInfo);
//    }
//
//    @AfterEach
//    void tearDown() {
//        UserContext.clear();
//    }
//
//    @Test
//    public void testPage() {
//        PageResult<OrdersServeResDTO> ordersServeResDTOPageResult = ordersServeManagerService.queryForPage(1694955099182362626L, 3, new OrdersServePageQueryReqDTO());
//        log.info("ordersServeResDTOPageResult :{}", ordersServeResDTOPageResult);
//    }
//
//    @Test
//    public void testQueryForList() {
//        List<OrdersServeResDTO> ordersServeResDTOS = ordersServeManagerService.queryForList(1695628302246506498L, null, 5L);
//        log.info("ordersServeResDTOS : {}", ordersServeResDTOS);
//    }
//    @Test
//    public void getDetail() {
//        OrdersServeDetailResDTO detail = ordersServeManagerService.getDetail(1688071322352574464L, 1682263143260631042L);
//        log.info("detail:{}", detail);
//    }
//
//    @Test
//    public void allocation() {
//        ordersServeManagerService.allocation(1689083543919075328L, 1682263143260631042L, 1684880730398269441L);
//    }
//
//    @Test
//    public void serveStart() {
//        ServeStartReqDTO serveStartReqDTO = new ServeStartReqDTO();
//        serveStartReqDTO.setId(2309050000000000265L);
//        serveStartReqDTO.setServeBeforeIllustrate("开始服务");
//         ordersServeManagerService.serveStart(serveStartReqDTO, 1695628302246506498L);
//    }
//
//    @Test
//    void serveFinished() {
//        ServeFinishedReqDTO serveFinishedReqDTO = new ServeFinishedReqDTO();
//        serveFinishedReqDTO.setId(2309050000000000265L);
//        serveFinishedReqDTO.setServeAfterIllustrate("结束服务");
//
//        ordersServeManagerService.serveFinished(serveFinishedReqDTO, 1695628302246506498L,3);
//    }
//
//    @Test
//    void deleteServe() {
//        ordersServeManagerService.deleteServe(1688825190651269120L, 1682263143260631042L, 3);
//    }
//
//
//    @Test
//    public void testServeTimeout() {
////        ordersServeManagerService.serveTimeout();
//    }
//}