package com.jzo2o.redis.helper;

import com.jzo2o.common.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author itcast
 */
@Component
@Slf4j
public class LockHelper {

    private static final String LOCK_PREFIX = "LOCK:";
    @Resource
    private RedissonClient redissonClient;

    public void synchronizeLock(String key, Long waitTime, final Execution execution) {
        RLock lock = null;
        try {
            // 加锁
            lock = getRLock(key);
            if (!lock.tryLock(NumberUtils.null2Zero(waitTime), -1, TimeUnit.SECONDS)) {
                return;
            }
            // 加锁成功执行操作
            execution.execute();
        } catch (Exception e) {
            log.error("同步任务执行异常e:", e);
        } finally {
            // 解锁
            if (lock != null) {
                lock.unlock();
            }
        }
    }

    public interface Execution {
        /**
         * 加锁后操作
         */
        void execute();
    }

    /**
     * 尝试加锁
     *
     * @param lockName 锁名
     * @param waitTime 等待时间，单位ms
     * @param leaseTime 租约时间，单位ms
     * @return 返回锁
     */
    public RLock trylock(String lockName, long waitTime, long leaseTime,TimeUnit unit) {
        RLock lock = redissonClient.getLock(lockName);
        try {
            if(lock.tryLock(waitTime, leaseTime, unit)) {
                return lock;
            }
        }catch (InterruptedException e) {
        }
        return null;
    }


    /**
     * 解锁
     *
     * @param rLock
     */
    public void unlock(RLock rLock) {
        if(rLock != null && rLock.isLocked()) {
            rLock.unlock();
        }
    }

    private RLock getRLock(String key) {
        return redissonClient.getLock(LOCK_PREFIX + key);
    }

}
