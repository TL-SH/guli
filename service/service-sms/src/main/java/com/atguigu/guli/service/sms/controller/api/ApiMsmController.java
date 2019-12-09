package com.atguigu.guli.service.sms.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.util.RandomUtil;
import com.atguigu.guli.service.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author tanglei
 */

@RestController
@RequestMapping("/api/sms")
@Api(description = "短信管理")
@CrossOrigin //跨域
@Slf4j
public class ApiMsmController {


    @Autowired
    private SmsService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ApiOperation(value = "获取验证码")
    @GetMapping(value = "/send/{phone}")
    public R code(@PathVariable String phone) {
        // 判断验证码是否为空
//        String code = this.redisTemplate.opsForValue().get(phone);
//        if(!StringUtils.isEmpty(code)){
//            return R.ok();
//        }

        String code = RandomUtil.getFourBitRandom();
        HashMap<String, String> param = new HashMap<>();
        param.put("code",code);
        this.msmService.send(phone,param);
        this.redisTemplate.opsForValue().set(phone,code,5,TimeUnit.MINUTES);

        return R.ok().message("短信发送成功");
    }

}
