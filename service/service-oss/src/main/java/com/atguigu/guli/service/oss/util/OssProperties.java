package com.atguigu.guli.service.oss.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tanglei
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss.file")
public class OssProperties {
    private String endpoint;
    private String keyid;
    private String keysecret;
    private String bucketname;


}
