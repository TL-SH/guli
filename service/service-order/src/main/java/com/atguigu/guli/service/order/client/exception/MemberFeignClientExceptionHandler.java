package com.atguigu.guli.service.order.client.exception;

import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.order.client.MemberFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author tanglei
 */
@Slf4j
@Component
public class MemberFeignClientExceptionHandler implements MemberFeignClient {
    @Override
    public MemberDto getMemberDtoByMemberId(String memberId) {
        log.error("熔断器被执行");
        return new MemberDto();
    }
}
