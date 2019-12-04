package com.atguigu.guli.service.ucenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author tanglei
 */
@SpringBootApplication
@ComponentScan({"com.atguigu.guli"})
public class ServiceUcenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceUcenterApplication.class, args);
    }
}