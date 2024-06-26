package com.jzo2o.redis.aspect;

import cn.hutool.extra.spring.SpringUtil;
import com.jzo2o.common.utils.AspectUtils;
import com.jzo2o.common.utils.StringUtils;
import com.jzo2o.redis.annotations.HashCacheClear;
import com.jzo2o.redis.handler.HashCacheKeyHandler;
import com.jzo2o.redis.helper.CacheHelper;
import com.jzo2o.redis.helper.JudgeRemoveCacheHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Slf4j
public class HashCacheClearAspect {

    private final CacheHelper cacheHelper;

    public HashCacheClearAspect(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }

    @Around("@annotation(hashCacheClear)")
    public Object pageClear(ProceedingJoinPoint pjp, HashCacheClear hashCacheClear) throws Throwable {
        // 1.校验是否清理缓存
        // 1.1.获取当前执行方法
        Method method = AspectUtils.getMethod(pjp);
        // 1.2.获取当前执行方法参数
        Object[] args = pjp.getArgs();

        // 1.3.校验是否清理缓存,不满足直接执行业务逻辑
        if (!hashCacheClear.always() && StringUtils.isEmpty(hashCacheClear.judgeRemoveCacheHandlerName())) {
            // 1.4.执行业务逻辑
            return pjp.proceed(pjp.getArgs());
        }
        // 1.5.通过JudgeRemoveCacheHandler判断是否删除缓存
        List<Object> params = AspectUtils.parseList(hashCacheClear.judgeRemoveCacheHandlerParamNames(), method, args);
        JudgeRemoveCacheHandler judgeRemoveCacheHandler = getBean(hashCacheClear.judgeRemoveCacheHandlerName(), JudgeRemoveCacheHandler.class);
        if(judgeRemoveCacheHandler != null && !judgeRemoveCacheHandler.judgeRemoveCache(params)) {
            return pjp.proceed(pjp.getArgs());
        }
        // 2.准备清理缓存数据
        String key = null;
        if (StringUtils.isNotEmpty(hashCacheClear.key())) {
            // 2.1.可以直接获取dataType
            key = AspectUtils.parse(hashCacheClear.key(), method, args);
        } else {
            // 2.2.通过自定义适配器获取dataType
            // 2.2.1.获取适配器
            HashCacheKeyHandler hashCacheKeyHandler = getBean(hashCacheClear.keyHandlerName(), HashCacheKeyHandler.class);
            // 2.2.2.获取入参
            List<Object> dataTypeParams = AspectUtils.parseList(hashCacheClear.keyHandlerParamNames(), method, args);
            // 2.2.3.获取dataType
            key = hashCacheKeyHandler.key(dataTypeParams);
        }

        // 3.执行业务逻辑
        Object executeResult = pjp.proceed(args);

        // 4.批量清理缓存
        if ( hashCacheClear.batchFieldIdsIndex() > -1) {
            // 4.1.批量清理缓存
            // 4.1.1.获取objectIds所在的入参data
            Object data = args[hashCacheClear.batchFieldIdsIndex()];
            // 4.1.2.入参为空不能清理缓存，直接返回
            if (data == null) {
                return executeResult;
            }
            // 4.1.3.批量删除
            cacheHelper.batchRemove(key, (List<Object>) data);

        } else if (StringUtils.isNotEmpty(hashCacheClear.fieldId())) {
            // 4.2.删除单个缓存
            cacheHelper.remove(key, AspectUtils.parse(hashCacheClear.fieldId(), method, args));
        }

        return executeResult;
    }


    private <T> T getBean(String beanName, Class<T> clazz) {
        if (StringUtils.isEmpty(beanName)) {
            return null;
        }
        return SpringUtil.getBean(beanName, clazz);
    }


}
