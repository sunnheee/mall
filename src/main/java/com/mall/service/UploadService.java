package com.mall.service;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;


@Service
public class UploadService {
    @Value("${qiniu.key}")
    private String key;
    @Value("${qiniu.sk}")
    private String sk;
    @Value("${qiniu.bucket}")
    private String bucket;

    private UploadManager uploadManager;

    private String token;

    @PostConstruct
    public void init(){
        Auth auth = Auth.create(key,sk);
        token = auth.uploadToken(bucket);
        Configuration cfg = new Configuration(Region.huanan());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        uploadManager = new UploadManager(cfg);
    }

    public String upload(InputStream in){
        try {
            Response res = uploadManager.put(in, null, token, null, null);
            DefaultPutRet putRet = new Gson().fromJson(res.bodyString(), DefaultPutRet.class);
            return "http://rxcnyxtt6.hn-bkt.clouddn.com/" + putRet.hash;
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return null;
    }
}
