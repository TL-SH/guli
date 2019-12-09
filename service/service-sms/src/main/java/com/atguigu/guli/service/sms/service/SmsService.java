package com.atguigu.guli.service.sms.service;

import java.util.Map;

/**
 * @author tanglei
 */
public interface SmsService {

    /**
     * 发送短信
     * @param PhoneNumbers
     * @param param
     */
    void send(String PhoneNumbers, Map<String,String> param);
}
