package com.jzo2o.publics.controller.outer;

import com.jzo2o.publics.model.dto.response.StorageUploadResDTO;
import com.jzo2o.thirdparty.core.storage.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author itcast
 */
@Slf4j
@RestController
@RequestMapping("/storage")
@Api(tags = "存储相关接口")
public class StorageController {
    @Resource
    private StorageService storageService;

    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public StorageUploadResDTO upload(@RequestPart("file") MultipartFile file) {
        //获得文件扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String url = null;
        try {
            url = storageService.upload(extension, new ByteArrayInputStream(file.getBytes()));
        } catch (IOException e) {
            log.error("文件上传失败,原因：", e);
        }
        return new StorageUploadResDTO(url);
    }
}
