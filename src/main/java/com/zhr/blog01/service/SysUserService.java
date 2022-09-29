package com.zhr.blog01.service;

import com.zhr.blog01.dao.mapper.SysUserMapper;
import com.zhr.blog01.dao.pojo.SysUser;

public interface SysUserService {
    SysUser findUserById(Long Id) ;
}
