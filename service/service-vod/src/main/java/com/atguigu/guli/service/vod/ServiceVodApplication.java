package com.atguigu.guli.service.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author tanglei
 */
@SpringBootApplication
@ComponentScan({"com.atguigu.guli"})
public class ServiceVodApplication{
    public static void main(String[] args) {
        SpringApplication.run(ServiceVodApplication.class,args);
    }
}
