package com.jzo2o.foundations.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.foundations.model.domain.Operator;
import com.jzo2o.foundations.model.dto.OperatorAddDTO;

/**
 * <p>
 * 运营人员 服务类
 * </p>
 *
 * @author author
 * @since 2023-06-29
 */
public interface IOperatorService extends IService<Operator> {

    /**
     * 根据名称查询运营人员
     *
     * @param username 名称
     * @return 运营人员
     */
    Operator findByUsername(String username);

    /**
     * 新增运营人员
     *
     * @param operatorAddDTO 运营人员新增模型
     */
    void add(OperatorAddDTO operatorAddDTO);
}
