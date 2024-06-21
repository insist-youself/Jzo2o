package com.jzo2o.orders.history.service;

import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.common.utils.IdUtils;
import com.jzo2o.common.utils.JsonUtils;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.orders.history.model.domain.HistoryOrders;
import com.jzo2o.orders.history.model.dto.request.HistoryOrdersListQueryReqDTO;
import com.jzo2o.orders.history.model.dto.request.HistoryOrdersPageQueryReqDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersDetailResDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersListResDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersPageResDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@Slf4j
public class IHistoryOrdersServiceTest {

    @Resource
    private IHistoryOrdersService historyOrdersService;

    @BeforeEach
    public void setUp() {
        UserContext.set(new CurrentUserInfo(1695339358949949440L, null, null, 1));
    }

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Test
    public void testQueryForList() {
        HistoryOrdersListQueryReqDTO historyOrdersListQueryReqDTO = new HistoryOrdersListQueryReqDTO();
        historyOrdersListQueryReqDTO.setMinSortTime(DateUtils.parse("2022-01-01", "yyyy-MM-dd"));
        historyOrdersListQueryReqDTO.setMaxSortTime(DateUtils.parse("2022-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        List<HistoryOrdersListResDTO> historyOrdersListResDTOS = historyOrdersService.queryUserOrderForList(historyOrdersListQueryReqDTO);
        log.info("historyOrdersListResDTOS : {}", JsonUtils.toJsonStr(historyOrdersListResDTOS));
    }

    @Test
    public void testQueryForPage() {
        HistoryOrdersPageQueryReqDTO historyOrdersPageQueryReqDTO = new HistoryOrdersPageQueryReqDTO();
        historyOrdersPageQueryReqDTO.setMinSortTime(DateUtils.parse("2022-04-08", "yyyy-MM-dd"));
        historyOrdersPageQueryReqDTO.setMaxSortTime(DateUtils.parse("2022-10-08 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        PageResult<HistoryOrdersPageResDTO> historyOrdersPageResDTOPageResult = historyOrdersService.queryForPage(historyOrdersPageQueryReqDTO);
        log.info("historyOrdersListResDTOS : {}", JsonUtils.toJsonStr(historyOrdersPageResDTOPageResult));
    }

    @Test
    public void test() {
        // 已取消
//        HistoryOrdersDetailResDTO detail = historyOrdersService.getDetailById(1702258133214740493L);

        // 已完成
//        HistoryOrdersDetailResDTO detail = historyOrdersService.getDetailById(1702258133176991748L);
        // 已退款
        HistoryOrdersDetailResDTO detail = historyOrdersService.getDetailById(2309220000000001819L);
        log.info("detail : {}", JsonUtils.toJsonStr(detail));
    }

    @Test
    public void testGetDetailById() {
        HistoryOrdersDetailResDTO detail = historyOrdersService.getDetailById(2309230000000001827L);
        log.info("detail:{}", detail);
    }

    @Test
    public void testBatchAdd(){

        int total = 1000;
        List<HistoryOrders> historyOrders = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int count = 0; count < total; count++) {
            HistoryOrders historyOrder = new HistoryOrders();
            historyOrder.setId(IdUtils.getSnowflakeNextId());
            historyOrder.setContactsPhone("18810966207");
            historyOrder.setContactsName("张三");
            historyOrder.setUserId((long)(Math.random() * 50));
            historyOrder.setServeTypeId(100L);
            historyOrder.setServeTypeName("家政服务");
            historyOrder.setServeItemId(1L);
            historyOrder.setServeProviderId(null);
            historyOrder.setServeProviderType(null);
            historyOrder.setServeId(10000L);
            historyOrder.setCityCode("010");
            historyOrder.setServeItemImg("https://yjy-xzbjzfw-oss.oss-cn-hangzhou.aliyuncs.com/aa6489e5-cd92-42f0-837a-952c99653b8b.png");
            historyOrder.setUnit(1);
            historyOrder.setOrdersStatus(600);
            historyOrder.setPayStatus(null);
            historyOrder.setTradeFinishTime(null);
            historyOrder.setPrice(new BigDecimal(100));
            historyOrder.setTotalAmount(new BigDecimal(100));
            historyOrder.setRealPayAmount(new BigDecimal(100));
            historyOrder.setDiscountAmount(BigDecimal.ZERO);
            historyOrder.setServeAddress("北京北京市昌平区金燕龙黑马程序员");
            historyOrder.setServeProviderStaffName(null);
            historyOrder.setServeProviderStaffPhone(null);
            historyOrder.setInstitutionName(null);
            historyOrder.setPlaceOrderTime(DateUtils.now());
            historyOrder.setServeStartTime(DateUtils.now().plusHours(5));
            historyOrder.setRealServeStartTime(null);
            historyOrder.setRealServeEndTime(null);
            historyOrder.setServeBeforeIllustrate(null);
            historyOrder.setServeBeforeImgs(null);
            historyOrder.setServeAfterImgs(null);
            historyOrder.setServeAfterIllustrate(null);
            historyOrder.setPayTime(null);
            historyOrder.setCancelTime(DateUtils.now());
            historyOrder.setCancelReason("超时");
            historyOrders.add(historyOrder);
        }
        historyOrdersService.saveBatch(historyOrders, 100);
        long endTime = System.currentTimeMillis();
        log.info("data size : {}, startTime:{},endTime:{},totalTime:{}", total, startTime, endTime, endTime - startTime);
    }
}