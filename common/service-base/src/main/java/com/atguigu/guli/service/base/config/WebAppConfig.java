package com.atguigu.guli.service.base.config;

import com.atguigu.guli.service.base.interceptor.ApiLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author tanglei
 */
//@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //api接口只要满足/api/*/auth/**这个规则，请求头都必须携带token信息
        registry.addInterceptor(new ApiLoginInterceptor()).addPathPatterns("/api/**/auth/**");

    }
}
