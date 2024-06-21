-- 派单实现
-- key: 派单同步队列，资源库存，连续失败次数，服务时间状态，服务单数量
-- argv：派单id，被派单服务人员id/机构id,该派单服务时间，单子限制数量，服务人员类型（2，服务人员，3：机构端）

--服务时间冲突校验
local jsonRedisTemp={}
local serveTimeExists = redis.call("EXISTS",KEYS[4])
if ARGV[5] == 2 and serveTimeExists > 0
then
    return "-1";
end;
--派单失败次数
local dispatchFaildTimes = redis.call("GET", KEYS[3])
if dispatchFaildTimes ~= false and tonumber(dispatchFaildTimes) > 3
then
    return "-2";
end
--当前抢单派单数量
local ordersNum = redis.call("HGET", KEYS[5], ARGV[2])
-- hget 获取不到数据返回false而不是nil
if ordersNum ~= false or  (ordersNum == true and tonumber(ordersNum) >= tonumber(ARGV[4]))
then
    return "-3";
end
-- --库存是否充足校验
local stockNum = redis.call("HGET",KEYS[2], ARGV[1])
if stockNum == false or (stockNum == true and tonumber(stockNum) < 1)
then
    return "-4";
end
-- --减库存
stockNum = redis.call("HINCRBY",KEYS[2], ARGV[1], -1)
if tonumber(stockNum) < 0
then
    return "-5"
end
-- -- 派单结果写入同步队列
local result = redis.call("HSETNX", KEYS[1], ARGV[1],ARGV[2])
if result > 0
then
    return ARGV[1] ..""
end
return ""
