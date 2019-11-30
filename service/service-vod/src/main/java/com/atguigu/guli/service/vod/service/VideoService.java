package com.atguigu.guli.service.vod.service;

import com.aliyuncs.exceptions.ClientException;

import java.io.InputStream;
import java.util.Map;

/**
 * @author tanglei
 */
public interface VideoService {
    /**
     * 视频上传
     * @param inputStream  以输入流的形式上传
     * @param originalFilename 上传文件的名称
     * @return 返回一个视频id
     */
    String uploadVideo(InputStream inputStream,String originalFilename);

    /**
     * 根据videoId删除阿里云的视频
     * @param videoId
     * @throws ClientException
     */
    void removeVideo(String videoId) throws Exception;

    /**
     * 后端获取上传地址和凭证
     * @param title 标题名
     * @param fileName 文件名
     * @return
     * @throws com.aliyuncs.exceptions.ClientException
     */
    Map<String,Object> getVideoUploadAuthAndAddress(String title,String fileName) throws ClientException;


    /**
     * 刷新视频上传凭证
     * @param videoId
     * @return
     * @throws ClientException
     */
    Map<String,Object> refreshVideoUploadAuth(String videoId) throws ClientException;


}
