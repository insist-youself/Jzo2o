package com.jzo2o.foundations.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.enums.EnableStatusEnum;
import com.jzo2o.common.expcetions.BadRequestException;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.foundations.mapper.OperatorMapper;
import com.jzo2o.foundations.model.domain.Operator;
import com.jzo2o.foundations.model.dto.OperatorAddDTO;
import com.jzo2o.foundations.service.IOperatorService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 运营人员 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-06-29
 */
@Service
public class OperatorServiceImpl extends ServiceImpl<OperatorMapper, Operator> implements IOperatorService {

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 根据名称查询运营人员
     *
     * @param username 名称
     * @return 运营人员
     */
    @Override
    public Operator findByUsername(String username) {
        return lambdaQuery().eq(Operator::getUsername, username).one();
    }

    /**
     * 新增运营人员
     *
     * @param operatorAddDTO 运营人员新增模型
     */
    @Override
    public void add(OperatorAddDTO operatorAddDTO) {
        Integer operatorNumExists = lambdaQuery().eq(Operator::getUsername, operatorAddDTO.getUsername())
                .count();
        if (operatorNumExists > 0) {
            throw new BadRequestException("账号已经存在，请勿重复添加");
        }
        Operator operator = BeanUtils.copyBean(operatorAddDTO, Operator.class);
        operator.setPassword(passwordEncoder.encode(operatorAddDTO.getPassword()));
        operator.setStatus(EnableStatusEnum.ENABLE.getStatus());
        baseMapper.insert(operator);
    }
}
