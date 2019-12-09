package com.atguigu.guli.service.base.interceptor;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.JwtUtils;
import com.atguigu.guli.common.base.util.ResponseUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tanglei
 * 网站接口api拦截器
 */
@Component
public class ApiLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(request.getRequestURL());

        //判断是否携带token或token是否有效
        if (!JwtUtils.checkToken(request)) {
            //返回封装的http结果
            ResponseUtil.out(response, R.setResult(ResultCodeEnum.LOGIN_AURH));
        }
        return true;
    }
}
