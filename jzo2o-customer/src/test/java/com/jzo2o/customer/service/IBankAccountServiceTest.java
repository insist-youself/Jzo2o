package com.jzo2o.customer.service;

import com.jzo2o.customer.model.dto.request.BankAccountUpsertReqDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class IBankAccountServiceTest {
    @Resource
    private IBankAccountService bankAccountService;

    @Test
    void test() {
        String url = "https://yjy-xzbjzfw-oss.oss-cn-hangzhou.aliyuncs.com/e5ce7b4f-81a6-481c-b475-d35996188344.png";

        BankAccountUpsertReqDTO bankAccountUpsertReqDTO = new BankAccountUpsertReqDTO();
        bankAccountUpsertReqDTO.setId(1695628302246506498L);
        bankAccountUpsertReqDTO.setType(3);
        bankAccountUpsertReqDTO.setName("张三");
        bankAccountUpsertReqDTO.setBankName("中国工商银行");
        bankAccountUpsertReqDTO.setProvince("北京市");
        bankAccountUpsertReqDTO.setCity("北京市");
        bankAccountUpsertReqDTO.setDistrict("海淀区");
        bankAccountUpsertReqDTO.setBranch("海淀区支行");
        bankAccountUpsertReqDTO.setAccount("2345678787678767");
        bankAccountUpsertReqDTO.setAccountCertification(url);
        bankAccountService.upsert(bankAccountUpsertReqDTO);
    }

}