package com.atguigu.guli.service.ucenter.controller.api;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.common.base.util.JwtUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.atguigu.guli.service.ucenter.util.HttpClientUtils;
import com.atguigu.guli.service.ucenter.util.UcenterProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author tanglei
 */
@CrossOrigin
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
@Slf4j
public class ApiWxController {

    @Autowired
    private UcenterProperties ucenterProperties;


    @Autowired
    private MemberService memberService;

    /**
     * 生成二维码
     * @return
     */
    @GetMapping("login")
    public String genQrConnect(){
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        // 处理回调的url
        String redirectUrl = "";
        try {
            redirectUrl = URLEncoder.encode(ucenterProperties.getRedirecturl(), "UTF-8");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }
        // 处理state
        // 正常的情况下生成随机数,防止crxy
        //生成随机数，存入session（redis）
        //使用阿里云请求跳转的情况：state必须设置为ngrok内网穿透地址的前置域名
        String state = "leishuai";

        String qrCodeUrl = String.format(
                baseUrl,
                ucenterProperties.getAppid(),
                redirectUrl,
                state
        );
        return "redirect:"+qrCodeUrl;
    }

    @GetMapping("callback")
    public String callback(String code,String state){
        // 测试回调有没有起作用,测试 code state
        System.out.println("callback 起作用了");
        System.out.println("code:"+code);
        System.out.println("state:"+state);

        if(StringUtils.isEmpty(code)){
            log.error("非法回调请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        if(StringUtils.isEmpty(state)){
            log.error("非法回调请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }
        //携带授权临时票据code，和appid以及appsecret请求access_token
        String baseAcessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        String accessTokenUrl = String.format(
                baseAcessTokenUrl,
                ucenterProperties.getAppid(),
                ucenterProperties.getAppsecret(),
                code
        );

        String result = "";

        try {
            result = HttpClientUtils.get(accessTokenUrl);
        } catch (Exception e) {
            log.error("获取access_token失败");
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        Gson gson = new Gson();
        // 转换为json
        HashMap<String,Object> resultMap = gson.fromJson(result, HashMap.class);

        // 判断微信获取access_token失败的响应
        Object errcodeObj = resultMap.get("errcode");
        if(errcodeObj != null){
            String errmsg = (String)resultMap.get("errmsg");
            String errcode = (String)errcodeObj;
            log.error("获取access_token失败 - " + "message: " + errmsg + ", errcode: " + errcode);
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        // 微信获取access_token成功的响应
        String accessToken = (String) resultMap.get("access_token");
        String openid = (String)resultMap.get("openid");
        System.out.println("accessToken:"+accessToken);
        System.out.println("openid:"+openid);

        // 根据access_token获取用户的基本信息

        // 根据openid判断当前数据库是否存在微信用户
        Member member = this.memberService.getByOpenid(openid);
        if(member==null){
            // 向微信的服务器发起请求,获取当前用户的信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?" +
                    "access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(
                    baseUserInfoUrl,
                    accessToken,
                    openid
            );

            String resultUserInfo = null;
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
            } catch (Exception e) {
                log.error(ExceptionUtils.getMessage(e));
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }
            // 获取个人信息失败的响应
            HashMap<String, Object> resultUserInfoMap = gson.fromJson(resultUserInfo, HashMap.class);
            if(resultUserInfoMap.get("errcode") != null){
                log.error("获取用户信息失败" + "，message：" + resultMap.get("errmsg"));
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }
            String nickname = (String)resultUserInfoMap.get("nickname");
            String headimgurl = (String)resultUserInfoMap.get("headimgurl");
            Double sex = (Double) resultUserInfoMap.get("sex");

            member = new Member();
            // 设置openid
            member.setOpenid(openid);
            // 设置用户名
            member.setNickname(nickname);
            // 设置默认图片
            member.setAvatar(headimgurl);
            // 设置性别
            member.setSex(sex.intValue());
            // 保存用户
            memberService.save(member);
        }
        // 生成token
        String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        // 重定向到首页
        return "redirect:http://localhost:3000?token=" + jwtToken;
    }

}
