package com.zhr.blog01.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mysql.cj.util.StringUtils;
import com.zhr.blog01.config.JWTUtils;
import com.zhr.blog01.dao.mapper.SysUserMapper;
import com.zhr.blog01.dao.pojo.LoginUserVo;
import com.zhr.blog01.dao.pojo.SysUser;
import com.zhr.blog01.service.SysUserService;
import com.zhr.blog01.vo.params.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public SysUser findUserById(Long Id) {
        SysUser sysUser = sysUserMapper.selectById(Id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("zhr");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String pwd) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,pwd);
        queryWrapper.select(SysUser::getId,SysUser::getAccount,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result getUserInfo(String token) {
        Map<String, Object> map = JWTUtils.checkToken(token);
        if(map == null) {
            return  Result.fail(500,"token错误");
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isNullOrEmpty(userJson)) {
            return Result.fail(500,"token错误");
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setNickname(sysUser.getNickname());
        return Result.success(loginUserVo);
    }
}
