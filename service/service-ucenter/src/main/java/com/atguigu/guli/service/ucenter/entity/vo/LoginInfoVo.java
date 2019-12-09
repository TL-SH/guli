package com.atguigu.guli.service.ucenter.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tanglei
 */
@Data
@ApiModel(value="用户登录基本信息")
public class LoginInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "昵称")
    private String nickname;

//    @ApiModelProperty(value = "性别 1 女，2 男")
//    private Integer sex;
//
//    @ApiModelProperty(value = "年龄")
//    private Integer age;

    @ApiModelProperty(value = "用户头像")
    private String avatar;
}
