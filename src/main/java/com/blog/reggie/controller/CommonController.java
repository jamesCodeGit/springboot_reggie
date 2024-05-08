package com.blog.reggie.controller;

import com.blog.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info("文件toString信息: {}", file.toString());

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // uuid随机生成名字
        String fileName = UUID.randomUUID() + suffix;
        // 文件目录
        File dir = new File(basePath);
        // 不存在目录自动创建
        if (!dir.exists()){
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try (//输入流，通过输入流读取文件内容
             FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
             //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
             ServletOutputStream outputStream = response.getOutputStream();
        ) {
            int len;
            byte[] bytes = new byte[1024];
            while ( (len = fileInputStream.read(bytes)) != 0){
                outputStream.write(bytes, 0 , len);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
