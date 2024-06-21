//package com.jzo2o.orders.dispatch.strategys;
//
//import com.jzo2o.common.utils.CollUtils;
//import com.jzo2o.orders.dispatch.model.dto.ServeProviderDTO;
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * @author Mr.M
// * @version 1.0
// * @description 按评分排序
// * @date 2023/11/24 5:58
// */
//public class ScoreRule extends AbstractProcessRule implements IProcessRule {
////    private IProcessRule next;
//
//    public ScoreRule(IProcessRule next) {
//        super(next);
////        this.next = next;
//    }
//
//    public List<ServeProviderDTO> doFilter(List<ServeProviderDTO> serveProviderDTOS) {
//        System.out.println("按评分排序");
//        if (CollUtils.size(serveProviderDTOS) < 2) {
//            return serveProviderDTOS;
//        }
//        //  2.按照比较器进行排序，排在最前方优先级最高
//        serveProviderDTOS = serveProviderDTOS.stream().sorted(Comparator.comparing(ServeProviderDTO::getEvaluationScore).reversed()).collect(Collectors.toList());
//        // 3.遍历优先级最高一批数据
//        ServeProviderDTO first = CollUtils.getFirst(serveProviderDTOS);
//
//        //获取相同级别的
//        return serveProviderDTOS.stream()
//                .filter(origin -> Comparator.comparing(ServeProviderDTO::getEvaluationScore).compare(origin, first) == 0)
//                .collect(Collectors.toList());
//
//
//    }
//
////    @Override
////    public List<ServeProviderDTO> filter(List<ServeProviderDTO> serveProviderDTOS) {
////        List<ServeProviderDTO> result = this.doFilter(serveProviderDTOS);
////        if(CollUtils.size(result) > 1 && next != null) {
////            return next.filter(result);
////        }else {
////            return result;
////        }
////    }
////
////    @Override
////    public IProcessRule next() {
////        return next;
////    }
//
//}
