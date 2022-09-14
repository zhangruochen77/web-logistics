package com.cly.controller;

import com.cly.service.ImgService;
import com.cly.service.impl.ImgServiceImpl;
import com.cly.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/log/api/img")
public class ImgController {

    @Autowired
    private ImgService imgService;

    /**
     * 上传图片文件
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result uploadImage(MultipartFile file) {
        String filename = randomFileName();
        boolean res = imgService.upload(file, filename);
        if (res) {
            return Result.success(ImgServiceImpl.url + filename);
        }
        return Result.fail(500, "上传图片失败！");
    }

    /**
     * 删除图片文件
     *
     * @param url
     * @return
     */
    @DeleteMapping
    public Result deleteImage(@RequestBody String url) {
        imgService.deleteImg(url);
        return Result.success();
    }


    /**
     * 按照时间规律随机生成文件名称
     *
     * @return
     */
    private String randomFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String fileName = simpleDateFormat.format(date);
        fileName += ".jpg";
        return fileName;

    }


}
