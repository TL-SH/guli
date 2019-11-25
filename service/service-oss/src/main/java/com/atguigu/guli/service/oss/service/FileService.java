package com.atguigu.guli.service.oss.service;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author tanglei
 */
public interface FileService {

    /**
     * 文件上传
     * @param inputStream 输入流
     * @param module 模块
     * @param originalFilename 文件原始名称
     * @return 文件在OSS上的url
     */
    String upload(InputStream inputStream,String module,String originalFilename) throws FileNotFoundException;

    /**
     * 删除文件
     * @param url
     */
    void removeFile(String url);

}
