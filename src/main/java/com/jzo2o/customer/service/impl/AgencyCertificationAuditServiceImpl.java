package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.enums.AuditStatusEnum;
import com.jzo2o.customer.enums.CertificationStatusEnum;
import com.jzo2o.customer.mapper.AgencyCertificationAuditMapper;
import com.jzo2o.customer.mapper.AgencyCertificationMapper;
import com.jzo2o.customer.model.domain.AgencyCertification;
import com.jzo2o.customer.model.domain.AgencyCertificationAudit;
import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.response.AgencyCertificationAuditResDTO;
import com.jzo2o.customer.model.dto.response.RejectReasonResDTO;
import com.jzo2o.customer.service.IAgencyCertificationAuditService;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.mysql.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 机构认证审核信息表 服务实现类
 * </p>
 *
 * @author linger
 * @since 2024-05-19
 */
@Service
public class AgencyCertificationAuditServiceImpl extends ServiceImpl<AgencyCertificationAuditMapper, AgencyCertificationAudit> implements IAgencyCertificationAuditService {

    @Resource
    private AgencyCertificationMapper agencyCertificationMapper;

    @Override
    @Transactional
    public void submitCertification(AgencyCertificationAuditAddReqDTO agencyCertificationAuditAddReqDTO) {
        // 1.校验请求信息
        if (ObjectUtil.isNull(agencyCertificationAuditAddReqDTO)) {
            throw new ForbiddenOperationException("认证申请请求不能为空");
        }
        // 2.查询当前用户对应的认证信息是否存在
        AgencyCertificationAudit agencyCertificationAudit1 = lambdaQuery()
                .eq(AgencyCertificationAudit::getServeProviderId, agencyCertificationAuditAddReqDTO.getServeProviderId())
                .one();
        AgencyCertificationAudit agencyCertificationAudit = BeanUtil
                .copyProperties(agencyCertificationAuditAddReqDTO, AgencyCertificationAudit.class);
        // 设置认证状态、审核状态的初始值
        agencyCertificationAudit.setCertificationStatus(CertificationStatusEnum.PROGRESSING.getStatus());
        agencyCertificationAudit.setAuditStatus(AuditStatusEnum.Unaudited.getStatus());
        if (ObjectUtil.isNotNull(agencyCertificationAudit1)) {
            // 如果对应认证信息已经存在，则先删除
//            agencyCertificationAudit.setId(agencyCertificationAudit1.getId());
//            agencyCertificationAudit.setAuditorId(null);
//            agencyCertificationAudit.setAuditTime(null);
//            agencyCertificationAudit.setAuditorName(null);
//            agencyCertificationAudit.setRejectReason(null);
//            boolean update = updateById(agencyCertificationAudit);
//            if (!update) {
//                throw new CommonException("更新认证信息失败");
//            }
            baseMapper.deleteById(agencyCertificationAudit1.getId());
        }
        // 3.执行认证信息插入
        baseMapper.insert(agencyCertificationAudit);
        // 将对应的机构认证信息插入agency_certification表中
        AgencyCertification agencyCertification = agencyCertificationMapper.selectById(agencyCertificationAudit.getServeProviderId());
        if (ObjectUtil.isNotNull(agencyCertification)) {
//            agencyCertification = BeanUtil.copyProperties(agencyCertificationAudit, AgencyCertification.class);
//            agencyCertification.setCertificationTime(null);
//            agencyCertification.setCertificationStatus(CertificationStatusEnum.PROGRESSING.getStatus());
//            agencyCertification.setId(agencyCertificationAudit.getServeProviderId());
//            agencyCertificationMapper.updateById(agencyCertification);
            agencyCertificationMapper.deleteById(agencyCertification.getId());
        }
        agencyCertification = BeanUtil.copyProperties(agencyCertificationAudit, AgencyCertification.class);
        //插入前设置对应id
        agencyCertification.setId(agencyCertificationAudit.getServeProviderId());
        agencyCertificationMapper.insert(agencyCertification);
    }

