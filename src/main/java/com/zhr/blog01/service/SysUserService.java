package com.zhr.blog01.service;

import com.zhr.blog01.dao.mapper.SysUserMapper;
import com.zhr.blog01.dao.pojo.SysUser;
import com.zhr.blog01.vo.params.Result;

public interface SysUserService {
    SysUser findUserById(Long Id) ;

    SysUser findUser(String account, String pwd);

    Result getUserInfo(String token);

    SysUser findUserByAccount(String account);
    void save(SysUser sysUser);
}
