package com.atguigu.guli.service.order.client;

import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.order.client.exception.MemberFeignClientExceptionHandler;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author tanglei
 */
@Component
@FeignClient(value = "GULI-UCENTER", fallback = MemberFeignClientExceptionHandler.class)
public interface MemberFeignClient {

    /**
     * 根据会员id查询会员信息
     * @param memberId
     * @return
     */
    @GetMapping(value = "/api/ucenter/member/inner/get-member-dto/{memberId}")
    public MemberDto getMemberDtoByMemberId(@PathVariable("memberId") String memberId);

}
