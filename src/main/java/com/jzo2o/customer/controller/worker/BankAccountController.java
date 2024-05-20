package com.jzo2o.customer.controller.worker;

import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.customer.model.domain.BankAccount;
import com.jzo2o.customer.model.dto.request.BankAccountUpsertReqDTO;
import com.jzo2o.customer.model.dto.response.BankAccountResDTO;
import com.jzo2o.customer.service.IBankAccountService;
import com.jzo2o.mvc.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author linger
 * @date 2024/5/18 19:35
 */
@RestController("workerBankAccountController")
@RequestMapping("/worker/bank-account")
@Api(tags = "服务端 - 银行账户相关接口")
public class BankAccountController {

    @Resource
    private IBankAccountService bankAccountService;

    @PostMapping
    @ApiOperation("新增或更新银行账号信息")
    public void addOrUpdate(@RequestBody BankAccountUpsertReqDTO bankAccountUpsertReqDTO){
        CurrentUserInfo currentUserInfo = UserContext.currentUser();
        bankAccountUpsertReqDTO.setId(currentUserInfo.getId());
        bankAccountUpsertReqDTO.setType(currentUserInfo.getUserType());
        bankAccountService.addOrUpdate(bankAccountUpsertReqDTO);
    }

    @GetMapping("/currentUserBankAccount")
    @ApiOperation("获取当前用户银行账号")
    public BankAccountResDTO currentUserBankAccount(){
        BankAccount bankAccount = bankAccountService.getById(UserContext.currentUserId());
        return BeanUtil.copyProperties(bankAccount, BankAccountResDTO.class);
    }

}
