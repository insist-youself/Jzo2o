package com.jzo2o.customer.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jzo2o.customer.model.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class IServeProviderServiceTest {
    @Resource
    private IServeProviderService serveProviderService;
    @Resource
    private IWorkerCertificationService workerCertificationService;
    @Resource
    private IWorkerCertificationAuditService workerCertificationAuditService;
    @Resource
    private IAgencyCertificationService agencyCertificationService;
    @Resource
    private IAgencyCertificationAuditService agencyCertificationAuditService;

    @Test
    void test() {
        LambdaQueryWrapper<ServeProvider> queryWrapper = Wrappers.<ServeProvider>lambdaQuery()
                .eq(ServeProvider::getType, 2);
        List<ServeProvider> list = serveProviderService.list(queryWrapper);

        String url = "https://yjy-xzbjzfw-oss.oss-cn-hangzhou.aliyuncs.com/e5ce7b4f-81a6-481c-b475-d35996188344.png";

        int i = 0;
        for (ServeProvider serveProvider : list) {
            WorkerCertificationAudit workerCertificationAudit = new WorkerCertificationAudit();
            workerCertificationAudit.setId(IdUtil.getSnowflakeNextId());
            workerCertificationAudit.setServeProviderId(serveProvider.getId());
            workerCertificationAudit.setName("服务人员0" + i);
            workerCertificationAudit.setIdCardNo("11010119930728260" + i);
            workerCertificationAudit.setFrontImg(url);
            workerCertificationAudit.setBackImg(url);
            workerCertificationAudit.setCertificationMaterial(url);
            workerCertificationAudit.setAuditStatus(1);
            workerCertificationAudit.setAuditorId(1674350264389750786L);
            workerCertificationAudit.setAuditorName("李四");
            workerCertificationAudit.setAuditTime(LocalDateTime.now());
            workerCertificationAudit.setCertificationStatus(2);
            workerCertificationAuditService.save(workerCertificationAudit);

            WorkerCertification workerCertification = new WorkerCertification();
            workerCertification.setId(serveProvider.getId());
            workerCertification.setName("服务人员0" + i);
            workerCertification.setIdCardNo("11010119930728260" + i);
            workerCertification.setFrontImg(url);
            workerCertification.setBackImg(url);
            workerCertification.setCertificationMaterial(url);
            workerCertification.setCertificationStatus(2);
            workerCertification.setCertificationTime(LocalDateTime.now());
            workerCertificationService.save(workerCertification);

            i++;
        }
    }


    @Test
    void test01() {
        LambdaQueryWrapper<ServeProvider> queryWrapper = Wrappers.<ServeProvider>lambdaQuery()
                .eq(ServeProvider::getType, 3);
        List<ServeProvider> list = serveProviderService.list(queryWrapper);

        String url = "https://yjy-xzbjzfw-oss.oss-cn-hangzhou.aliyuncs.com/e5ce7b4f-81a6-481c-b475-d35996188344.png";

        int i = 0;
        for (ServeProvider serveProvider : list) {
            AgencyCertificationAudit agencyCertificationAudit = new AgencyCertificationAudit();
            agencyCertificationAudit.setId(IdUtil.getSnowflakeNextId());
            agencyCertificationAudit.setServeProviderId(serveProvider.getId());
            agencyCertificationAudit.setName("机构0" + i);
            agencyCertificationAudit.setIdNumber("11010119930728260" + i);
            agencyCertificationAudit.setLegalPersonName("法人0" + i);
            agencyCertificationAudit.setLegalPersonIdCardNo("11010119930728260" + i);
            agencyCertificationAudit.setBusinessLicense(url);
            agencyCertificationAudit.setAuditStatus(1);
            agencyCertificationAudit.setAuditorId(1674350264389750786L);
            agencyCertificationAudit.setAuditorName("李四");
            agencyCertificationAudit.setAuditTime(LocalDateTime.now());
            agencyCertificationAudit.setCertificationStatus(2);
            agencyCertificationAuditService.save(agencyCertificationAudit);

            AgencyCertification agencyCertification = new AgencyCertification();
            agencyCertification.setId(serveProvider.getId());
            agencyCertification.setName("机构0" + i);
            agencyCertification.setIdNumber("11010119930728260" + i);
            agencyCertification.setLegalPersonName("法人0" + i);
            agencyCertification.setLegalPersonIdCardNo("11010119930728260" + i);
            agencyCertification.setBusinessLicense(url);
            agencyCertification.setCertificationStatus(2);
            agencyCertification.setCertificationTime(LocalDateTime.now());
            agencyCertificationService.save(agencyCertification);

            i++;
        }
    }

}