package com.atguigu.guli.service.base.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tanglei
 */
@Data
public class MemberDto implements Serializable {

    private static final long serialVersionUID = 1L;

    //会员id
    private String id;

    //微信openid
    private String openid;

    //手机号
    private String mobile;

    //密码
    //private String password;

    //昵称
    private String nickname;

    //性别 1 女，2 男
    private Integer sex;

    //年龄
    private Integer age;

    //用户头像
    private String avatar;

    //用户签名
    private String sign;
}