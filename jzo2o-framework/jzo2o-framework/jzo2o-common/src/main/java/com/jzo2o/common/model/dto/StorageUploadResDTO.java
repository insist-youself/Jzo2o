package com.jzo2o.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传响应值
 *
 * @author itcast
 * @create 2023/7/6 15:29
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageUploadResDTO {
    /**
     * 文件地址
     */
    private String url;
}