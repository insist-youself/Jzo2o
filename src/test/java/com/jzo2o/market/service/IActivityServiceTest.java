//package com.jzo2o.market.service;
//
//import com.jzo2o.common.model.PageResult;
//import com.jzo2o.common.utils.DateUtils;
//import com.jzo2o.common.utils.JsonUtils;
//import com.jzo2o.market.constants.TabTypeConstants;
//import com.jzo2o.market.enums.ActivityStatusEnum;
//import com.jzo2o.market.enums.ActivityTypeEnum;
//import com.jzo2o.market.model.dto.request.ActivityQueryForPageReqDTO;
//import com.jzo2o.market.model.dto.request.ActivitySaveReqDTO;
//import com.jzo2o.market.model.dto.response.ActivityInfoResDTO;
//import com.jzo2o.market.model.dto.response.SeizeCouponInfoResDTO;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Slf4j
//public class IActivityServiceTest {
//
//    @Resource
//    private IActivityService activityService;
//
//    @BeforeEach
//    void setUp() {
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void queryForPage() {
//        ActivityQueryForPageReqDTO activityQueryForPageReqDTO = new ActivityQueryForPageReqDTO();
//        activityQueryForPageReqDTO.setPageNo(1L);
//        activityQueryForPageReqDTO.setPageSize(10L);
//        activityQueryForPageReqDTO.setStatus(ActivityStatusEnum.NO_DISTRIBUTE.getStatus());
//        PageResult<ActivityInfoResDTO> activityInfoResDTOPageResult = activityService.queryForPage(activityQueryForPageReqDTO);
//        log.info("activityInfoResDTOPageResult:{}", JsonUtils.toJsonStr(activityInfoResDTOPageResult));
//    }
//
//    @Test
//    void queryById() {
//
//        ActivityInfoResDTO activityInfoResDTO = activityService.queryById(1703941732871819264L);
//        log.info("activityInfoResDTO:{}", JsonUtils.toJsonStr(activityInfoResDTO));
//    }
//
//    @Test
//    void save() {
//        ActivitySaveReqDTO activitySaveReqDTO = new ActivitySaveReqDTO();
//        activitySaveReqDTO.setName("国庆优惠券1");
//        activitySaveReqDTO.setAmountCondition(new BigDecimal(100));
//        activitySaveReqDTO.setDiscountAmount(new BigDecimal(50));
//        activitySaveReqDTO.setType(ActivityTypeEnum.AMOUNT_DISCOUNT.getType());
//        activitySaveReqDTO.setDistributeStartTime(DateUtils.parse("2023-09-22", "yyyy-MM-dd"));
//        activitySaveReqDTO.setDistributeEndTime(DateUtils.parse("2023-10-19 15:34:00", "yyyy-MM-dd HH:mm:ss"));
//        activitySaveReqDTO.setValidityDays(7);
//        activitySaveReqDTO.setTotalNum(1000);
//        activityService.save(activitySaveReqDTO);
//
//    }
//
//    @Test
//    void queryForList() {
//
////        List<SeizeCouponInfoResDTO> seizeCouponInfoResDTOS = activityService.queryForListFromCache(TabTypeConstants.SEIZING);
////        log.info("seizeCouponInfoResDTOS:{}", seizeCouponInfoResDTOS);
//    }
//
//    @Test
//    void getActivityInfoByIdFromCache() {
//        ActivityInfoResDTO activityInfoByIdFromCache = activityService.getActivityInfoByIdFromCache(1703766630830026752L);
//        log.info("activityInfoByIdFromCache:{}", activityInfoByIdFromCache);
//
//    }
//
//    @Test
//    void preHeat() {
//        activityService.preHeat();
//    }
//
//    @Test
//    void activityFinished() {
//        activityService.activityFinished();
//    }
//
//    @Test
//    void updateStatus() {
//        activityService.updateStatus();
//    }
//
//    @Test
//    void revoke() {
//        activityService.revoke(1703941732871819264L);
//    }
//}