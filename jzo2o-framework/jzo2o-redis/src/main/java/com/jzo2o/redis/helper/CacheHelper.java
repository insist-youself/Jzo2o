package com.jzo2o.redis.helper;

import com.jzo2o.common.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author itcast
 */
@Component
public class CacheHelper {
    private static final String CACHE_PREFIX = "CACHE_";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 缓存实现
     *
     * @param key
     * @param dataExecutor
     * @param clazz
     * @param ttl
     * @param <T>
     * @return
     */
    public <T> T get(String key, DataExecutor<T> dataExecutor, Class<T> clazz, Long ttl) {
        String redisKey = CACHE_PREFIX + key;
        Object value = redisTemplate.opsForValue().get(redisKey);
        // 缓存为“”直接返回是为了防止缓存穿透， 缓存有值直接返回
        if (StringUtils.EMPTY.equals(value)) {
            return null;
        }
        if (StringUtils.isNotEmpty(value.toString())) {
            // 自动延长redis有效期
            redisTemplate.expire(redisKey, ttl, TimeUnit.SECONDS);
            if (JsonUtils.isJson(value.toString())) {
                return BeanUtils.toBean(value, clazz);
            } else {
                return (T) value;
            }
        }
        // 默认有效期5分钟
        if (ttl == null) {
            ttl = 5 * 60L;
        }
        // 获取数据
        T data = dataExecutor.execute();
        // 写入缓存，
        if (data == null) {
            redisTemplate.opsForValue().set(redisKey, StringUtils.EMPTY, ttl, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(redisKey, JsonUtils.toJsonStr(data), ttl, TimeUnit.SECONDS);
        }
        return data;
    }

    /**
     * 批量获取缓存数据，按照id列表顺序返回目标数据,如果缓存不存在则查询数据库
     *
     * @param dataType               目标数据类型，CACHE_加dataType 为redisKey
     * @param objectIds              目标数据唯一id
     * @param batchDataQueryExecutor 批量目标数据获取执行器用于当缓存数据不存在时查询数据库
     * @param clazz                  目标数据类型class
     * @param ttl                    目标数据整体过期时间(ttl大于0才会设置有效期)
     * @param <K>                    目标数据id数据类型
     * @param <T>                    目标数据类型
     * @return
     */
    public <K, T> List<T> batchGet(String dataType, List<K> objectIds, BatchDataQueryExecutor<K, T> batchDataQueryExecutor, Class<T> clazz, Long ttl) {
        // 1.缓存获取数据
        // 1.1.参数校验
        if (StringUtils.isEmpty(dataType) || CollUtils.isEmpty(objectIds)) {
            return null;
        }
        // 1.2.redis key
        String redisKey = CACHE_PREFIX + dataType;
        // 1.3.获取缓存
        List<T> list = redisTemplate.opsForHash().multiGet(redisKey, objectIds);

        // 2.缓存未得数据获取
        // 2.1.获取未缓存数据的objectIds序号列表
        List<Integer> noCacheObjectIdIndexs = CollUtils.getIndexsOfNullData(list);
        if (CollUtils.isEmpty(noCacheObjectIdIndexs)) {
            return BeanUtils.copyToList(list, clazz);
        }
        // 2.2.获取未缓存数据的objectId列表
        List<K> noCacheObjectIds = CollUtils.valueofIndexs(objectIds, noCacheObjectIdIndexs);
        if (batchDataQueryExecutor == null) {
            return BeanUtils.copyToList(list, clazz);
        }
        // 3.获取未缓存数据
        Map<K, T> data = batchDataQueryExecutor.execute(noCacheObjectIds, clazz);
        if (CollUtils.isEmpty(data)) {
            return BeanUtils.copyToList(list, clazz);
        }
        // 4.未加入缓存数据入缓存
        redisTemplate.opsForHash().putAll(redisKey, data);
        // 5.动态设置缓存过期时间
        if(ttl > 0) {
            redisTemplate.expire(redisKey, ttl, TimeUnit.SECONDS);
        }
        // 6.组装返回数据，保证数据根据objectIds列表顺序返回
        for (Integer noCacheObjectIdIndex : noCacheObjectIdIndexs) {
            list.set(noCacheObjectIdIndex, data.get(objectIds.get(noCacheObjectIdIndex)));
        }
        return BeanUtils.copyToList(list, clazz);
    }

    /**
     * 批量获取缓存数据，按照id列表顺序返回目标数据
     *
     * @param dataType               目标数据类型，例如评价数据
     * @param batchDataQueryExecutor 目标数据获取执行器
     * @param clazz                  目标数据类型class
     * @param ttl                    目标数据整体过期时间(ttl大于0才会设置有效期)
     * @param <K>                    目标数据id数据类型
     * @param <T>                    目标数据类型
     * @return 查询结果
     */
    public <K, T> List<T> getAll(String dataType, BatchDataQueryExecutor<K, T> batchDataQueryExecutor, Class<T> clazz, Long ttl) {
        //1.构建redisKey
        String redisKey = CACHE_PREFIX + dataType;

        //2.从缓存查询数据
        Map<K, T> entries = redisTemplate.opsForHash().entries(redisKey);
        Collection<T> values = entries.values();

        //3.缓存中已查询出数据或不存在目标数据执行器，直接返回
        if (null == batchDataQueryExecutor || ObjectUtils.isNotEmpty(values)) {
            return BeanUtils.copyToList(values, clazz);
        }

        //4.执行目标数据查询执行器,使用者可以在查询执行方法中写入控制，来阻止缓存穿透
        Map<K, T> data = batchDataQueryExecutor.execute(null, clazz);
        if (data.size() <= 0) {
            return BeanUtils.copyToList(values, clazz);
        }

        //5.存储数据到缓存
        redisTemplate.opsForHash().putAll(redisKey, data);
        if(ttl > 0) {
            redisTemplate.expire(redisKey, ttl, TimeUnit.SECONDS);
        }

        //6.返回查询结果
        return BeanUtils.copyToList(values, clazz);
    }

    /**
     * 批量获取缓存数据，按照id列表顺序返回目标数据
     *
     * @param dataType    目标数据类型，例如评价数据
     * @param data        缓存数据
     * @param keyFunction hashKey获取函数
     * @param ttl         目标数据整体过期时间(ttl大于0才会设置有效期)
     * @param <K>         目标数据id数据雷兴国
     * @param <T>         目标数据类型
     * @return 查询结果
     */
    public <K, T> void doPutAll(String dataType, List<T> data, Function<T, K> keyFunction, Long ttl) {
        //1.构建redisKey
        String redisKey = CACHE_PREFIX + dataType;
        if (CollUtils.isEmpty(data)) {
            return;
        }
        K apply = keyFunction.apply(data.get(0));
        Map<K, T> map = data.stream().collect(Collectors.toMap(d -> keyFunction.apply(d), d -> d));
        //5.存储数据到缓存
        redisTemplate.opsForHash().putAll(redisKey, map);
        if(ttl > 0) {
            redisTemplate.expire(redisKey, ttl, TimeUnit.SECONDS);
        }
    }

    public <K> void batchRemove(String dataType, List<K> objectIds) {
        String redisKey = CACHE_PREFIX + dataType;
        redisTemplate.opsForHash().delete(redisKey, objectIds.toArray());
    }

    /**
     * 批量删除缓存
     *
     * @param keys 缓存key列表
     */
    public void batchRemove(List<String> keys) {
        // 1.redis key 列表
        List<String> redisKeys = keys.stream()
                .map(key -> CACHE_PREFIX + key)
                .collect(Collectors.toList());
        // 2.批量删除
        redisTemplate.delete(redisKeys);
    }

    /**
     * 删除缓存
     *
     * @param key 缓存key
     */
    public void remove(String key) {
        redisTemplate.delete(CACHE_PREFIX + key);
    }

    /**
     * 分页缓存删除
     *
     * @param dataType 数据类型
     * @param objectId 对象id
     */
    public <K> void remove(String dataType, K objectId) {
        String redisKey = CACHE_PREFIX + dataType;
        redisTemplate.opsForHash().delete(redisKey, objectId);
    }

    public interface DataExecutor<T> {
        T execute();
    }

    /**
     * 数据查询执行器
     *
     * @param <T>
     */
    public interface BatchDataQueryExecutor<K, T> {
        /**
         * 查询key对应的值
         *
         * @param objectIds 没有命中的对象id列表
         * @param clazz     数据转换类型
         * @return
         */
        Map<K, T> execute(List<K> objectIds, Class<T> clazz);
    }

}
