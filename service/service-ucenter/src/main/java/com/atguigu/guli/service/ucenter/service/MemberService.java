package com.atguigu.guli.service.ucenter.service;

import com.atguigu.guli.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author leishuai
 * @since 2019-12-04
 */
public interface MemberService extends IService<Member> {
    /**
     * 用户注册统计
     * @param day
     * @return
     */
    Integer countRegisterByDay(String day);
}