    @Override
    public RejectReasonResDTO rejectReason(Long serveProviderId) {
        //查询对应的驳回原因
        AgencyCertificationAudit agencyCertificationAudit = lambdaQuery()
                .eq(AgencyCertificationAudit::getServeProviderId, serveProviderId)
                .one();
        RejectReasonResDTO rejectReasonResDTO = BeanUtil
                .copyProperties(agencyCertificationAudit, RejectReasonResDTO.class);
        return rejectReasonResDTO;
    }

    @Override
    public PageResult<AgencyCertificationAuditResDTO> pageQuery(AgencyCertificationAuditPageQueryReqDTO agencyCertificationAuditPageQueryReqDTO) {
        Page<AgencyCertificationAudit> page = PageUtils.parsePageQuery(agencyCertificationAuditPageQueryReqDTO, AgencyCertificationAudit.class);
        LambdaQueryWrapper<AgencyCertificationAudit> queryWrapper = Wrappers.<AgencyCertificationAudit>lambdaQuery()
                .like(ObjectUtil.isNotEmpty(agencyCertificationAuditPageQueryReqDTO.getName()), AgencyCertificationAudit::getName, agencyCertificationAuditPageQueryReqDTO.getName())
                .eq(ObjectUtil.isNotEmpty(agencyCertificationAuditPageQueryReqDTO.getLegalPersonName()), AgencyCertificationAudit::getLegalPersonName, agencyCertificationAuditPageQueryReqDTO.getLegalPersonName())
                .eq(ObjectUtil.isNotEmpty(agencyCertificationAuditPageQueryReqDTO.getAuditStatus()), AgencyCertificationAudit::getAuditStatus, agencyCertificationAuditPageQueryReqDTO.getAuditStatus())
                .eq(ObjectUtil.isNotEmpty(agencyCertificationAuditPageQueryReqDTO.getCertificationStatus()), AgencyCertificationAudit::getCertificationStatus, agencyCertificationAuditPageQueryReqDTO.getCertificationStatus());
        Page<AgencyCertificationAudit> result = baseMapper.selectPage(page, queryWrapper);
        return PageUtils.toPage(result, AgencyCertificationAuditResDTO.class);
    }

    @Override
    @Transactional
    public void auditById(Long id, CertificationAuditReqDTO certificationAuditReqDTO) {
        AgencyCertificationAudit agencyCertificationAudit = lambdaQuery()
                .eq(AgencyCertificationAudit::getId, id)
                .one();
        if (ObjectUtil.isNull(agencyCertificationAudit)) {
            throw new ForbiddenOperationException("不存在该服务人员的认证信息，无法进行审核");
        }
        agencyCertificationAudit.setCertificationStatus(certificationAuditReqDTO.getCertificationStatus());
        agencyCertificationAudit.setRejectReason(certificationAuditReqDTO.getRejectReason());
        CurrentUserInfo currentUserInfo = UserContext.currentUser();
        agencyCertificationAudit.setAuditTime(LocalDateTime.now());
        agencyCertificationAudit.setAuditorId(currentUserInfo.getId());
        agencyCertificationAudit.setAuditorName(currentUserInfo.getName());
        agencyCertificationAudit.setAuditStatus(AuditStatusEnum.Audited.getStatus());
        boolean update = updateById(agencyCertificationAudit);
        if (!update) {
            throw new CommonException("审核认证信息失败");
        }
        // 审核之后将对应的机构认证信息更新到 agency_certification 表中
        AgencyCertification agencyCertification = agencyCertificationMapper.selectById(agencyCertificationAudit.getServeProviderId());
        if (ObjectUtil.isNull(agencyCertification)) {
            throw new ForbiddenOperationException("请求错误, 对应的机构认证信息未存在!");
        }
        agencyCertification = BeanUtil.copyProperties(agencyCertificationAudit, AgencyCertification.class);
        agencyCertification.setCertificationTime(LocalDateTime.now());
        agencyCertification.setId(agencyCertificationAudit.getServeProviderId());
        agencyCertificationMapper.updateById(agencyCertification);
    }
}
