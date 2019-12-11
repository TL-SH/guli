package com.atguigu.guli.service.order.service;

import com.atguigu.guli.service.order.entity.Order;
import com.atguigu.guli.service.order.entity.vo.OrderQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author leishuai
 * @since 2019-12-10
 */
public interface OrderService extends IService<Order> {

    /**
     * 保存订单
     * @param courseId
     * @param memberId
     * @return
     */
    String saveOrder(String courseId, String memberId);


    /**
     * 分页订单类表
     * @param pageParam
     * @param orderQueryVo
     * @return
     */
    Map<String,Object> selectPage(Page<Order> pageParam, OrderQueryVo orderQueryVo);


    /**
     * 课程是否购买
     * @param memberId
     * @param courseId
     * @return
     */
    Boolean isBuyByCourseId(String memberId,String courseId);


}
