package com.mall.controller.admin;

import com.mall.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class FileController {

    @Resource
    private UploadService uploadService;

    @PostMapping("upload/file")
    public Map<String,Object> upload(MultipartFile file){
        System.out.println(file.getOriginalFilename());

        Map<String,Object> map = new HashMap<>();
        try {
            String url = uploadService.upload(file.getInputStream());
            map.put("resultCode", 200);
            map.put("data", url);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    //同时上传多张图片的方法
    @PostMapping("upload/files")
    public Map<String,Object> uploadFiles(HttpServletRequest req){
        //把req转换成CommonsMultipartResolver类型的对象
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(req.getSession().getServletContext());

        //如果不是上传文件的请求，则直接结束
        if(!multipartResolver.isMultipart(req)){
            return null;
        }

        //如果是上传文件的请求，则直接把req强制转换成multiRequest对象
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;

        List<String> fileUrl = new ArrayList<>();
        //获取到所有的文件名
        Iterator<String> fileNames = multiRequest.getFileNames();
        while(fileNames.hasNext()) {
            String name = fileNames.next();
            //根据文件名读取文件
            MultipartFile file = multiRequest.getFile(name);
            try {
                String url = uploadService.upload(file.getInputStream());
                fileUrl.add(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            Map<String,Object> map = new HashMap<>();
            map.put("resultCode",200);
            map.put("data",fileUrl);
            return map;

    }
}
