package com.atguigu.guli.service.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author tanglei
 */
@SpringBootApplication
@ComponentScan({"com.atguigu.guli"})
@EnableEurekaClient
public class ServiceEduApplication {

    public static void main(String[] args){
        SpringApplication.run(ServiceEduApplication.class,args);
    }

}
