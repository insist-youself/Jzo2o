package com.jzo2o.api.customer;

import com.jzo2o.api.customer.dto.request.InstitutionStaffAddReqDTO;
import com.jzo2o.api.customer.dto.request.InstitutionStaffPageQueryReqDTO;
import com.jzo2o.api.customer.dto.response.InstitutionStaffResDTO;
import com.jzo2o.common.model.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 内部接口 - 机构下属服务人员相关接口
 *
 * @author itcast
 */
@FeignClient(contextId = "jzo2o-customer", value = "jzo2o-customer", path = "/customer/inner/institution-staff")
public interface InstitutionStaffApi {

    /**
     * 根据多个id查询机构下的服务人员
     * @param ids
     * @return
     */
    @GetMapping("/findByIds")
    List<InstitutionStaffResDTO> findByIds(@RequestParam("ids") List<Long> ids);

    /**
     * 根据id查询机构下的服务人员
     * @param id
     * @return
     */
    @GetMapping("/findById")
    InstitutionStaffResDTO findById(@RequestParam("id") Long id);

    /**
     * 根据机构id和机构人员id查询机构下服务人员，加上机构id目的是了确定该人员归属于该机构
     * @param id
     * @param institutionId
     * @return
     */
    @GetMapping("/findByIdAndInstitutionId/{id}")
    InstitutionStaffResDTO findByIdAndInstitutionId(@PathVariable("id") Long id, @RequestParam("institutionId") Long institutionId);

    /**
     * 根据机构id查询机构下服务人员列表
     * @param institutionId
     * @return
     */
    @GetMapping("/findByInstitutionId")
    List<InstitutionStaffResDTO> findByInstitutionId(@RequestParam("institutionId") Long institutionId);

    @PostMapping("/add")
    void add(@RequestBody InstitutionStaffAddReqDTO institutionStaffAddReqDTO);
}
