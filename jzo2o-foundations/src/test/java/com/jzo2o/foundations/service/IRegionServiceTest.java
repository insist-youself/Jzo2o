package com.jzo2o.foundations.service;

import com.jzo2o.api.foundations.dto.response.RegionSimpleResDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class IRegionServiceTest {
    @Resource
    private IRegionService regionService;

    @Test
    void listSimple() {
        List<RegionSimpleResDTO> regionSimpleResDTOS = regionService.queryActiveRegionList();
        System.out.println(regionSimpleResDTOS);
    }

//    @Test
//    void active() {
//        regionService.changeActiveStatus(1686303222843662337L, 1);
//    }
}