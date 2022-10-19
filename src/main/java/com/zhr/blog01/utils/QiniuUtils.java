package com.zhr.blog01.utils;

import com.alibaba.fastjson2.JSON;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class QiniuUtils {
    public static final String url = "http://rjz9q78si.hn-bkt.clouddn.com";

    private static String accessKey = "s964IOInx469qa-MievPrRw_iF5mNhWJuaOmYVgN";

    private static String accessSecretKey = "cII6zlZ70hcaDLYc1czrS4PKAiM-X5a46raMmkSU";
    
    public boolean upload(MultipartFile file,String filename) {
        // 构造一个带指定region的对象配置类
        Configuration cfg = new Configuration(Region.huanan());
        // .. 其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // .. 生成上传凭证
        String bucket = "jjking";
        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, accessSecretKey);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(uploadBytes, filename, upToken);
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
