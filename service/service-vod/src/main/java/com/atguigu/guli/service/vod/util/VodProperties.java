package com.atguigu.guli.service.vod.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tanglei
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.vod.file")
public class VodProperties {
    private String keyid;
    private String keysecret;
}
