package com.jzo2o.market.constants;

public class RedisConstants {

    public static final class RedisKey {
        /**
         * 优惠券库存表 由Canal同步程序写入
         * redis key格式：COUPON:RESOURCE:STOCK:{序号}  序号=活动id% 10
         * hash结构 (hashKey:活动id,hashValue:库存数量)
         */
        public static final String COUPON_RESOURCE_STOCK = "COUPON:RESOURCE:STOCK:{%s}";

        /**
         * 抢券同步队列，存储抢券成功记录由抢券程序(Lua)写入
         * redis key格式 QUEUE:COUPON:SEIZE:SYNC:{序号}   序号=活动id% 10
         * hash结构 (hashKey:活动id,hashValue:用户id)
         */
        public static final String COUPON_SEIZE_SYNC_QUEUE_NAME = "COUPON:SEIZE:SYNC";

        /**
         * 抢券成功列表，用户抢券成功写入记录
         * redis key格式 ：COUPON:SEIZE:LIST:活动id_{序号} 序号=活动id% 10
         * hash结构 (hashKey:用户id,hashValue:1)
         */
        public static final String COUPON_SEIZE_LIST = "COUPON:SEIZE:LIST:%s_{%s}";

        /**
         * 活动列表 由于活动预热程序写入，待开始及进行中的活动
         * redis key格式:ACTIVITY:LIST
         * string 结构: value=活动列表json串
         */
        public static final String ACTIVITY_CACHE_LIST = "ACTIVITY:LIST";
    }

    public static final class Formatter {
        /**
         * 优惠券抢券同步
         */
        public static final String COUPON_SEIZE_HANDLE_LOCK = "COUPON:SEIZE:RESULT_PROCESS";

        /**
         * 活动预热
         */
        public static final String ACTIVITY_PREHEAT = "ACTIVITY:PREHEAT";

        /**
         * 活动结束
         */
        public static final String ACTIVITY_FINISHED = "ACTIVITY:FINISHED";

    }
}
