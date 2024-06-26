package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jzo2o.api.customer.dto.request.EvaluationSubmitReqDTO;
import com.jzo2o.api.customer.dto.request.ScoreItem;
import com.jzo2o.api.orders.OrdersApi;
import com.jzo2o.api.orders.OrdersServeApi;
import com.jzo2o.api.orders.dto.response.OrderResDTO;
import com.jzo2o.api.orders.dto.response.ServeProviderIdResDTO;
import com.jzo2o.common.constants.UserType;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.customer.client.EvaluationHttpClient;
import com.jzo2o.customer.model.domain.CommonUser;
import com.jzo2o.customer.model.domain.ServeProvider;
import com.jzo2o.customer.model.dto.EvaluationSubmitDTO;
import com.jzo2o.customer.model.dto.RelationScoreDTO;
import com.jzo2o.customer.model.dto.request.AuditReqDTO;
import com.jzo2o.customer.model.dto.request.EvaluationPageByTargetReqDTO;
import com.jzo2o.customer.model.dto.request.LikeOrCancelReqDTO;
import com.jzo2o.customer.model.dto.response.*;
import com.jzo2o.customer.properties.EvaluationProperties;
import com.jzo2o.customer.service.EvaluationService;
import com.jzo2o.customer.service.ICommonUserService;
import com.jzo2o.customer.service.IServeProviderService;
import com.jzo2o.mvc.utils.UserContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评价服务 - 业务层
 *
 * @author itcast
 * @create 2023/9/11 16:19
 **/
@Service
public class EvaluationServiceImpl implements EvaluationService {
    @Resource
    private EvaluationHttpClient evaluationHttpClient;
    @Resource
    private EvaluationProperties evaluationProperties;
    @Resource
    private OrdersApi ordersApi;
    @Resource
    private OrdersServeApi ordersServeApi;
    @Resource
    private IServeProviderService serveProviderService;
    @Resource
    private ICommonUserService commonUserService;

    /**
     * 默认评价内容
     */
    private static final String DEFAULT_EVALUATION = "此用户没有填写评价，系统默认好评";

    /**
     * 默认匿名状态，0：不匿名，1：匿名
     */
    private static final Integer DEFAULT_ANONYMOUS_STATUS = 1;

    /**
     * 发表评价
     *
     * @param evaluationSubmitReqDTO 发表评价请求体
     */
    @Override
    public BooleanResDTO submit(EvaluationSubmitReqDTO evaluationSubmitReqDTO) {
        //校验订单状态
        OrderResDTO orderResDTO = ordersApi.queryById(evaluationSubmitReqDTO.getOrdersId());
        if (ObjectUtil.notEqual(orderResDTO.getOrdersStatus(), 400)) {
            throw new ForbiddenOperationException("非待评价状态不可评价");
        }

        CurrentUserInfo currentUserInfo = evaluationSubmitReqDTO.getCurrentUserInfo();


        //发送服务项评价
        EvaluationResDTO serveItemEvaluationResDTO = queryByTargetTypeIdAndOrdersId(UserContext.currentUser(), evaluationProperties.getServeItem().getTargetTypeId(), evaluationSubmitReqDTO.getOrdersId());
        //针对该订单如果没有发表过该服务的评价才进行提交数据
        if (null == serveItemEvaluationResDTO) {
            requestSubmitServeItemEvaluation(evaluationSubmitReqDTO, currentUserInfo);
        }

        //发送服务人员/机构评价
        EvaluationResDTO serveProviderEvaluationResDTO = queryByTargetTypeIdAndOrdersId(UserContext.currentUser(), evaluationProperties.getServeProvider().getTargetTypeId(), evaluationSubmitReqDTO.getOrdersId());
        //针对该订单如果没有发表过该师傅的评价才进行提交数据
        if (null == serveProviderEvaluationResDTO) {
            requestSubmitServeProviderEvaluation(evaluationSubmitReqDTO, currentUserInfo);
        }

        //根据订单id评价
        ordersApi.evaluate(evaluationSubmitReqDTO.getOrdersId());

        return new BooleanResDTO(true);
    }

