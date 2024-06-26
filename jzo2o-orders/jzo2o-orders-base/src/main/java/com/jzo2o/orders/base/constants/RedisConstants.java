package com.jzo2o.orders.base.constants;

public class RedisConstants {
    public static final class RedisFormatter {
        /**
         * 抢单用户锁模板，格式：ORDERS:SEIZE_#{serveProviderId}
         */
        public static final String SEIZE = "ORDERS:SEIZE_#{serveProviderId}";

        /**
         * 派单锁模板，格式：ORDERS:DISPATCH_#{id}
         */
        public static final String DISPATCH = "ORDERS:DISPATCH_#{id}";
        /**
         * 加入派单池锁模板，格式：ORDERS:JSONDISPATCHLIST_#{id}
         */
        public static final String JSONDISPATCHLIST = "ORDERS:JSONDISPATCHLIST_#{id}";

        public static final String REJECT = "ORDERS:DISPATCH:REJECT_#{dispatchRejectReqDTO.id}";

        /**
         * 抢单超时
         */
        public static final String SEIZE_TIME_OUT = "ORDERS:SEIZE:TIME_OUT:#{cityCode}";

        public static final String ORDERS_SERVE_TIMEOUT = "ORDERS:SEVER_TIMEOUT:#{ordersServe.id}";
    }
    public static final class RedisKey {
        /**
         * 抢单id列表redis key模板：ORDERS:SEIZE:IDS_{服务人员id或机构id}_{citycode}
         */
        public static final String ORDERS_SEIZE_IDS_FOR_SERVE_PROVIDE = "ORDERS:SEIZE:IDS_%s_%s";

        /**
         * 抢单列表redis key模板：ORDERS:SEIZE:PAGE_{citycode}
         */
        public static final String ORDERS_SEIZE_PAGE = "ORDERS:SEIZE:PAGE_%s";

        // 抢单相关
        /**
         *抢单池，key格式:ORDERS:SEIZE:POOL_{抢单id}_{抢单id尾号}
         */
        public static final String ORDERS_SEIZE = "ORDERS:SEIZE:%s_{%s}";

        /**
         * 抢单结果同步队列，该队列分为10条，格式：ORDERS:SEIZE:SYNC_{抢单id尾号}
         * 序号分别为：0到9的10个数字
         */
        public static final String ORERS_SEIZE_SYNC_QUEUE_NAME = "ORDERS:SEIZE:SYNC";

        /**
         * 当前用户/机构当前接单数量，接单+派单总数量
         * 接单+派单操作+1
         * 完成服务/取消服务-1
         * 格式：PROVIDER:SERVE:NUM_{cityCode}_{序号}
         */
        public static final String PROVIDER_SERVE_NUM = "PROVIDER:SERVE:NUM_%s_{%s}";

        /**
         * 所有服务单放在一起
         */
        public static final String SERVE_ORDERS = "SERVE_ORDERS:PAGE_QUERY:PAGE_%s";

        public static final String SERVE_ORDERS_KEY = "SERVE_ORDERS:PAGE_QUERY:PAGE_#{serveProviderId}";

        /**
         * 服务人员或机构每日取消服务单次数
         */
        public static final String SERVE_CANCEL_TIMES_EVERY_DAY = "ORDERS:SERVE:CANCEL_%s_%s";

        public static final String SERVE_TIME_UPDATE = "ORDERS:SERVE_TIME:#{id}";

        /**
         * 库存，hash结构
         */
        public static final String ORDERS_RESOURCE_STOCK = "ORDERS:RESOURCE:STOCK:{%s}";

        /**
         * 派单连续失败次数 string
         */
        public static final String ORDERS_DISPATCH_FAILD_TIMES = "ORDERS:DISPATCH:FAILD_TIMES_%s_{%s}";

        /**
         * 派单成功同步队列名称
         */
        public static final String ORERS_DISPATCH_SYNC_QUEUE_NAME = "ORDERS:DISPATCH:SYNC";

        /**
         * 服务时间状态 string 格式：PROVIDER:SERVE_TIME:{serveProviderId}_{serveTime}_{序号}
         */
        public static final String SERVE_PROVIDER_TIMES = "PROVIDER:SERVE_TIME:%s_%s_{%s}";

        /**
         * 服务状态表 hash 格式：PROVIDER:SERVE_STATE:{序号}
         * key 格式：{服务人员/机构id}_{times或num}；time表示接单时间列表，nun表示接单数量
         */
        public static final String SERVE_PROVIDER_STATE = "PROVIDER:SERVE_STATE:{%s}";

        /**
         * 派单列表 - zSet结构
         */
        public static final String DISPATCH_LIST = "ORDERS:DISPATCH:LIST";

        /**
         * 用户端滚动分页查询订单
         */
        public static final String ORDERS = "ORDERS:PAGE_QUERY:PAGE_%s";
    }

    public static class Lock {
        /**
         * 抢派单分布式锁
         */
        public static final String DISPATCH_OR_SEIZE_LOCK = "ORDERS:DISPATCH_OR_SEIZE:%s";

        public static final String PROVIDER_DISPATCH_LOCK = "PROVIDER:DISPATCH:LOCK_%s";

        /**
         * 派单同步锁定同步队列redis分布式锁
         */
        public static final String DISPATCH_SYNC_LOCK = "ORDERS:DISPATCH:SYNC_%s";

        /**
         * 接单超时
         */
        public static final String DISPATCH_RECEIVE_TIMEOUT_LOCK = "ORDERS:DISPATCH:TIMEOUT";

        /**
         * 抢单单同步锁定同步队列redis分布式锁
         */
        public static final String SEIZE_SYNC_LOCK = "ORDERS:SEIZE:SYNC_%s";

        /**
         * 订单分片id获取锁
         */
        public static final String ORDERS_SHARD_KEY_ID_LOCK = "ORDERS:SHARD_KEY:LOCK";

        public static final String ORDERS_SHARD_KEY_ID_GENERATOR = "ORDERS:SHARD_KEY:GENERATOR";

    }
    public static class Ttl {
        /**
         * 订单抢单缓存 60s
         */
        public static final long ORDERS_SEIZE_PAGE_TTL = 60;

        /**
         * 服务单分页有效期
         */
        public static final long SERVE_ORSERS_PAGE_TTL = 600;


        /**
         * 订单分页有效期
         */
        public static final long ORDERS_PAGE_TTL = 600;


        /**
         * 抢单派单处理超时时间单位s
         */
        public static final long DISPATCH_SEIZE_LOCK_TTL = 10;

        /**
         * 派单锁定，10分钟内不能再次派单
         */
        public static final long PROVIDER_DISPATCH_LOCK_TTL = 600;
    }
}
