package com.jzo2o.customer.service;

import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.customer.model.dto.response.ServeSkillCategoryResDTO;
import com.jzo2o.mvc.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
public class ServeSkillServiceTest {

    @Resource
    private IServeSkillService serveSkillService;

    @BeforeAll
    public static void setUp() {
        CurrentUserInfo currentUserInfo = new CurrentUserInfo(1694955099182362626L, null, null, UserType.INSTITUTION);
        UserContext.set(currentUserInfo);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        UserContext.clear();
    }

    @Test
    public void testCategory() {
        List<ServeSkillCategoryResDTO> category = serveSkillService.category();
        log.info("category:{}", category);
    }
}
