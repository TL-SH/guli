package com.atguigu.guli.service.order.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tanglei
 */
@Data
@ApiModel(description = "订单")
public class OrderQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程id")
    private String courseId;

    @ApiModelProperty(value = "会员id")
    private String memberId;

    @ApiModelProperty(value = "订单状态（0：未支付 1：已支付）")
    private Integer status;

    @ApiModelProperty(value = "开始时间")
    private String gmtCreateBegin;

    @ApiModelProperty(value = "结束时间")
    private String gmtCreateEnd;

}