package com.zhr.blog01.service.Impl;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.util.StringUtils;
import com.zhr.blog01.config.JWTUtils;
import com.zhr.blog01.dao.pojo.LoginParam;
import com.zhr.blog01.dao.pojo.SysUser;
import com.zhr.blog01.service.LoginService;
import com.zhr.blog01.service.SysUserService;
import com.zhr.blog01.vo.params.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    private static final String slat = "123456";


    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public Result login(LoginParam loginParam) {
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if (StringUtils.isNullOrEmpty(account) || StringUtils.isNullOrEmpty(password)) {
            return Result.fail(500,"错误");
        }
        String pwd = DigestUtils.md5DigestAsHex((password + slat).getBytes());
        SysUser user = sysUserService.findUser(account, pwd);
        if (user == null)
            return Result.fail(500,"密码错误");
        String Token = JWTUtils.createToken(user.getId());
        redisTemplate.opsForValue().set("TOKEN_" + Token, JSON.toJSONString(user),1, TimeUnit.DAYS);
        return Result.success(Token);
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_" + token);
        return Result.success(null);
    }

    @Override
    public Result register(LoginParam loginParam) {
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        if (StringUtils.isNullOrEmpty(account) || StringUtils.isNullOrEmpty(password) || StringUtils.isNullOrEmpty(nickname))
            return Result.fail(500,"错误");
        SysUser sysUser = this.sysUserService.findUserByAccount(account);
        if (sysUser != null) {
            return Result.fail(500,"已注册");
        }
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setPassword(DigestUtils.md5DigestAsHex((password + slat).getBytes()));
        sysUser.setAccount(account);
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1);
        sysUser.setDeleted(0);
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.sysUserService.save(sysUser);
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        Object userId = stringObjectMap.get("userId");
        return sysUserService.findUserById((Long) userId);
    }

}
