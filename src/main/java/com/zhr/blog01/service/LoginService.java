package com.zhr.blog01.service;


import com.zhr.blog01.dao.pojo.LoginParam;
import com.zhr.blog01.vo.params.Result;

public interface LoginService {
    Result login(LoginParam loginParam);

    Result logout(String token);

    Result register(LoginParam loginParam);
}
