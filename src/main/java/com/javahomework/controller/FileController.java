package com.javahomework.controller;

import com.javahomework.common.Result;
import com.javahomework.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * @Auther:xiaoTongYu
 * @Date:2024/9/8
 * @Description:com.javahomework.controller
 * @version:1.0
 */
@RestController
@RequestMapping("/upload")
public class FileController {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/")
    public Result save(@RequestParam("file") MultipartFile img) {
        String url=System.getProperty("user.dir");//获取项目绝对路径
        if(!img.isEmpty()){
            UUID id=UUID.randomUUID();//生成文件名
            try {
                //保存上传的资源文件路径，路径在部署jar包同级目录。
                String path = url+"/static/img/";
                File dir = new File(path);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                //参数就是图片保存在服务器的本地地址
                img.transferTo(new File(path+id+".jpg"));
                String imgHtml = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath())+"/static/img/"+id+".jpg";
                System.out.println(imgHtml);
                return  Result.suc(imgHtml);
            } catch (Exception e) {
                return Result.fail();
            }
        }
        return Result.fail();
    }



}
