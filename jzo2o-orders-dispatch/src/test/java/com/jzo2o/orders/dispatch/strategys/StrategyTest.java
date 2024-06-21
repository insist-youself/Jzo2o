package com.jzo2o.orders.dispatch.strategys;

import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.orders.dispatch.model.dto.ServeProviderDTO;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2023/11/24 6:05
 */
public class StrategyTest {

    public static void main(String[] args) {
        // 创建数据
        List<ServeProviderDTO> serveProviderDTOS = Arrays.asList(
                //1号 接单数最少
                ServeProviderDTO.builder().id(1L).acceptanceNum(0).acceptanceDistance(30).evaluationScore(50).build(),
                //2号 得分最高
                ServeProviderDTO.builder().id(2L).acceptanceNum(1).acceptanceDistance(10).evaluationScore(100).build(),
                //3号 得分最高
                ServeProviderDTO.builder().id(3L).acceptanceNum(2).acceptanceDistance(10).evaluationScore(100).build(),
                //4号 距离最近
                ServeProviderDTO.builder().id(4L).acceptanceNum(2).acceptanceDistance(5).evaluationScore(50).build(),
                //4号 距离最近
                ServeProviderDTO.builder().id(5L).acceptanceNum(1).acceptanceDistance(5).evaluationScore(50).build()
        );
        //获取距离优先策略
//        IProcessStrategy processStrategy = new DistanceStrategyImpl();
        IProcessStrategy processStrategy =null;
        //通过策略bean进行匹配处理
        ServeProviderDTO precedenceServeProvider = processStrategy.getPrecedenceServeProvider(serveProviderDTOS);
        System.out.println(precedenceServeProvider);
    }
}
