package com.atguigu.guli.service.order.service;

import java.util.Map;

/**
 * @author tanglei
 */
public interface WeixinPayService {

   /**
    * 根据订单号下单,生成支付连接
    * @param orderNo
    * @param remoteAddr
    * @return
    */
   Map<String,Object> createNative(String orderNo, String remoteAddr);

   /**
    * 根据订单号去微信服务器查询订单的状态
    * @param orderNo
    * @return
    */
   Map<String,String> queryPayStatus(String orderNo);

   /**
    * 更改订单状态
    * @param map
    */
   void updateOrderStatus(Map<String, String> map);
}
