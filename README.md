# 家缘优选
家缘优选是一个家政服务 O2O（Online to Offline）平台，是基于 Spring Cloud Alibaba 框架构建的微服务项目。用户可通过小程序在线下单、支付；家政服务人员和家政公司则通过平台进行抢单；抢单成功后服务人员需到线下提供服务，平台跟进整个服务过程；服务完成后用户可进行评价以及售后、退款等操作；运营人员可通过管理端完成服务人员管理、机构管理、订单管理等业务。


## 项目展示
- 运营管理端（PC）
![img.png](https://jzo2o-oss-001.oss-cn-hangzhou.aliyuncs.com/Jzo2o/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202024-07-19%20155639.jpg)
- 服务机构端（PC）

  <img src="https://jzo2o-oss-001.oss-cn-hangzhou.aliyuncs.com/Jzo2o/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202024-07-19%20161255.jpg"  alt=""/>
- 服务人员端（app）

  <img src="https://jzo2o-oss-001.oss-cn-hangzhou.aliyuncs.com/Jzo2o/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202024-07-19%20160323.jpg"  height="600"  alt=""/>
- 用户端（小程序）

  <img src="https://jzo2o-oss-001.oss-cn-hangzhou.aliyuncs.com/Jzo2o/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202024-07-19%20161705.jpg"  height="600"  alt=""/>
  

## 项目目录
| 目录                                                                                            | 描述      |
|-----------------------------------------------------------------------------------------------|---------|
| 🏘️ [jzo2o-api](https://github.com/insist-youself/Jzo2o/tree/master/jzo2o-api)	               | API服务模块 |
| 🏘 [jzo2o-customer](https://github.com/insist-youself/Jzo2o/tree/master/jzo2o-customer)       | 用户服务模块  |
| 🔗 [jzo2o-foundations](https://github.com/insist-youself/Jzo2o/tree/master/jzo2o-foundations) | 基础服务模块  |
| 🏘️ [jzo2o-framework](https://github.com/insist-youself/Jzo2o/tree/master/jzo2o-framework)    | 基础配置模块  |
| 🕸️ [jzo2o-gateway](https://github.com/insist-youself/Jzo2o/tree/master/jzo2o-gateway)        | 网关模块    |
| 🔗 [jzo2o-market](https://github.com/insist-youself/Jzo2o/tree/master/jzo2o-market)           | 优惠活动模块  |
| 🛠 [jzo2o-orders](https://github.com/insist-youself/Jzo2o/tree/master/jzo2o-orders)           | 订单模块    |
| 🕸️ [jzo2o-publics](https://github.com/insist-youself/Jzo2o/tree/master/jzo2o-publics)        | 公共服务模块  |
| 🏘  [jzo2o-trade](https://github.com/insist-youself/Jzo2o/tree/master/jzo2o-trade)                                                                           | 交易服务模块  |


## 系统架构
项目是基于Spring Cloud Alibaba框架构建的微服务项目，采用前后端分离模式进行开发，系统架构图如下：
![image2](https://jzo2o-oss-001.oss-cn-hangzhou.aliyuncs.com/Jzo2o/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202024-07-19%20171601.jpg)
- 用户层：
包括四个端：运营端(PC)、服务端（APP）、机构端（PC）、用户端（小程序）
- 负载层：
反向代理、负载均衡。
- 服务层：包括网关、业务微服务、基础服务。
- 业务微服务：包括运营基础服务、客户管理服务、订单管理服务、抢单服务、派单服务、支付服务等。
- 基础服务：Nacos（服务注册、配置中心）、XXL-JOB（任务调度）、RabbitMQ（消息队列）、Elasticsearch（全文检索）、Canal（数据同步）、Sentinel（熔断降级、限流）等。
- 数据层：
MySQL数据库存储：服务信息、区域信息、客户信息、订单信息、支付信息、抢单池、派单池、结算信息等。
- 分库分表：使用ShardingShphere进行分库分表。
- TiDB分布式数据库存储：历史订单信息。
- 消息队列：存储数据同步消息、各类异步消息等。
- 索引：服务信息、服务提供者信息、订单信息等。
- 缓存：服务信息、订单信息、服务单信息等。


### 项目核心交互流程图：
![image2](https://jzo2o-oss-001.oss-cn-hangzhou.aliyuncs.com/Jzo2o/%E6%B5%81%E7%A8%8B%E5%9B%BE.jpg)
	
	
## 项目学习收获
### 技术
- 掌握Spring Cloud 在项目中的开发与调优能力
- 掌握Redis在项目中的应用能力 
- 掌握缓存技术方案的分析与设计能力 
- 掌握Canal+MQ异构数据同步的开发调试能力 
- 掌握Elasticsearch全文检索与地理搜索的开发能力 
- 掌握ShardingSphere分库分表的方案设计与开发能力 
- 掌握Seata分布式事务控制的开发能力 
- 掌握数据冷热分离技术方案的设计与开发能力 
- 掌握XXL-JOB+线程池任务调度方案的设计与开发能力
- 掌握系统调优与线上故障处理的能力 
- 掌握状态机组件的设计与开发能力 
### 业务
- 掌握项目需求分析能力
- 掌握系统分析与设计的能力
- 掌握门户业务的设计与开发能力 
- 掌握订单支付业务的系统设计与开发能力 
- 掌握优惠券&活动管理业务的系统设计与开发能力 
- 掌握秒杀抢购业务的常见设计方案与开发能力 
- 掌握派单调度类业务的系统设计与开发能力 
- 掌握客户管理业务的系统设计与开发能力 
- 掌握活动管理业务的系统设计与开发能力 
- 掌握搜索附近业务的系统设计与开发能力 
- 掌握服务管理&商品管理业务的系统计与开发能力 
- 掌握统计分析与看板业务的系统设计与开发能力


# 欢迎贡献与交流
项目需要大家的支持，期待更多小伙伴的贡献，你可以：
对于项目中的Bug，能够在Issues区提出建议，我会积极响应