package com.atguigu.guli.service.order.client;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.order.client.exception.CourseFeignClientExceptionHandler;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author tanglei
 */
@Component
@FeignClient(value = "guli-edu", fallback = CourseFeignClientExceptionHandler.class)
public interface CourseFeignClient {

    /**
     * 根据课程id获取课程信息
     * @param id
     * @return
     */
    @GetMapping(value = "/api/edu/course/inner/get-course-dto/{id}")
    CourseDto getCourseDtoById(@PathVariable(value = "id") String id);

    /**
     * 根据课程id更改销售量
     * @param id
     * @return
     */
    @GetMapping(value = "/api/edu/course/inner/update-buy-count/{id}")
    R updateBuyCountById(@PathVariable(value = "id") String id);
}