    /**
     * 请求发送服务项评价
     *
     * @param evaluationSubmitReqDTO 评价请求数据
     * @param currentUserInfo        当前用户数据
     */
    private void requestSubmitServeItemEvaluation(EvaluationSubmitReqDTO evaluationSubmitReqDTO, CurrentUserInfo currentUserInfo) {

        //发表评价url
        String url = evaluationProperties.getDomain() + "/common/evaluations";

        //查询订单
        OrderResDTO orderResDTO = ordersApi.queryById(evaluationSubmitReqDTO.getOrdersId());

        //组装评价数据
        EvaluationSubmitDTO evaluationSubmitDTO = new EvaluationSubmitDTO();
        evaluationSubmitDTO.setRelationId(orderResDTO.getId().toString());
        evaluationSubmitDTO.setTargetId(orderResDTO.getServeItemId().toString());
        evaluationSubmitDTO.setTargetName(orderResDTO.getServeItemName());
        evaluationSubmitDTO.setIsAnonymous(evaluationSubmitReqDTO.getIsAnonymous());
        evaluationSubmitDTO.setContent(evaluationSubmitReqDTO.getServeItemEvaluationContent());
        evaluationSubmitDTO.setPictureArray(evaluationSubmitReqDTO.getServeItemPictureArray());

        //组装评分数据
        evaluationSubmitDTO.setScoreArray(evaluationSubmitReqDTO.getServeItemScoreItems());

        //组装请求参数
        Map<String, Object> serveItemEvaluationParametersMap = new HashMap<>();
        serveItemEvaluationParametersMap.put("targetTypeId", evaluationProperties.getServeItem().getTargetTypeId());

        //发送请求
        evaluationHttpClient.post(currentUserInfo, url, serveItemEvaluationParametersMap, evaluationSubmitDTO);
    }

    /**
     * 请求发送服务人员/机构评价
     *
     * @param evaluationSubmitReqDTO 评价请求数据
     * @param currentUserInfo        当前用户数据
     */
    private void requestSubmitServeProviderEvaluation(EvaluationSubmitReqDTO evaluationSubmitReqDTO, CurrentUserInfo currentUserInfo) {
        //发表评价url
        String url = evaluationProperties.getDomain() + "/common/evaluations";

        //查询服务人员/机构
        ServeProviderIdResDTO serveProviderIdResDTO = ordersServeApi.queryServeProviderIdByOrderId(evaluationSubmitReqDTO.getOrdersId());
        ServeProvider serveProvider = serveProviderService.getById(serveProviderIdResDTO.getServeProviderId());

        //组装评价数据
        EvaluationSubmitDTO evaluationSubmitDTO = new EvaluationSubmitDTO();
        evaluationSubmitDTO.setRelationId(evaluationSubmitReqDTO.getOrdersId().toString());
        evaluationSubmitDTO.setTargetId(serveProvider.getId().toString());
        evaluationSubmitDTO.setTargetName(serveProvider.getName());
        evaluationSubmitDTO.setIsAnonymous(evaluationSubmitReqDTO.getIsAnonymous());
        evaluationSubmitDTO.setContent(evaluationSubmitReqDTO.getServeProviderEvaluationContent());

        //组装评分数据
        evaluationSubmitDTO.setScoreArray(evaluationSubmitReqDTO.getServeProviderScoreItems());

        //组装请求参数
        Map<String, Object> serveItemEvaluationParametersMap = new HashMap<>();
        serveItemEvaluationParametersMap.put("targetTypeId", evaluationProperties.getServeProvider().getTargetTypeId());

        //发送请求
        evaluationHttpClient.post(currentUserInfo, url, serveItemEvaluationParametersMap, evaluationSubmitDTO);
    }

    /**
     * 根据对象属性分页查询评价列表
     *
     * @param evaluationPageByTargetReqDTO 分页查询请求体
     * @return 分页结果
     */
    @Override
    public List<EvaluationResDTO> pageByTarget(EvaluationPageByTargetReqDTO evaluationPageByTargetReqDTO) {
        CurrentUserInfo currentUserInfo = UserContext.currentUser();

        //如果是用户端请求，查询的评价为服务项评价
        if (ObjectUtil.equal(UserType.C_USER, currentUserInfo.getUserType())) {
            evaluationPageByTargetReqDTO.setTargetTypeId(evaluationProperties.getServeItem().getTargetTypeId());
        }

        //请求url
        String url = evaluationProperties.getDomain() + "/common/evaluations/pageByTargetId";

        //封装请求参数
        Map<String, Object> parameterMap = BeanUtil.beanToMap(evaluationPageByTargetReqDTO);
        //只查询好评
        parameterMap.put("scoreLevel", 3);

        //向评价系统发送http请求
        String jsonResult = evaluationHttpClient.get(currentUserInfo, url, parameterMap);

        //解析响应结果
        return JSONUtil.parseObj(jsonResult).getJSONObject("data").getJSONArray("list").toList(EvaluationResDTO.class);
    }

