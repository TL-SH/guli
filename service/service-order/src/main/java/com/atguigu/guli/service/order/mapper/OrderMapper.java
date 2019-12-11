package com.atguigu.guli.service.order.mapper;

import com.atguigu.guli.service.order.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author leishuai
 * @since 2019-12-10
 */
public interface OrderMapper extends BaseMapper<Order> {

    Integer isBuyByCourseId(@Param("memberId") String memberId,
                            @Param("courseId") String courseId);
}
