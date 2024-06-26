package com.jzo2o.orders.dispatch.service;

import com.jzo2o.api.foundations.RegionApi;
import com.jzo2o.api.foundations.dto.response.RegionServeInfoResDTO;
import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.orders.base.model.domain.OrdersDispatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

import java.util.List;
import java.util.Set;

@SpringBootTest
@Slf4j
class IDispatchServiceTest {

//    @Resource
//    private IDispatchService dispatchService;
//
//    @Resource
//    private IOrdersDispatchService ordersDispatchService;

    @Resource
    private RegionApi regionApi;

    @Resource
    private RedisTemplate redisTemplate;


    @Test
    public void testSortedSet_add(){
        for (int i = 0; i < 10; i++) {
            //向key为：test_sortedset的sortedset中添加10个元素，value为i，socre为i(转为double)
            redisTemplate.opsForZSet().add("test_sortedset",i,i*1d);
        }

    }
    @Test
    public void testSortedSet_rang(){
        //使用rangeByScore查询score范围从0到5
        Set test_sortedset = redisTemplate.opsForZSet().rangeByScore("test_sortedset", 0, 5);
        test_sortedset.stream().forEach(System.out::println);
        //将key为test_sortedset中的vlaue为0的元素的socre加10
        redisTemplate.opsForZSet().incrementScore("test_sortedset", 0, 10);

    }





    @BeforeEach
    void setUp() {
        CurrentUserInfo currentUserInfo = new CurrentUserInfo(1682263143260631042L, null, null, UserType.INSTITUTION);

        UserContext.set(currentUserInfo);
    }

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Test
    void dispatchRejct() {
    }


    @Test
    void queryForList() {
    }

    @Test
    void getDetail() {
    }

}