    /**
     * 删除评价
     *
     * @param id 评价id
     */
    @Override
    public void delete(String id) {
        //请求url
        String url = evaluationProperties.getDomain() + "/common/evaluations/" + id;

        //封装请求参数
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("targetTypeId", evaluationProperties.getServeItem().getTargetTypeId());

        //向评价系统发送http请求
        evaluationHttpClient.delete(UserContext.currentUser(), url, parameterMap);
    }

    /**
     * 点赞或取消点赞
     *
     * @param likeOrCancelReqDTO 点赞请求数据
     */
    @Override
    public void likeOrCancel(LikeOrCancelReqDTO likeOrCancelReqDTO) {
        //请求url
        String url = evaluationProperties.getDomain() + "/common/likes";

        //封装请求参数
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("targetTypeId", evaluationProperties.getServeItem().getTargetTypeId());

        //向评价系统发送http请求
        evaluationHttpClient.post(UserContext.currentUser(), url, parameterMap, likeOrCancelReqDTO);
    }

    /**
     * 用户举报
     *
     * @param auditReqDTO 举报请求
     */
    @Override
    public void userReport(AuditReqDTO auditReqDTO) {
        //请求url
        String url = evaluationProperties.getDomain() + "/common/audit";

        //组装请求体
        auditReqDTO.setTargetTypeId(evaluationProperties.getServeItem().getTargetTypeId());

        //向评价系统发送http请求
        evaluationHttpClient.post(UserContext.currentUser(), url, null, auditReqDTO);
    }

    /**
     * 分页查询当前用户评价列表
     *
     * @param pageNo   页码，默认为1
     * @param pageSize 页面大小，默认为10
     * @return 当前用户评价列表
     */
    @Override
    public List<EvaluationAndOrdersResDTO> pageByCurrentUser(Integer pageNo, Integer pageSize) {
        //请求url
        String url = evaluationProperties.getDomain() + "/common/evaluations/pageByEvaluatorId";

        //封装请求参数
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("pageNo", pageNo);
        parameterMap.put("pageSize", pageSize);
        parameterMap.put("targetTypeId", evaluationProperties.getServeItem().getTargetTypeId());
        parameterMap.put("evaluatorId", UserContext.currentUserId());

        //向评价系统发送http请求
        String jsonResult = evaluationHttpClient.get(UserContext.currentUser(), url, parameterMap);

        //解析响应数据
        if (ObjectUtil.isEmpty(JSONUtil.parseObj(jsonResult).getJSONObject("data").getJSONArray("list"))) {
            return Collections.emptyList();
        }
        List<EvaluationAndOrdersResDTO> list = JSONUtil.parseObj(jsonResult).getJSONObject("data").getJSONArray("list").toList(EvaluationAndOrdersResDTO.class);

        //筛选出订单id，批量查询订单详情
        List<Long> orderIds = list.stream().map(e -> Long.valueOf(e.getRelationId())).distinct().collect(Collectors.toList());
        List<OrderResDTO> orderResDTOList = ordersApi.queryByIds(orderIds);
        Map<Long, OrderResDTO> ordersMap = orderResDTOList.stream().collect(Collectors.toMap(OrderResDTO::getId, o -> o));

        //组装订单数据
        list.forEach(e -> {
            Long ordersId = Long.valueOf(e.getRelationId());
            e.setServeStartTime(ordersMap.get(ordersId).getServeStartTime());
            e.setServeAddress(ordersMap.get(ordersId).getServeAddress());
            e.setServeItemImg(ordersMap.get(ordersId).getServeItemImg());
        });
        return list;
    }


