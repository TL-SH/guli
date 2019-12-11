package com.atguigu.guli.service.order.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.order.service.WeixinPayService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author tanglei
 */
@RestController
@RequestMapping("/api/order/weixinPay")
@Api(description = "网站微信支付")
@CrossOrigin //跨域
@Slf4j
public class ApiWeixinPayController {

    @Autowired
    private WeixinPayService weixinPayService;

    /**
     * 生成二维码
     * @return
     */
    @GetMapping("/createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo, HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        Map map = weixinPayService.createNative(orderNo, remoteAddr);
        return R.ok().data(map);
    }

    /**
     *  查询订单状态
     *  此接口会被轮询(周期性调用)
     *  @param orderNo
     *  @return
     */
    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
        Map<String, String> map = this.weixinPayService.queryPayStatus(orderNo);
        // 支付成功,更改订单状态
        if("SUCCESS".equals(map.get("trade_state"))){
            // 更新订单的状态
            weixinPayService.updateOrderStatus(map);
            // 如果支付成功则停止轮询
            return R.ok().message("支付成功");
        }
        return R.setResult(ResultCodeEnum.PAY_RUN);
    }

}