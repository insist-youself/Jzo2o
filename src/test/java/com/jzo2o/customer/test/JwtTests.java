package com.jzo2o.customer.test;

import com.jzo2o.common.utils.JwtTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class JwtTests {

    @Resource
    private JwtTool jwtTool;
    @Test
    public void test() {
        String token = jwtTool.createToken(1L, "李四你好", "1", 1);
        log.info("{}",token);
    }
}
