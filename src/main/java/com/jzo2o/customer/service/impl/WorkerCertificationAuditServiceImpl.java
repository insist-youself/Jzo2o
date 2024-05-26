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
import com.jzo2o.customer.mapper.WorkerCertificationAuditMapper;
import com.jzo2o.customer.mapper.WorkerCertificationMapper;
import com.jzo2o.customer.model.domain.WorkerCertification;
import com.jzo2o.customer.model.domain.WorkerCertificationAudit;
import com.jzo2o.customer.model.dto.request.CertificationAuditReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditAddReqDTO;
import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditPageQueryReqDTO;
import com.jzo2o.customer.model.dto.response.RejectReasonResDTO;
import com.jzo2o.customer.model.dto.response.WorkerCertificationAuditResDTO;
import com.jzo2o.customer.service.IWorkerCertificationAuditService;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.mysql.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务人员认证审核信息表 服务实现类
 * </p>
 *
 * @author linger
 * @since 2024-05-19
 */
@Service
public class WorkerCertificationAuditServiceImpl extends ServiceImpl<WorkerCertificationAuditMapper, WorkerCertificationAudit> implements IWorkerCertificationAuditService {

    @Resource
    private WorkerCertificationMapper workerCertificationMapper;

    @Override
    @Transactional
    public void submitCertification(WorkerCertificationAuditAddReqDTO workerCertificationAuditAddReqDTO) {
        // 1.校验请求信息
        if (ObjectUtil.isNull(workerCertificationAuditAddReqDTO)) {
            throw new ForbiddenOperationException("认证申请请求不能为空");
        }
        // 2.查询当前用户对应的认证信息是否存在
        WorkerCertificationAudit workerCertificationAudit1 = lambdaQuery()
                .eq(WorkerCertificationAudit::getServeProviderId, workerCertificationAuditAddReqDTO.getServeProviderId())
                .one();
        WorkerCertificationAudit workerCertificationAudit = BeanUtil
                .copyProperties(workerCertificationAuditAddReqDTO, WorkerCertificationAudit.class);
        // 设置认证状态、审核状态的初始值
        workerCertificationAudit.setCertificationStatus(CertificationStatusEnum.PROGRESSING.getStatus());
        workerCertificationAudit.setAuditStatus(AuditStatusEnum.Unaudited.getStatus());
        if (ObjectUtil.isNotNull(workerCertificationAudit1)) {
            // 如果对应认证信息已经存在，则执行删除操作
            baseMapper.deleteById(workerCertificationAudit1.getId());
        }
        // 3.执行认证信息插入
        baseMapper.insert(workerCertificationAudit);
        WorkerCertification workerCertification = workerCertificationMapper.selectById(workerCertificationAudit.getServeProviderId());
        if (ObjectUtil.isNotNull(workerCertification)) {
            workerCertificationMapper.deleteById(workerCertification.getId());
        }
        workerCertification = BeanUtil.copyProperties(workerCertificationAudit, WorkerCertification.class);
        //插入前设置 对应id
        workerCertification.setId(workerCertificationAudit.getServeProviderId());
        workerCertificationMapper.insert(workerCertification);
    }

    @Override
    public RejectReasonResDTO rejectReason(Long serveProviderId) {
        //查询对应的驳回原因
        WorkerCertificationAudit workerCertificationAudit = lambdaQuery()
                .eq(WorkerCertificationAudit::getServeProviderId, serveProviderId)
                .one();
        RejectReasonResDTO rejectReasonResDTO = BeanUtil
                .copyProperties(workerCertificationAudit, RejectReasonResDTO.class);
        return rejectReasonResDTO;
    }

    /**
     * 分页查询
     *
     * @param workerCertificationAuditPageQueryReqDTO 分页查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<WorkerCertificationAuditResDTO> pageQuery(WorkerCertificationAuditPageQueryReqDTO workerCertificationAuditPageQueryReqDTO) {
        Page<WorkerCertificationAudit> page = PageUtils.parsePageQuery(workerCertificationAuditPageQueryReqDTO, WorkerCertificationAudit.class);
        LambdaQueryWrapper<WorkerCertificationAudit> queryWrapper = Wrappers.<WorkerCertificationAudit>lambdaQuery()
                .like(ObjectUtil.isNotEmpty(workerCertificationAuditPageQueryReqDTO.getName()), WorkerCertificationAudit::getName, workerCertificationAuditPageQueryReqDTO.getName())
                .eq(ObjectUtil.isNotEmpty(workerCertificationAuditPageQueryReqDTO.getIdCardNo()), WorkerCertificationAudit::getIdCardNo, workerCertificationAuditPageQueryReqDTO.getIdCardNo())
                .eq(ObjectUtil.isNotEmpty(workerCertificationAuditPageQueryReqDTO.getAuditStatus()), WorkerCertificationAudit::getAuditStatus, workerCertificationAuditPageQueryReqDTO.getAuditStatus())
                .eq(ObjectUtil.isNotEmpty(workerCertificationAuditPageQueryReqDTO.getCertificationStatus()), WorkerCertificationAudit::getCertificationStatus, workerCertificationAuditPageQueryReqDTO.getCertificationStatus());
        Page<WorkerCertificationAudit> result = baseMapper.selectPage(page, queryWrapper);
        return PageUtils.toPage(result, WorkerCertificationAuditResDTO.class);
    }

    @Override
    @Transactional
    public void auditById(Long id, CertificationAuditReqDTO certificationAuditReqDTO) {
        WorkerCertificationAudit workerCertificationAudit = lambdaQuery()
                .eq(WorkerCertificationAudit::getId, id)
                .one();
        if (ObjectUtil.isNull(workerCertificationAudit)) {
            throw new ForbiddenOperationException("不存在该服务人员的认证信息，无法进行审核");
        }
        workerCertificationAudit.setCertificationStatus(certificationAuditReqDTO.getCertificationStatus());
        workerCertificationAudit.setRejectReason(certificationAuditReqDTO.getRejectReason());
        CurrentUserInfo currentUserInfo = UserContext.currentUser();
        workerCertificationAudit.setAuditTime(LocalDateTime.now());
        workerCertificationAudit.setAuditorId(currentUserInfo.getId());
        workerCertificationAudit.setAuditorName(currentUserInfo.getName());
        workerCertificationAudit.setAuditStatus(AuditStatusEnum.Audited.getStatus());
        boolean update = updateById(workerCertificationAudit);
        if (!update) {
            throw new CommonException("审核认证信息失败");
        }
        // 审核之后将对应的服务人员认证信息插入work_certification表中
        WorkerCertification workerCertification = workerCertificationMapper.selectById(workerCertificationAudit.getServeProviderId());
        if (ObjectUtil.isNull(workerCertification)) {
            throw new ForbiddenOperationException("请求错误, 对应的服务人员认证信息未存在!");
        }
        workerCertification = BeanUtil.copyProperties(workerCertificationAudit, WorkerCertification.class);
        workerCertification.setCertificationTime(LocalDateTime.now());
        workerCertification.setId(workerCertificationAudit.getServeProviderId());
        workerCertificationMapper.updateById(workerCertification);
    }
}
