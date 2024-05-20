package com.jzo2o.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.customer.model.domain.BankAccount;
import com.jzo2o.customer.model.dto.request.BankAccountUpsertReqDTO;
import com.jzo2o.customer.model.dto.response.BankAccountResDTO;

/**
 * <p>
 * 服务人员/机构银行账户表 服务类
 * </p>
 *
 * @author linger
 * @since 2024-05-18
 */
public interface IBankAccountService extends IService<BankAccount> {

    /**
     * 新增或更新银行账号信息
     *
     * @param bankAccountUpsertReqDTO
     * @return
     */
    BankAccountResDTO addOrUpdate(BankAccountUpsertReqDTO bankAccountUpsertReqDTO);
}