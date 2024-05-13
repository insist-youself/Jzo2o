package com.jzo2o.foundations.client;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.model.domain.Region;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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
    SpringCacheService springCacheService;


    @Test
    public void test_update() throws IOException {
        Region update = springCacheService.update(1675857554875428866L);
        System.out.println(update);
    }

    @Test
    public void test_update2() throws IOException {
        Region update = springCacheService.update2(1675857554875428866L);
        System.out.println(update);
    }

    @Test
    public void test_get() throws IOException {
        Region region = springCacheService.get(1677152267410149374L);
        System.out.println(region);
    }


}

@Service
class SpringCacheService extends ServiceImpl<RegionMapper, Region> {


    //    @CachePut(value = "XZB_CACHE:REGION", key = "#id",cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES)
    //返回值中activeStatus等2才缓存
    @CachePut(value = "XZB_CACHE:REGION", key = "#id", unless = "#result.activeStatus!=2", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES)
    @Transactional
    public Region update(long id) {
        Region region = baseMapper.selectById(id);
        if (region == null) {
            throw new RuntimeException("error!");
        }
        region.setName(region.getName() + "a");
        baseMapper.updateById(region);

        return region;
    }

    @CachePut(value = "XZB_CACHE:REGION", key = "#id", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES)
    //执行方法前先删除
    @CacheEvict(value = "XZB_CACHE:REGION", key = "#id", beforeInvocation = true)
    @Transactional
    public Region update2(long id) {
        Region region = baseMapper.selectById(id);
        if (region == null) {
            throw new RuntimeException("error!");
        }
        region.setName(region.getName() + "a");
        baseMapper.updateById(region);

        return region;
    }

    @Caching(
            cacheable = {
                    //result.id为空时缓存时间短一些
                    @Cacheable(value = "XZB_CACHE:REGION", key = "#id", unless = "#result.id!=null", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
                    //activeStatus为2时缓存时间不过期
                    @Cacheable(value = "XZB_CACHE:REGION", key = "#id", unless = "#result.activeStatus!=2", cacheManager = RedisConstants.CacheManager.FOREVER)
            }
    )
    public Region get(long id) {
        Region region = baseMapper.selectById(id);
        if (region == null) {
            //防止缓存穿透
            region = new Region();
        }
        return region;
    }
}