package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.enums.EnableStatusEnum;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.enums.CertificationStatusEnum;
import com.jzo2o.customer.mapper.AgencyCertificationAuditMapper;
import com.jzo2o.customer.model.domain.AgencyCertification;
import com.jzo2o.customer.model.domain.AgencyCertificationAudit;
import com.jzo2o.customer.model.domain.WorkerCertification;
import com.jzo2o.customer.model.dto.AgencyCertificationUpdateDTO;
import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.request.AgencyCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.response.AgencyCertificationAuditResDTO;
import com.jzo2o.customer.model.dto.response.RejectReasonResDTO;
import com.jzo2o.customer.service.IAgencyCertificationAuditService;
import com.jzo2o.customer.service.IAgencyCertificationService;
import com.jzo2o.customer.service.IServeProviderService;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.mysql.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 机构认证审核表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-09-06
 */
@Service
public class AgencyCertificationAuditServiceImpl extends ServiceImpl<AgencyCertificationAuditMapper, AgencyCertificationAudit> implements IAgencyCertificationAuditService {
    @Resource
    private IAgencyCertificationService agencyCertificationService;
    @Resource
    private IServeProviderService serveProviderService;

    /**
     * 机构申请资质认证
     *
     * @param agencyCertificationAuditAddReqDTO 认证申请请求体
     */
    @Override
    @Transactional
    public void applyCertification(AgencyCertificationAuditAddReqDTO agencyCertificationAuditAddReqDTO) {
        //1.新增申请资质认证记录
        AgencyCertificationAudit agencyCertificationAudit = BeanUtil.toBean(agencyCertificationAuditAddReqDTO, AgencyCertificationAudit.class);
        baseMapper.insert(agencyCertificationAudit);
        //查询认证记录
        Long serveProviderId = agencyCertificationAuditAddReqDTO.getServeProviderId();
        AgencyCertification agencyCertification = agencyCertificationService.getById(serveProviderId);

        if(ObjectUtil.isNotNull(agencyCertification)){
            //2.将认证信息状态更新为认证中
            agencyCertification.setCertificationStatus(CertificationStatusEnum.PROGRESSING.getStatus());//认证中
            agencyCertificationService.updateById(agencyCertification);
        }else{
            agencyCertification = new AgencyCertification();
            agencyCertification.setId(serveProviderId);
            agencyCertification.setCertificationStatus(CertificationStatusEnum.PROGRESSING.getStatus());//认证中
            agencyCertificationService.save(agencyCertification);
        }

    }

    /**
     * 审核认证信息
     *
     * @param id                       申请记录id
     * @param certificationAuditReqDTO 审核请求
     */
    @Override
    @Transactional
    public void auditCertification(Long id, CertificationAuditReqDTO certificationAuditReqDTO) {
        //1.更新申请记录
        CurrentUserInfo currentUserInfo = UserContext.currentUser();
        LambdaUpdateWrapper<AgencyCertificationAudit> updateWrapper = Wrappers.<AgencyCertificationAudit>lambdaUpdate()
                .eq(AgencyCertificationAudit::getId, id)
                .set(AgencyCertificationAudit::getAuditStatus, EnableStatusEnum.ENABLE.getStatus())
                .set(AgencyCertificationAudit::getAuditorId, currentUserInfo.getId())
                .set(AgencyCertificationAudit::getAuditorName, currentUserInfo.getName())
                .set(AgencyCertificationAudit::getAuditTime, LocalDateTime.now())
                .set(AgencyCertificationAudit::getCertificationStatus, certificationAuditReqDTO.getCertificationStatus())
                .set(ObjectUtil.isNotEmpty(certificationAuditReqDTO.getRejectReason()), AgencyCertificationAudit::getRejectReason, certificationAuditReqDTO.getRejectReason());
        super.update(updateWrapper);

        //2.更新认证信息，如果认证成功，需要将各认证属性也更新
        AgencyCertificationAudit agencyCertificationAudit = baseMapper.selectById(id);
        AgencyCertificationUpdateDTO agencyCertificationUpdateDTO = new AgencyCertificationUpdateDTO();
        agencyCertificationUpdateDTO.setId(agencyCertificationAudit.getServeProviderId());
        agencyCertificationUpdateDTO.setCertificationStatus(certificationAuditReqDTO.getCertificationStatus());
        if (ObjectUtil.equal(CertificationStatusEnum.SUCCESS.getStatus(), certificationAuditReqDTO.getCertificationStatus())) {
            //如果认证成功，需要更新服务人员/机构名称
            serveProviderService.updateNameById(agencyCertificationAudit.getServeProviderId(), agencyCertificationAudit.getName());

            agencyCertificationUpdateDTO.setName(agencyCertificationAudit.getName());
            agencyCertificationUpdateDTO.setIdNumber(agencyCertificationAudit.getIdNumber());
            agencyCertificationUpdateDTO.setLegalPersonName(agencyCertificationAudit.getLegalPersonName());
            agencyCertificationUpdateDTO.setLegalPersonIdCardNo(agencyCertificationAudit.getLegalPersonIdCardNo());
            agencyCertificationUpdateDTO.setBusinessLicense(agencyCertificationAudit.getBusinessLicense());
            agencyCertificationUpdateDTO.setCertificationTime(agencyCertificationAudit.getAuditTime());
        }
        agencyCertificationService.updateByServeProviderId(agencyCertificationUpdateDTO);
    }

