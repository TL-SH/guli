package com.atguigu.guli.service.order.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.util.JwtUtils;
import com.atguigu.guli.service.order.entity.Order;
import com.atguigu.guli.service.order.entity.vo.OrderQueryVo;
import com.atguigu.guli.service.order.service.OrderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author tanglei
 */
@RestController
@RequestMapping("/api/order")
@Api(description = "网站订单管理")
@CrossOrigin //跨域
@Slf4j
public class ApiOrderController {
    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "新增订单")
    @PostMapping("auth/save/{courseId}")
    public R save(@PathVariable String courseId, HttpServletRequest request){
        String orderId = orderService.saveOrder(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderId",orderId);
    }

    @ApiOperation(value = "删除订单")
    @DeleteMapping("auth/remove/{orderId}")
    public R remove(@PathVariable String orderId) {
        orderService.removeById(orderId);
        return R.ok();
    }

    @ApiOperation(value = "获取订单")
    @GetMapping("auth/get/{orderId}")
    public R get(@PathVariable String orderId) {
        Order order = orderService.getById(orderId);
        return R.ok().data("item", order);
    }

    @ApiOperation(value = "获取订单分页列表")
    @GetMapping("auth/{page}/{limit}")
    public R index(
        @ApiParam(name = "page", value = "当前页码", required = true)
        @PathVariable Long page,
        @ApiParam(name = "limit", value = "每页记录数", required = true)
        @PathVariable Long limit,
        @ApiParam(name = "courseQuery", value = "查询对象", required = false)
        OrderQueryVo orderQueryVo,
        HttpServletRequest request){

        orderQueryVo.setMemberId(JwtUtils.getMemberIdByJwtToken(request));
        Page<Order> pageParam = new Page<>(page,limit);
        Map<String, Object> map = orderService.selectPage(pageParam, orderQueryVo);
        return R.ok().data(map);
    }


    @ApiOperation(value = "判断课程是否购买")
    @GetMapping("inner/is-buy/{memberId}/{courseId}")
    public Boolean isBuyByCourseId(@PathVariable String memberId, @PathVariable String courseId) {
        return orderService.isBuyByCourseId(memberId, courseId);
    }
}
