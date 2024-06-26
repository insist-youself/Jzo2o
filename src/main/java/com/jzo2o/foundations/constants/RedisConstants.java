package com.jzo2o.foundations.constants;

/**
 * redis相关常量
 *
 * @author itcast
 * @create 2023/8/15 14:58
 **/
public class RedisConstants {

    public static final class CacheName {
        /**
         * 家政服务缓存
         */
        public static final String JZ_CACHE = "JZ_CACHE";

        /**
         * 用户端首页服务图标
         */
        public static final String SERVE_ICON = "JZ_CACHE:SERVE_ICON";

        /**
         * 用户端首页热门服务
         */
        public static final String HOT_SERVE = "JZ_CACHE:HOT_SERVE";

        /**
         * 用户端已开通服务分类
         */
        public static final String SERVE_TYPE = "JZ_CACHE:SERVE_TYPE";

        /**
         * 服务项
         */
        public static final String SERVE_ITEM = "JZ_CACHE:SERVE_ITEM";

        /**
         * 服务
         */
        public static final String SERVE = "JZ_CACHE:SERVE_RECORD";
    }

    public static final class CacheManager {
        /**
         * 缓存时间永久
         */
        public static final String FOREVER = "cacheManagerForever";

        /**
         * 缓存时间永久
         */
        public static final String THIRTY_MINUTES = "cacheManager30Minutes";

        /**
         * 缓存时间1天
         */
        public static final String ONE_DAY = "cacheManagerOneDay";
    }

    public static final class Ttl {
        /**
         * 缓存时间30分钟
         */
        public static final int THIRTY = 30;
    }

}
