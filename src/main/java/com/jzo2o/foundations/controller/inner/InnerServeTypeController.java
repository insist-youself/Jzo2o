package com.jzo2o.foundations.controller.inner;


import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.api.foundations.ServeTypeApi;
import com.jzo2o.api.foundations.dto.response.ServeTypeSimpleResDTO;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.service.IServeTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 内部接口 - 服务类型相关接口
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
@RestController
@RequestMapping("/inner/serve-type")
@Api(tags = "内部接口 - 服务类型相关接口")
public class InnerServeTypeController implements ServeTypeApi {
    @Resource
    private IServeTypeService serveTypeService;

    @Override
    @GetMapping("/listByIds")
    @ApiOperation("根据id列表查询服务类型")
    public List<ServeTypeSimpleResDTO> listByIds(@RequestParam("ids") List<Long> ids) {
        return BeanUtil.copyToList(serveTypeService.listByIds(ids), ServeTypeSimpleResDTO.class);
    }
}