    /**
     * 根据评价等级分页查询当前用户评价列表
     *
     * @param scoreLevel 评价等级，1差评，2中评，3好评
     * @param pageNo     页码
     * @param pageSize   页面大小
     * @return 评价列表
     */
    @Override
    public List<EvaluationAndOrdersResDTO> pageByCurrentUserAndScoreLevel(Integer scoreLevel, Integer pageNo, Integer pageSize) {
        //请求url
        String url = evaluationProperties.getDomain() + "/common/evaluations/pageByTargetId";

        //封装请求参数
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("pageNo", pageNo);
        parameterMap.put("pageSize", pageSize);
        parameterMap.put("targetTypeId", evaluationProperties.getServeProvider().getTargetTypeId());
        parameterMap.put("targetId", UserContext.currentUserId());
        parameterMap.put("sortBy", 1);  //固定按评价时间排序

        if (ObjectUtil.isNotEmpty(scoreLevel)) {
            parameterMap.put("scoreLevel", scoreLevel);
        }

        //向评价系统发送http请求
        String jsonResult = evaluationHttpClient.get(UserContext.currentUser(), url, parameterMap);

        //解析响应数据
        List<EvaluationAndOrdersResDTO> list = JSONUtil.parseObj(jsonResult).getJSONObject("data").getJSONArray("list").toList(EvaluationAndOrdersResDTO.class);
        if (CollUtil.isEmpty(list)) {
            return list;
        }

        //筛选出订单id，批量查询订单详情
        List<Long> orderIds = list.stream().map(e -> Long.valueOf(e.getRelationId())).distinct().collect(Collectors.toList());
        List<OrderResDTO> orderResDTOList = ordersApi.queryByIds(orderIds);
        Map<Long, OrderResDTO> ordersMap = orderResDTOList.stream().collect(Collectors.toMap(OrderResDTO::getId, o -> o));

        //组装订单数据
        list.forEach(e -> {
            Long ordersId = Long.valueOf(e.getRelationId());
            e.setServeStartTime(ordersMap.get(ordersId).getServeStartTime());
            e.setServeAddress(ordersMap.get(ordersId).getServeAddress());
            e.setServeItemImg(ordersMap.get(ordersId).getServeItemImg());
        });
        return list;
    }

    /**
     * 根据订单id列表查询师傅评分
     *
     * @param orderIds 订单id列表
     * @return 评分
     */
    @Override
    public Map<String, Double> queryServeProviderScoreByOrdersId(List<Long> orderIds) {
        //请求url
        String url = evaluationProperties.getDomain() + "/admin/evaluations/findScoreByRelationIds";

        //封装请求参数
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("targetTypeId", evaluationProperties.getServeProvider().getTargetTypeId());
        List<String> ordersIdStr = orderIds.stream().map(String::valueOf).collect(Collectors.toList());
        parameterMap.put("relationIds", String.join(",", ordersIdStr));

        //向评价系统发送http请求
        CurrentUserInfo currentUserInfo = new CurrentUserInfo(1L, "", "", UserType.OPERATION);
        String jsonResult = evaluationHttpClient.get(currentUserInfo, url, parameterMap);

        List<RelationScoreDTO> scoreList = JSONUtil.parseObj(jsonResult).getJSONArray("data").toList(RelationScoreDTO.class);
        //解析响应数据

        return scoreList.stream().collect(Collectors.toMap(RelationScoreDTO::getRelationId, RelationScoreDTO::getScore));
    }

    /**
     * 获取评价系统信息
     *
     * @return 评价系统信息
     */
    @Override
    public EvaluationTokenDto getEvaluationInfo() {
        return new EvaluationTokenDto(evaluationHttpClient.generateToken(UserContext.currentUser()));
    }

    /**
     * 根据对象类型和订单id查询评价
     *
     * @param targetTypeId 对象类型id
     * @param ordersId     订单id
     * @return 评价
     */
    @Override
    public EvaluationResDTO queryByTargetTypeIdAndOrdersId(CurrentUserInfo currentUserInfo, String targetTypeId, Long ordersId) {
        //请求url
        String url = evaluationProperties.getDomain() + "/common/evaluations/findByRelationId";

        //封装请求参数
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("targetTypeId", targetTypeId);
        parameterMap.put("relationId", ordersId.toString());

        //向评价系统发送http请求
        String jsonResult = evaluationHttpClient.get(currentUserInfo, url, parameterMap);

        //解析响应数据
        JSONObject jsonObject = JSONUtil.parseObj(jsonResult).getJSONObject("data");
        if (null == jsonObject) {
            return null;
        }
        return jsonObject.toBean(EvaluationResDTO.class);
    }

    /**
     * 根据对象类型id查询评价配置信息
     *
     * @return 评价配置信息
     */
    private EvaluationSystemInfoResDTO findSystemInfo(String targetTypeId, CurrentUserInfo currentUserInfo) {
        //请求url
        String url = evaluationProperties.getDomain() + "/common/systems/info";

        //封装请求参数
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("targetTypeId", targetTypeId);

        //向评价系统发送http请求
        String jsonResult = evaluationHttpClient.get(currentUserInfo, url, parameterMap);

        //解析响应数据
        JSONObject jsonObject = JSONUtil.parseObj(jsonResult).getJSONObject("data");
        if (null == jsonObject) {
            throw new ForbiddenOperationException("查询不到配置信息");
        }
        return jsonObject.toBean(EvaluationSystemInfoResDTO.class);
    }

