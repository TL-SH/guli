package com.atguigu.guli.service.edu.client.exception;

import com.atguigu.guli.service.edu.client.OrderFeignClient;

/**
 * @author tanglei
 */
public class OrderFeignClientExceptionHandler implements OrderFeignClient {
    @Override
    public Boolean isBuyByCourseId(String memberId, String courseId) {
        return false;
    }
}
