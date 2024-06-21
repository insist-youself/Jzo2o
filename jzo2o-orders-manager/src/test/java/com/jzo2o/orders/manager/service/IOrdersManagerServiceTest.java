package com.jzo2o.orders.manager.service;

import com.jzo2o.api.orders.dto.response.OrderResDTO;
import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.model.dto.PageQueryDTO;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.HttpUtils;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.mysql.utils.PageHelperUtils;
import com.jzo2o.orders.base.config.OrderStateMachine;
import com.jzo2o.orders.base.enums.OrderStatusChangeEventEnum;
import com.jzo2o.orders.base.enums.OrderStatusEnum;
import com.jzo2o.orders.base.mapper.BreachRecordMapper;
import com.jzo2o.orders.base.mapper.OrdersMapper;
import com.jzo2o.orders.base.mapper.OrdersServeMapper;
import com.jzo2o.orders.base.model.domain.BreachRecord;
import com.jzo2o.orders.base.model.domain.Orders;
import com.jzo2o.orders.base.model.domain.OrdersServe;
import com.jzo2o.orders.manager.model.dto.OrderCancelDTO;
import com.jzo2o.orders.manager.model.dto.request.PlaceOrderReqDTO;
import com.jzo2o.orders.manager.model.dto.response.PlaceOrderResDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Slf4j
class IOrdersManagerServiceTest {

    @Resource
    private IOrdersManagerService ordersManagerService;
    @Resource
    private OrderStateMachine orderStateMachine;

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrdersServeMapper ordersServeMapper;



    @BeforeAll
    public static void setUp() {
        CurrentUserInfo currentUserInfo = new CurrentUserInfo(1695339358949949440L, null, null, UserType.OPERATION);
        UserContext.set(currentUserInfo);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        UserContext.clear();
    }

    @Test
    void cancel() {
        OrderCancelDTO orderCancelDTO = new OrderCancelDTO();
        orderCancelDTO.setId(2309200000000000688L);
        orderCancelDTO.setCancelReason("123455");
        orderCancelDTO.setCurrentUserId(1695339358949949440L);
        orderCancelDTO.setCurrentUserName("");
        orderCancelDTO.setCurrentUserType(UserType.C_USER);

        ordersManagerService.cancel(orderCancelDTO);
    }

    @Test
    void hide() {
        ordersManagerService.hide(1688799083319300096L, 1, 1678652825949913088L);
    }

    @Test
    public void testGetDetail() {
        OrderResDTO detail = ordersManagerService.getDetail(2023082500000000053L);
        log.info("detail:{}", detail);
    }

}