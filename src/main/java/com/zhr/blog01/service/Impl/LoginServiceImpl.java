package com.zhr.blog01.service.Impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysql.cj.util.StringUtils;
import com.zhr.blog01.config.JWTUtils;
import com.zhr.blog01.dao.pojo.LoginParam;
import com.zhr.blog01.dao.pojo.SysUser;
import com.zhr.blog01.service.LoginService;
import com.zhr.blog01.service.SysUserService;
import com.zhr.blog01.vo.params.Result;
import io.netty.util.internal.StringUtil;
import org.apache.commons.codec.cli.Digest;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.tomcat.jni.Error;
import org.eclipse.persistence.oxm.json.JsonObjectBuilderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

@Service
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
}
