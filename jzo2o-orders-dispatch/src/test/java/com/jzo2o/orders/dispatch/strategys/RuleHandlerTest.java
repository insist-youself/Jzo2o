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
public class RuleHandlerTest {

    public static void main(String[] args) {
        IProcessRule ruleChain = null;
        // 策略1：构建责任链,先距离优先，距离相同再判断接单数
//        IProcessRule rule = new AcceptNumRule(null);
//        IProcessRule ruleChain = new DistanceRule(rule);
        // 策略2：构建责任链,先评分优先，评分相同再判断接单数
//        IProcessRule rule = new AcceptNumRule(null);
//        IProcessRule ruleChain = new ScoreRule(rule);
        // 策略3：构建责任链,先接单数优先，接单数相同再判断评分
//        IProcessRule rule = new ScoreRule(null);
//        IProcessRule ruleChain = new AcceptNumRule(rule);

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

        // 发起处理请求
        List<ServeProviderDTO> list = ruleChain.filter(serveProviderDTOS);
        //处理结果
        ServeProviderDTO result = null;
        // 3.1.唯一高优先级直接返回
        int size = 1;
        if((size = CollUtils.size(list)) == 1) {
            result = list.get(0);
        }
        // 3.2.多个高优先级随机返回
        int randomIndex = (int) (Math.random() * size);
        result = list.get(randomIndex);
        System.out.println(result);
    }
}
