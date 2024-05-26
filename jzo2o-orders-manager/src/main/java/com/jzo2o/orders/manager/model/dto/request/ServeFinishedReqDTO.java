package com.jzo2o.orders.manager.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel("服务开始模型")
public class ServeFinishedReqDTO {
    @ApiModelProperty("服务id")
    private Long id;

    @ApiModelProperty("服务完成图片")
    @NotNull(message = "请上传服务完成图片")
    @Size(min = 1, max = 3, message = "上传1-3张照片")
    private List<String> serveAfterImgs;

    @ApiModelProperty("服务完成说明")
    private String serveAfterIllustrate;
}
