package com.atguigu.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.guli.service.oss.service.FileService;
import com.atguigu.guli.service.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author tanglei
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private OssProperties ossProperties;


    @Override
    public String upload(InputStream inputStream, String module, String originalFilename) throws FileNotFoundException {


        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ossProperties.getEndpoint();
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = ossProperties.getKeyid();
        String accessKeySecret = ossProperties.getKeysecret();
        String bucketName = ossProperties.getBucketname();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 判断桶是否存在
        if(!ossClient.doesBucketExist(bucketName)){
            // 不存在就创建一个
            ossClient.createBucket(bucketName);
            // 设置oss的访问权限: 公共读
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }
        // 构建日期路径 /avatar/2019/11/25
        String folder = new DateTime().toString("yyyy/MM/dd");

        // 文件名:uuid.扩展名
        String fileName = UUID.randomUUID().toString();
        String fileExtention = originalFilename.substring(originalFilename.lastIndexOf("."));
        String key = new StringBuilder()
                .append(module)
                .append("/")
                .append(folder)
                .append("/")
                .append(fileName)
                .append(fileExtention).toString();

        ossClient.putObject(bucketName,key, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        return new StringBuffer()
                .append("https://")
                .append(ossProperties.getBucketname())
                .append(".")
                .append(ossProperties.getEndpoint())
                .append("/")
                .append(key).toString();
    }

    @Override
    public void removeFile(String url) {

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ossProperties.getEndpoint();
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = ossProperties.getKeyid();
        String accessKeySecret = ossProperties.getKeysecret();
        String bucketName = ossProperties.getBucketname();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        String host = new StringBuffer()
                .append("https://")
                .append(ossProperties.getBucketname())
                .append(".")
                .append(ossProperties.getEndpoint())
                .append("/").toString();

        String key = url.substring(host.length());
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, key);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
