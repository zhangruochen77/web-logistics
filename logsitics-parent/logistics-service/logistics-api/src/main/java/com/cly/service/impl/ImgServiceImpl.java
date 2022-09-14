package com.cly.service.impl;

import com.alibaba.fastjson.JSON;
import com.cly.service.ImgService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImgServiceImpl implements ImgService {

    /**
     * 使用七牛云提供的30天的链接，仅仅支持 http 协议 并且以 / 结尾 需要和后面的图片上传结合
     */
    public static final String url = "http://ri6exgdg2.hn-bkt.clouddn.com/";

    /**
     * ak 密钥
     */
    @Value("${qiniu.accessKey}")
    private String accessKey;

    /**
     * sk 密钥
     */
    @Value("${qiniu.accessSecretKey}")
    private String accessSecretKey;

    /**
     * 空间名称
     */
    private String bucket = "ruo-chen-space";

    /**
     * 上传图片到七牛云方法
     *
     * @param file 需要进行上传的文件
     * @return 是否上传成功
     */
    public boolean upload(MultipartFile file, String filename) {

        // 构造一个带指定 Region 对象的配置类 指定云服务器所在的位置
        Configuration cfg = new Configuration(Region.huanan());
        // ...其他参数参考类注释 生成上传管理对象
        UploadManager uploadManager = new UploadManager(cfg);

        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, accessSecretKey);  // 生成权限人
            String upToken = auth.uploadToken(bucket);  // 创建权限 token 上传凭证
            Response response = uploadManager.put(uploadBytes, filename, upToken);
            // 解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            return putRet != null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 删除图片文件
     *
     * @param url
     */
    @Override
    public void deleteImg(String url) {
        int len = url.length();
        String imgName = url.substring(len - 21, len);

        Configuration cfg = new Configuration(Region.huanan());
        Auth auth = Auth.create(accessKey, accessSecretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            //单次批量请求的文件数量不得超过1000
            String[] keyList = new String[]{imgName};
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, keyList);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keyList.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keyList[i];
                System.out.print(key + "\t");
                if (status.code == 200) {
                    System.out.println("delete success");
                } else {
                    System.out.println(status.data.error);
                }
            }
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
    }

}
