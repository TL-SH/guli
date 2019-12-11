package com.atguigu.guli.service.order.service.impl;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.order.client.CourseFeignClient;
import com.atguigu.guli.service.order.entity.Order;
import com.atguigu.guli.service.order.entity.PayLog;
import com.atguigu.guli.service.order.mapper.PayLogMapper;
import com.atguigu.guli.service.order.service.OrderService;
import com.atguigu.guli.service.order.service.WeixinPayService;
import com.atguigu.guli.service.order.util.HttpClient;
import com.atguigu.guli.service.order.util.WeixinPayProperties;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author tanglei
 */
@Service
@Slf4j
public class WeixinPayServiceImpl implements WeixinPayService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WeixinPayProperties weixinPayProperties;


    @Autowired
    private PayLogMapper payLogMapper;


    @Autowired
    private CourseFeignClient courseFeignClient;


    @Override
    public Map<String, Object> createNative(String orderNo, String remoteAddr) {
        try {

            Map<String, Object> payMap = (Map)redisTemplate.opsForValue().get(orderNo);
            if(payMap!=null){
                return payMap;
            }

            Order order = orderService.getOrderByOrderNo(orderNo);

            HashMap<String, String> m = new HashMap<>();
            //1、设置参数
            m.put("appid", weixinPayProperties.getAppid()); // 公众号id
            m.put("mch_id", weixinPayProperties.getPartner()); // 商户号
            m.put("nonce_str", WXPayUtil.generateNonceStr()); // 随机字符串
            m.put("body", order.getCourseTitle()); // 商品描述
            m.put("out_trade_no", orderNo); // 商户订单号
            int totalFee = order.getTotalFee().multiply(new BigDecimal("100")).intValue();
            m.put("total_fee", totalFee + ""); // 标价金额
            m.put("spbill_create_ip", remoteAddr); // 终端ip
            m.put("notify_url", weixinPayProperties.getNotifyurl()); // 通知地址
            m.put("trade_type", "NATIVE"); // 交易类型

            // 2.通过httpClient 向第三方接口发送请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            // client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m, weixinPayProperties.getPartnerkey()));
            client.setHttps(true);
            client.post();

            // 3.返回第三方数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            // 4.返回封装的数据
            if("FAIL".equals(resultMap.get("return_code")) || "FAIL".equals(resultMap.get("result_code")) ){
                log.error("统一下单错误 - " +
                        "return_msg: " + resultMap.get("return_msg") + ", " +
                        "err_code: " + resultMap.get("err_code") + ", " +
                        "err_code_des: " + resultMap.get("err_code_des"));
                throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("result_code", resultMap.get("result_code"));
            // 二维码里链接
            map.put("code_url", resultMap.get("code_url"));
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            // 商户订单号
            map.put("out_trade_no", orderNo);

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            redisTemplate.opsForValue().set(orderNo,map,2, TimeUnit.HOURS);
            return map;
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }

    }

    @Override
    public Map<String, String> queryPayStatus(String orderNo) {

        try {
            //1、封装参数
            Map<String, String> m = new HashMap<>();
            m.put("appid", weixinPayProperties.getAppid());
            m.put("mch_id", weixinPayProperties.getPartner());
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            // 2.通过httpClient 向第三方接口发送请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            // client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m, weixinPayProperties.getPartnerkey()));
            client.setHttps(true);
            client.post();

            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            if("FAIL".equals(resultMap.get("return_code")) || "FAIL".equals(resultMap.get("result_code")) ){
                log.error("查询支付结果错误 - " +
                        "return_msg: " + resultMap.get("return_msg") + ", " +
                        "err_code: " + resultMap.get("err_code") + ", " +
                        "err_code_des: " + resultMap.get("err_code_des"));
                throw new GuliException(ResultCodeEnum.PAY_ORDERQUERY_ERROR);
            }
            return resultMap;
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }
    }

    /**
     * 支付成功跟改订单状态和课程购买数量
     * @param map
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        String orderNo = map.get("out_trade_no");
        Order order = orderService.getOrderByOrderNo(orderNo);
        order.setStatus(1);
        orderService.updateById(order);

        // 记录支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee().longValue());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(new Gson().toJson(map));
        //插入到支付日志表
        payLogMapper.insert(payLog);

        // 跟新课程购买数量
        courseFeignClient.updateBuyCountById(order.getCourseId());

    }
}
