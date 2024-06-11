//package com.jzo2o.market.service;
//
//import com.jzo2o.api.market.dto.request.CouponUseBackReqDTO;
//import com.jzo2o.api.market.dto.request.CouponUseReqDTO;
//import com.jzo2o.api.market.dto.response.AvailableCouponsResDTO;
//import com.jzo2o.api.market.dto.response.CouponUseResDTO;
//import com.jzo2o.common.constants.UserType;
//import com.jzo2o.common.model.CurrentUserInfo;
//import com.jzo2o.common.model.PageResult;
//import com.jzo2o.common.utils.JsonUtils;
//import com.jzo2o.market.model.dto.request.CouponOperationPageQueryReqDTO;
//import com.jzo2o.market.model.dto.request.SeizeCouponReqDTO;
//import com.jzo2o.market.model.dto.response.CouponInfoResDTO;
//import com.jzo2o.mvc.utils.UserContext;
//import com.jzo2o.redis.utils.RedisSyncQueueUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.script.DefaultRedisScript;
//
//import javax.annotation.Resource;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.List;
//
//import static com.jzo2o.market.constants.RedisConstants.RedisKey.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Slf4j
//public class ICouponServiceTest {
//
//    @Resource
//    private ICouponService couponService;
//
//    @Resource(name = "redisTemplate")
//    RedisTemplate redisTemplate;
//
//    @Resource(name = "seizeCouponScript")
//    private DefaultRedisScript<String> seizeCouponScript;
//
//    @BeforeEach
//    void setUp() {
//        CurrentUserInfo currentUserInfo = new CurrentUserInfo(1695339358949949440L, null, null, UserType.C_USER);
//        UserContext.set(currentUserInfo);
//    }
//
//    @AfterEach
//    void tearDown() {
//        UserContext.clear();
//    }
//
//    @Test
//    void queryForPageOfOperation() {
//        CouponOperationPageQueryReqDTO couponOperationPageQueryReqDTO = new CouponOperationPageQueryReqDTO();
//        couponOperationPageQueryReqDTO.setActivityId(1703941732871819264L);
//        PageResult<CouponInfoResDTO> couponInfoResDTOPageResult = couponService.queryForPageOfOperation(couponOperationPageQueryReqDTO);
//        log.info("couponInfoResDTOPageResult:{}", JsonUtils.toJsonStr(couponInfoResDTOPageResult));
//    }
//
//    @Test
//    void queryForList() {
//        List<CouponInfoResDTO> couponInfoResDTOS = couponService.queryForList(null, UserContext.currentUserId(), 1);
//        log.info("couponInfoResDTOS : {}", couponInfoResDTOS);
//    }
//
//    @Test
//    void seizeCoupon() {
//        SeizeCouponReqDTO seizeCouponReqDTO = new SeizeCouponReqDTO();
//        seizeCouponReqDTO.setId(1705048030539472896L);
//        couponService.seizeCoupon(seizeCouponReqDTO);
//    }
//
//    @Test
//    void test_seizeCouponScriptLua() {
//
//        //argv：抢券活动id
//        long activityId = 1706183021040336896L;
//        // argv: 用户id
//        Long userId = 1694250327664218113L;
//        int index = (int) (activityId % 10);
//        //key: 抢券同步队列，资源库存,抢券成功列表
//        // 同步队列redisKey
//        String couponSeizeSyncRedisKey = RedisSyncQueueUtils.getQueueRedisKey(COUPON_SEIZE_SYNC_QUEUE_NAME, index);
//        // 资源库存redisKey
//        String resourceStockRedisKey = String.format(COUPON_RESOURCE_STOCK, index);
//        // 抢券成功列表
//        String couponSeizeListRedisKey = String.format(COUPON_SEIZE_LIST,activityId, index);
//        // 抢券
//        Object execute = redisTemplate.execute(seizeCouponScript, Arrays.asList(couponSeizeSyncRedisKey, resourceStockRedisKey, couponSeizeListRedisKey),
//                activityId, userId);
//        log.debug("seize coupon result : {}", execute);
//    }
//
//    @Test
//    void getAvailable() {
//        List<AvailableCouponsResDTO> available = couponService.getAvailable(new BigDecimal(100));
//        log.info("available:{}", JsonUtils.toJsonStr(available));
//    }
//
//    @Test
//    void use() {
//        CouponUseReqDTO couponUseReqDTO = new CouponUseReqDTO();
//        couponUseReqDTO.setId(1703980760572669952L);
//        couponUseReqDTO.setOrdersId(123456L);
//        couponUseReqDTO.setTotalAmount(new BigDecimal(100));
//        CouponUseResDTO use = couponService.use(couponUseReqDTO);
//        log.info("use : {}", use);
//    }
//
//    @Test
//    void useBack() {
//        CouponUseBackReqDTO couponUseBackReqDTO = new CouponUseBackReqDTO();
//        couponUseBackReqDTO.setId(1703980760572669952L);
//        couponUseBackReqDTO.setOrdersId(123456L);
//        couponService.useBack(couponUseBackReqDTO);
//    }
//}