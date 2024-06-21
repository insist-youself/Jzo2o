package com.jzo2o.market.config;

import com.jzo2o.redis.model.SyncMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import javax.annotation.Resource;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
public class SyncThreadPoolTest {

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    RedissonClient redissonClient;


//    public class RunnableSimple implements Runnable{
//        //任务序号
//        private int index;
//        public RunnableSimple(int index){
//            this.index = index;
//        }
//
//        @Override
//        public void run() {
//            //执行任务
//            log.info("{}执行任务:{}",Thread.currentThread().getId(),index);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    public class RunnableSimple implements Runnable{
        //任务序号
        private int index;
        public RunnableSimple(int index){
            this.index = index;
        }

//        @Override
//        public void run() {
//            //执行任务
//            log.info("{}执行任务:{}",Thread.currentThread().getId(),index);
//           // 获取数据
//            String queue = String.format("QUEUE:COUPON:SEIZE:SYNC:{%s}",index);
//            log.info("开始获取{}队列的数据",queue);
//            getData(queue);
//        }


        @Override
        public void run() {
            // 指定锁名称，每个队列只有一个线程去处理，锁粒度为队列，锁名称在队列名称前加Lock:
            String lockName = String.format("lock:QUEUE:COUPON:SEIZE:SYNC:{%s}",index);
            // 队列名称
            String queue = String.format("QUEUE:COUPON:SEIZE:SYNC:{%s}",index);
            RLock lock = redissonClient.getLock(lockName);

            try {
                boolean isLock = lock.tryLock(3, -1, TimeUnit.SECONDS);
                if (isLock) {
                    //执行任务
                    log.info("{}执行任务:{}",Thread.currentThread().getId(),index);

                    log.info("{}开始获取{}队列的数据",Thread.currentThread().getId(),queue);

                    getData(queue);
                    //模拟执行任务的时长
                    Thread.sleep(50000);
                }else{
                    log.info("!!!!!!{}获取{}队列的锁失败",Thread.currentThread().getId(),queue);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 4.释放锁
                if (lock != null || lock.isLocked()) {
                    lock.unlock();
                }
            }

        }
    }

    /**
     *  从同步队列拿数据
     * @param queue 队列名称
     */
    private void getData(String queue) {

        Cursor<Map.Entry<String, Object>> cursor = null;
        // 通过scan从redis hash数据中批量获取数据，获取完数据需要手动关闭游标
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .count(10)
                .build();
        try {
            cursor =  redisTemplate.opsForHash().scan(queue, scanOptions);
            // 遍历数据转换成SyncMessage列表
            List<SyncMessage<Object>> collect = cursor.stream()
                    .map(entry -> SyncMessage
                            .builder()
                            .key(entry.getKey().toString())
                            .value(entry.getValue().toString())
                            .build())
                    .collect(Collectors.toList());
            log.info("{}获取{}数据{}条", Thread.currentThread().getId(),queue,collect.size());
            collect.stream().forEach(System.out::println);
        } catch (Exception e) {
            log.error("同步处理异常，e:", e);
            throw new RuntimeException(e);
        } finally {
            // 关闭游标
            if (cursor != null) {
                cursor.close();
            }
        }

    }


    @Resource(name="syncThreadPool")
    private ThreadPoolExecutor threadPoolExecutor;

    @Test
    public void test_threadPool() throws InterruptedException {
//        for (int i = 0; i < 10; i++) {
//            threadPoolExecutor.execute(new RunnableSimple(i));
//        }
//        Thread.sleep(3000);// 延迟三秒，线程池的线程执行任务后空闲下来
//        for (int i = 10; i < 20; i++) {
//            threadPoolExecutor.execute(new RunnableSimple(i));
//        }
//        threadPoolExecutor.execute(new RunnableSimple(i));
        //主线程休眠一定的时间防止程序结束

        threadPoolExecutor.execute(new RunnableSimple(0));
        //Thread.sleep(3000);//延迟两秒，线程池的线程执行任务后空闲下来
        threadPoolExecutor.execute(new RunnableSimple(0));

        Thread.sleep(9999999);
    }
 }