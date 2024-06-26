-- 抢单lua实现
-- key: 抢单同步队列，资源库存
-- argv：抢单id,被派单服务人员id/机构id,服务人员类型（2，服务人员，3：机构端）,是否是机器抢单（1：机器抢单，0：人工抢单）

-- --库存是否充足校验
local stockNum = redis.call("HGET",KEYS[2], ARGV[1])
if stockNum == false or  tonumber(stockNum) < 1
then
    return "-1";
end
-- --减库存
stockNum = redis.call("HINCRBY",KEYS[2], ARGV[1], -1)
if stockNum < 0
then
    return "-2"
end
-- -- 抢单结果写入同步队列
local result = redis.call("HSETNX", KEYS[1], ARGV[1],"[" ..ARGV[2] .."," .. ARGV[3] .."," .. ARGV[4] .."]")
if result > 0
then
    return ARGV[1] ..""
end
return "-3"
