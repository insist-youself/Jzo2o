package com.jzo2o.customer.controller.inner;


import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.api.customer.InstitutionStaffApi;
import com.jzo2o.api.customer.dto.request.InstitutionStaffAddReqDTO;
import com.jzo2o.api.customer.dto.response.InstitutionStaffResDTO;
import com.jzo2o.customer.model.domain.InstitutionStaff;
import com.jzo2o.customer.service.IInstitutionStaffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 内部接口 - 机构下属服务人员相关接口
 * </p>
 *
 * @author itcast
 * @since 2023-07-18
 */
@RestController
@RequestMapping("/inner/institution-staff")
@Api(tags = "内部接口 - 机构下属服务人员相关接口")
public class InnerInstitutionStaffController implements InstitutionStaffApi {
    @Resource
    private IInstitutionStaffService institutionStaffService;

    @Override
    @GetMapping("/findByIds")
    @ApiOperation("根据id列表查询")
    public List<InstitutionStaffResDTO> findByIds(@RequestParam("ids") List<Long> ids) {
        return BeanUtil.copyToList(institutionStaffService.listByIds(ids), InstitutionStaffResDTO.class);
    }

    @GetMapping("/findById")
    @ApiOperation("根据id查询")
    @Override
    public InstitutionStaffResDTO findById(Long id) {
        InstitutionStaff institutionStaff = institutionStaffService.getById(id);
        InstitutionStaffResDTO institutionStaffResDTO = BeanUtil.toBean(institutionStaff, InstitutionStaffResDTO.class);
        return institutionStaffResDTO;
    }


    @Override
    @GetMapping({"/findByIdAndInstitutionId/{id}"})
    public InstitutionStaffResDTO findByIdAndInstitutionId(@PathVariable("id") Long id, @RequestParam("institutionId") Long institutionId) {
        return institutionStaffService.findByIdAndInstitutionId(id, institutionId);
    }

    @Override
    public List<InstitutionStaffResDTO> findByInstitutionId(Long institutionId) {
        return institutionStaffService.findByInstitutionId(institutionId);
    }

    @Override
    @PostMapping("/add")
    @ApiOperation("内部接口生成服务人员")
    public void add(@RequestBody InstitutionStaffAddReqDTO institutionStaffAddReqDTO) {
        institutionStaffService.add(institutionStaffAddReqDTO);

    }


}
