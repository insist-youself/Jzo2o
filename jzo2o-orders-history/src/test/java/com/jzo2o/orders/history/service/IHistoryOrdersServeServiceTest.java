package com.jzo2o.orders.history.service;

import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.DateUtils;
import com.jzo2o.common.utils.IdUtils;
import com.jzo2o.common.utils.JsonUtils;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.orders.history.model.domain.HistoryOrders;
import com.jzo2o.orders.history.model.domain.HistoryOrdersServe;
import com.jzo2o.orders.history.model.dto.request.HistoryOrdersServeListQueryReqDTO;
import com.jzo2o.orders.history.model.dto.request.HistoryOrdersServePageQueryReqDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersServeDetailResDTO;
import com.jzo2o.orders.history.model.dto.response.HistoryOrdersServeResDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
public class IHistoryOrdersServeServiceTest {
    
    @Resource
    private IHistoryOrdersServeService historyOrdersServeService;

    @BeforeEach
    void setUp() {
        CurrentUserInfo currentUserInfo = new CurrentUserInfo(1694955099182362626L, null, null, null);

        UserContext.set(currentUserInfo);
    }

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Test
    public void test() {
        int num = 10;
        List<HistoryOrdersServe> historyOrdersServes = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            HistoryOrdersServe historyOrdersServe = new HistoryOrdersServe();
            historyOrdersServe.setId(IdUtils.getSnowflakeNextId());
            historyOrdersServe.setContactsPhone("18810966207");
            historyOrdersServe.setContactsName("张三");
            historyOrdersServe.setServeTypeId(100L);
            historyOrdersServe.setServeTypeName("家政服务");
            historyOrdersServe.setServeItemId(1L);
            historyOrdersServe.setServeItemName("打扫卫生");
            historyOrdersServe.setServeProviderId(10000L);
            historyOrdersServe.setServeProviderType(3);
            historyOrdersServe.setCityCode("010");
            historyOrdersServe.setServeItemImg("https://yjy-xzbjzfw-oss.oss-cn-hangzhou.aliyuncs.com/aa6489e5-cd92-42f0-837a-952c99653b8b.png");
            historyOrdersServe.setServeAddress("北京北京市昌平区金燕龙黑马程序员");
            historyOrdersServe.setServeProviderStaffName("张三");
            historyOrdersServe.setServeProviderStaffPhone("18810966207");
            historyOrdersServe.setServeStartTime(DateUtils.now().plusHours(5));
            historyOrdersServe.setRealServeStartTime(DateUtils.now().minusDays(10));
            historyOrdersServe.setRealServeEndTime(DateUtils.now());
            historyOrdersServe.setServeBeforeIllustrate("开始服务");
            historyOrdersServe.setServeBeforeImgs(Arrays.asList("https://yjy-xzbjzfw-oss.oss-cn-hangzhou.aliyuncs.com/aa6489e5-cd92-42f0-837a-952c99653b8b.png"));
            historyOrdersServe.setServeAfterImgs(Arrays.asList("https://yjy-xzbjzfw-oss.oss-cn-hangzhou.aliyuncs.com/aa6489e5-cd92-42f0-837a-952c99653b8b.png"));
            historyOrdersServe.setServeAfterIllustrate("结束服务");
            historyOrdersServe.setSortTime(DateUtils.now().minusDays(400));
            historyOrdersServe.setOrdersOriginType(1);
            historyOrdersServe.setServeStatus(3);
            historyOrdersServes.add(historyOrdersServe);
        }
        historyOrdersServeService.saveBatch(historyOrdersServes);

    }

    @Test
    public void testQueryForList() {
        HistoryOrdersServeListQueryReqDTO historyOrdersServeListQueryReqDTO = new HistoryOrdersServeListQueryReqDTO();
        historyOrdersServeListQueryReqDTO.setMinSortTime(DateUtils.parse("2022-01-01", "yyyy-MM-dd"));
        historyOrdersServeListQueryReqDTO.setMaxSortTime(DateUtils.parse("2022-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        List<HistoryOrdersServeResDTO> historyOrdersServeResDTOS = historyOrdersServeService.queryForList(historyOrdersServeListQueryReqDTO);
        log.info("historyOrdersServeResDTOS: {}", JsonUtils.toJsonStr(historyOrdersServeResDTOS));
    }
    @Test
    public void testQueryDetailById() {
        HistoryOrdersServeDetailResDTO historyOrdersServeDetailResDTO = historyOrdersServeService.queryDetailById(1702525633861083136L);
        log.info("historyOrdersServeDetailResDTO : {}", historyOrdersServeDetailResDTO);
    }

    @Test
    public void testQueryForPage() {
        HistoryOrdersServePageQueryReqDTO historyOrdersServeListQueryReqDTO = new HistoryOrdersServePageQueryReqDTO();
        historyOrdersServeListQueryReqDTO.setMinSortTime(DateUtils.parse("2022-01-01", "yyyy-MM-dd"));
        historyOrdersServeListQueryReqDTO.setMaxSortTime(DateUtils.parse("2022-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        historyOrdersServeListQueryReqDTO.setPageNo(1L);
        historyOrdersServeListQueryReqDTO.setPageSize(10L);
        PageResult<HistoryOrdersServeResDTO> historyOrdersServeResDTOS = historyOrdersServeService.queryForPage(historyOrdersServeListQueryReqDTO);
        log.info("historyOrdersServeResDTOS: {}", JsonUtils.toJsonStr(historyOrdersServeResDTOS));
    }
}
