package com.atguigu.guli.service.ucenter.service.impl;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.FormUtils;
import com.atguigu.guli.common.base.util.JwtUtils;
import com.atguigu.guli.common.base.util.MD5;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.vo.LoginInfoVo;
import com.atguigu.guli.service.ucenter.entity.vo.LoginVo;
import com.atguigu.guli.service.ucenter.entity.vo.RegisterVo;
import com.atguigu.guli.service.ucenter.mapper.MemberMapper;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author leishuai
 * @since 2019-12-04
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Override
    public Integer countRegisterByDay(String day) {
        return baseMapper.selectRegisterCount(day);
    }

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterVo registerVo) {
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();
        // 校验参数
        if(StringUtils.isEmpty(code)
                ||StringUtils.isEmpty(nickname)
                ||StringUtils.isEmpty(password)
                ||!FormUtils.isMobile(mobile)){
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        // 效验验证码
        String mobileCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(mobileCode)){
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }

        // 是否被注册
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count>0){
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }

        Member member = new Member();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setDisabled(false);
        member.setAvatar("https://guli-file-leishuai.oss-cn-shanghai.aliyuncs.com/avatar/default.jpg");

        baseMapper.insert(member);
    }

    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        // 校验参数
        if(StringUtils.isEmpty(mobile)
                ||!FormUtils.isMobile(mobile)
                ||StringUtils.isEmpty(password)){
            throw new GuliException(ResultCodeEnum.LOGIN_MOBLE_ERROR);
        }
        // 登录验证
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Member member = baseMapper.selectOne(queryWrapper);
        if(member==null) throw new GuliException(ResultCodeEnum.LOGIN_MOBLE_ERROR);

        // 检验密码
        if(!MD5.encrypt(password).equals(member.getPassword())) throw new GuliException(ResultCodeEnum.LOGIN_MOBLE_ERROR);

        // 校验用户是否已经被禁用
        if(member.getDisabled()) throw new GuliException(ResultCodeEnum.LOGIN_DISABLED_ERROR);

        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());

        return token;
    }

    @Override
    public LoginInfoVo getLoginInfo(String memberId) {
        Member member = baseMapper.selectById(memberId);
        LoginInfoVo loginInfoVo = new LoginInfoVo();
        BeanUtils.copyProperties(member,loginInfoVo);
        return loginInfoVo;
    }
}
