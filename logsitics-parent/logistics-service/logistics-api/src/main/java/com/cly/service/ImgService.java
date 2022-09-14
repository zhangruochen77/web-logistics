package com.cly.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImgService {

    boolean upload(MultipartFile file, String filename);

    /**
     * 删除文件
     *
     * @param url
     */
    void deleteImg(String url);
}
