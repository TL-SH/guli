package com.atguigu.guli.service.order.client.exception;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.order.client.CourseFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author tanglei
 */
@Slf4j
@Component
public class CourseFeignClientExceptionHandler implements CourseFeignClient {
    @Override
    public CourseDto getCourseDtoById(String id) {
        log.error("熔断器被执行");
        return new CourseDto();
    }

    @Override
    public R updateBuyCountById(String id) {
        log.error("熔断器被执行");
        return R.error();
    }
}
