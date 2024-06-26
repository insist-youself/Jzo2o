package com.jzo2o.foundations.service;

import com.jzo2o.foundations.model.dto.request.ServeItemUpsertReqDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class IServeItemServiceTest {

    @Resource
    private IServeItemService serveItemService;

//    @Test
//    void changeActiveStatus() {
//        serveItemService.changeActiveStatus(1685850705647194113L, 1);
//    }

    @Test
    void update() {
        ServeItemUpsertReqDTO serveItemUpsertReqDTO = new ServeItemUpsertReqDTO();
        serveItemUpsertReqDTO.setName("空调维修");
        serveItemService.update(1685850705647194113L, serveItemUpsertReqDTO);
    }
}