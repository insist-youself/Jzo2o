package com.jzo2o.foundations.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.model.domain.Region;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;


@SpringBootTest
@Slf4j
public class SpringCacheTest {

    @Resource
    SpringCacheTestService springCacheTestService;

    @Test
    public void test_findRegionById() throws IOException {
        Region region = springCacheTestService.findRegionById(1677152267410149373L);
        System.out.println(region);
    }

    @Test
    public void test_deleteById() throws IOException {
        try {
            springCacheTestService.deleteById(111L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
  @Service
  class SpringCacheTestService extends ServiceImpl<RegionMapper, Region> {

    //unless控制当返回值为null时忽略缓存即不缓存，否则将缓存
//    @Cacheable(value = "CACHETEST:REGION", key = "#id",unless = "#result==null",cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES)
    @Cacheable(value = "CACHETEST:REGION", key = "#id",cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES)
    public Region findRegionById(Long id){
        Region region = baseMapper.selectById(id);
        return region;
    }
    @Transactional

    @Caching(evict = {
            @CacheEvict(value = "CACHETEST:REGION", key = "#id", beforeInvocation = true),//执行前删除
            @CacheEvict(value = "CACHETEST:REGION", key = "#id")//执行后删除
    }
    )
    public void deleteById(Long id){
        baseMapper.deleteById(id);
    }

}