    /**
     * 查询所有的评价配置信息
     *
     * @return 评价配置信息
     */
    @Override
    @Cacheable(value = "JZ_CACHE", key = "'ALL_EVALUATION_CONFIG'", cacheManager = "cacheManagerOneDay")
    public AllEvaluationSystemInfoResDTO findAllSystemInfo(CurrentUserInfo currentUserInfo) {
        //查询服务项配置信息
        EvaluationSystemInfoResDTO serveItemSystemInfo = findSystemInfo(evaluationProperties.getServeItem().getTargetTypeId(), currentUserInfo);

        //查询师傅配置信息
        EvaluationSystemInfoResDTO serveProviderSystemInfo = findSystemInfo(evaluationProperties.getServeProvider().getTargetTypeId(), currentUserInfo);
        if (null == serveItemSystemInfo || null == serveProviderSystemInfo) {
            throw new ForbiddenOperationException("查询不到配置信息");
        }
        return new AllEvaluationSystemInfoResDTO(serveItemSystemInfo, serveProviderSystemInfo);
    }

    /**
     * 自动评价
     *
     * @param evaluationSubmitReqDTO 评价信息
     */
    @Override
    public void autoEvaluate(EvaluationSubmitReqDTO evaluationSubmitReqDTO) {
        //1.查询当前用户信息
        CommonUser commonUser = commonUserService.getById(evaluationSubmitReqDTO.getUserId());
        CurrentUserInfo currentUserInfo = new CurrentUserInfo(commonUser.getId(), commonUser.getNickname(), commonUser.getAvatar(), UserType.C_USER);

        //2.获取配置信息
        AllEvaluationSystemInfoResDTO allSystemInfo = findAllSystemInfo(currentUserInfo);

        //3.封装服务项默认评分数据
        List<EvaluationSystemInfoResDTO.ScoreConfig> serveItemScoreConfigList = allSystemInfo.getServeItemEvaluationSystemInfo().getEvaluationConfig().getScoreConfigList();
        List<ScoreItem> serveItemScoreList = serveItemScoreConfigList.stream()
                .filter(EvaluationSystemInfoResDTO.ScoreConfig::getEnabled)
                .map(s -> ScoreItem.defaultScoreItem(s.getItemId(), s.getItemName()))
                .collect(Collectors.toList());

        //4.封装师傅默认评分数据
        List<EvaluationSystemInfoResDTO.ScoreConfig> serveProviderScoreConfigList = allSystemInfo.getServeProviderEvaluationSystemInfo().getEvaluationConfig().getScoreConfigList();
        List<ScoreItem> serveProviderScoreList = serveProviderScoreConfigList.stream()
                .filter(EvaluationSystemInfoResDTO.ScoreConfig::getEnabled)
                .map(s -> ScoreItem.defaultScoreItem(s.getItemId(), s.getItemName()))
                .collect(Collectors.toList());


        //5.封装默认评价内容、默认评分、默认匿名信息
        evaluationSubmitReqDTO.setServeItemEvaluationContent(DEFAULT_EVALUATION);
        evaluationSubmitReqDTO.setServeItemScoreItems(ArrayUtil.toArray(serveItemScoreList, ScoreItem.class));
        evaluationSubmitReqDTO.setServeProviderEvaluationContent(DEFAULT_EVALUATION);
        evaluationSubmitReqDTO.setServeProviderScoreItems(ArrayUtil.toArray(serveProviderScoreList, ScoreItem.class));
        evaluationSubmitReqDTO.setIsAnonymous(DEFAULT_ANONYMOUS_STATUS);

        //6.发送服务项评价
        EvaluationResDTO serveItemEvaluationResDTO = queryByTargetTypeIdAndOrdersId(currentUserInfo, evaluationProperties.getServeItem().getTargetTypeId(), evaluationSubmitReqDTO.getOrdersId());
        //针对该订单如果没有发表过该服务的评价才进行提交数据
        if (null == serveItemEvaluationResDTO) {
            requestSubmitServeItemEvaluation(evaluationSubmitReqDTO, currentUserInfo);
        }

        //7.发送服务人员/机构评价
        EvaluationResDTO serveProviderEvaluationResDTO = queryByTargetTypeIdAndOrdersId(currentUserInfo, evaluationProperties.getServeProvider().getTargetTypeId(), evaluationSubmitReqDTO.getOrdersId());
        //针对该订单如果没有发表过该师傅的评价才进行提交数据
        if (null == serveProviderEvaluationResDTO) {
            requestSubmitServeProviderEvaluation(evaluationSubmitReqDTO, currentUserInfo);
        }
    }
}
