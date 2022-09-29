package com.zhr.blog01.service.Impl;

import com.zhr.blog01.dao.mapper.SysUserMapper;
import com.zhr.blog01.dao.pojo.SysUser;
import com.zhr.blog01.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Override
    public SysUser findUserById(Long Id) {
        SysUser sysUser = sysUserMapper.selectById(Id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("zhr");
        }
        return sysUser;
    }
}