    /**
     * 分页查询
     *
     * @param agencyCertificationAuditPageQueryReqDTO 分页查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<AgencyCertificationAuditResDTO> pageQuery(AgencyCertificationAuditPageQueryReqDTO agencyCertificationAuditPageQueryReqDTO) {
        Page<AgencyCertificationAudit> page = PageUtils.parsePageQuery(agencyCertificationAuditPageQueryReqDTO, AgencyCertificationAudit.class);
        LambdaQueryWrapper<AgencyCertificationAudit> queryWrapper = Wrappers.<AgencyCertificationAudit>lambdaQuery()
                .like(ObjectUtil.isNotEmpty(agencyCertificationAuditPageQueryReqDTO.getName()), AgencyCertificationAudit::getName, agencyCertificationAuditPageQueryReqDTO.getName())
                .like(ObjectUtil.isNotEmpty(agencyCertificationAuditPageQueryReqDTO.getLegalPersonName()), AgencyCertificationAudit::getLegalPersonName, agencyCertificationAuditPageQueryReqDTO.getLegalPersonName())
                .eq(ObjectUtil.isNotEmpty(agencyCertificationAuditPageQueryReqDTO.getAuditStatus()), AgencyCertificationAudit::getAuditStatus, agencyCertificationAuditPageQueryReqDTO.getAuditStatus())
                .eq(ObjectUtil.isNotEmpty(agencyCertificationAuditPageQueryReqDTO.getCertificationStatus()), AgencyCertificationAudit::getCertificationStatus, agencyCertificationAuditPageQueryReqDTO.getCertificationStatus());
        Page<AgencyCertificationAudit> result = baseMapper.selectPage(page, queryWrapper);
        return PageUtils.toPage(result, AgencyCertificationAuditResDTO.class);
    }

    /**
     * 查询当前用户最近驳回原因
     *
     * @return 驳回原因
     */
    @Override
    public RejectReasonResDTO queryCurrentUserLastRejectReason() {
        LambdaQueryWrapper<AgencyCertificationAudit> queryWrapper = Wrappers.<AgencyCertificationAudit>lambdaQuery()
                .eq(AgencyCertificationAudit::getServeProviderId, UserContext.currentUserId())
                .orderByDesc(AgencyCertificationAudit::getCreateTime)
                .last("limit 1");
        AgencyCertificationAudit agencyCertificationAudit = baseMapper.selectOne(queryWrapper);
        return new RejectReasonResDTO(agencyCertificationAudit.getRejectReason());
    }
}
