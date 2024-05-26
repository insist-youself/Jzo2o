package com.jzo2o.foundations.handler;

import com.jzo2o.api.foundations.dto.response.RegionSimpleResDTO;
import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.service.HomeService;
import com.jzo2o.foundations.service.IRegionService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * SpringCache 缓存同步任务
 * @author linger
 * @date 2024/5/22 20:04
 */

@Slf4j
@Component
public class SpringCacheSyncHandler {

    @Resource
    private IRegionService regionService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private HomeService homeService;

    /**
     * 已启用区域缓存更新
     * 每日凌晨 1点更新
     */
    @XxlJob(value = "activeRegionCacheSync")
    public void activeRegionCacheSync() {
        log.info(">>>>>>> 开始进行缓存同步，更新已经启用区域");
        // 1. 清理缓存
        String key = RedisConstants.CacheName.JZ_CACHE + "::ACTIVE_REGIONS";
        redisTemplate.delete(key);

        // 2. 更新缓存
        regionService.queryActiveRegionListCache();
        log.info(">>>>>>> 更新已启用区域完成");

        log.info(">>>>>>> 开始进行缓存同步，更新已经启用服务");
        // 1. 清理缓存
        List<RegionSimpleResDTO> regionSimpleResDTOS = regionService.queryActiveRegionList();
        // 遍历该区域，对每个区域的首页服务进行删除缓存再添加缓存
        regionSimpleResDTOS.forEach(item -> {
            String key1 = RedisConstants.CacheName.SERVE_ICON + item.getId();
            redisTemplate.delete(key1);
            homeService.queryServeIconCategoryByRegionIdCache(item.getId());
        });

        log.info(">>>>>>> 更新已启用服务完成");


        log.info(">>>>>>> 开始进行缓存同步，更新已经启用服务项");
        regionSimpleResDTOS.forEach(item -> {
            String key1 = RedisConstants.CacheName.SERVE_TYPE + item.getId();
            redisTemplate.delete(key1);
            homeService.queryServeTypeList(item.getId());
        });
        log.info(">>>>>>> 更新已启用服务项完成");

        log.info(">>>>>>> 开始进行缓存同步，更新已经启用热门服务");
        regionSimpleResDTOS.forEach(item -> {
            String key1 = RedisConstants.CacheName.HOT_SERVE + item.getId();
            redisTemplate.delete(key1);
            homeService.queryHotServeList(item.getId());
        });
        log.info(">>>>>>> 更新已启用热门服务完成");


    }


}
