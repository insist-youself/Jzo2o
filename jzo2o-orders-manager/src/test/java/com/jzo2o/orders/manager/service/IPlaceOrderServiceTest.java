//package com.jzo2o.orders.manager.service;
//
//import com.jzo2o.api.market.CouponApi;
//import com.jzo2o.api.market.dto.response.AvailableCouponsResDTO;
//import com.jzo2o.common.constants.UserType;
//import com.jzo2o.common.model.CurrentUserInfo;
//import com.jzo2o.common.utils.Base64Utils;
//import com.jzo2o.common.utils.JsonUtils;
//import com.jzo2o.mvc.constants.HeaderConstants;
//import com.jzo2o.mvc.utils.ResponseUtils;
//import com.jzo2o.mvc.utils.UserContext;
//import com.jzo2o.orders.manager.model.dto.request.PlaceOrderReqDTO;
//import com.jzo2o.orders.manager.model.dto.response.PlaceOrderResDTO;
//import feign.FeignException;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//@Slf4j
//public class IPlaceOrderServiceTest {
//
//    @Resource
//    private IOrdersCreateService ordersCreateService;
//
//    @Resource
//    private CouponApi couponApi;
//
//    private static List<LocalDateTime> localDateTimes = new ArrayList<>();
//
//    @BeforeAll
//    public static void setUp() throws Exception {
//        CurrentUserInfo currentUserInfo = new CurrentUserInfo(1695339358949949440L, null, null, UserType.C_USER);
//        UserContext.set(currentUserInfo);
//        LocalDateTime localDateTime = LocalDateTime.of(2023, 9, 7, 18, 30, 0);
//        for (int count = 0; count < 2; count++) {
//            localDateTimes.add(localDateTime.plusMinutes(30 * count));
//        }
//
//    }
//
//    @AfterAll
//    public static void tearDown() throws Exception {
//        UserContext.clear();
//    }
//
//    @Test
//    public void getAvailableCoupons() {
//        List<AvailableCouponsResDTO> availableCoupons = couponApi.getAvailable(new BigDecimal(100));
////        List<AvailableCouponsResDTO> availableCoupons = ordersManagerService.getAvailableCoupons(1693815624114970626L, 1);
//        log.info("availableCoupons : {}", availableCoupons);
//    }
//    @Test
//    public void placeOrder() {
//
//        for (int count = 0; count < 1; count++) {
//
//            PlaceOrderReqDTO placeOrderReqDTO = new PlaceOrderReqDTO();
//            placeOrderReqDTO.setServeStartTime(localDateTimes.get((int) (Math.random() * localDateTimes.size())));
//            placeOrderReqDTO.setServeId(1693815624114970626L);
//            placeOrderReqDTO.setAddressBookId(1695353353577459714L);
//            placeOrderReqDTO.setPurNum(1);
//            placeOrderReqDTO.setCouponId(123L);
//
//            try {
//                PlaceOrderResDTO placeOrderResDTO = ordersCreateService.placeOrder(placeOrderReqDTO);
//            }catch (FeignException feignException) {
//                Object headerValue = feignException.responseHeaders().get(HeaderConstants.INNER_ERROR);
//                log.info("headerValue1 : {},e:", headerValue.toString(), feignException);
//            }catch (Exception e) {
//                String headerValue = ResponseUtils.getResponse().getHeader(HeaderConstants.INNER_ERROR);
//                log.info("headerValue2 : {},e:", headerValue, e);
//            }
//
//        }
//    }
//
//}