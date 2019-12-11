package com.atguigu.guli.service.edu.client;

import com.atguigu.guli.service.edu.client.exception.OrderFeignClientExceptionHandler;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author tanglei
 */
@FeignClient(value = "guli-order",fallback = OrderFeignClientExceptionHandler.class)
public interface OrderFeignClient {

    /**
     * 判断课程是否购买
     * @param memberId
     * @param courseId
     * @return
     */
    @GetMapping("/api/order/inner/is-buy/{memberId}/{courseId}")
    Boolean isBuyByCourseId(
            @PathVariable(value = "memberId") String memberId,
            @PathVariable(value = "courseId") String courseId);


}
