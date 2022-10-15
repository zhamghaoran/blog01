package com.zhr.blog01.handler;

import com.alibaba.fastjson.JSON;
import com.zhr.blog01.dao.pojo.SysUser;
import com.zhr.blog01.service.LoginService;
import com.zhr.blog01.vo.params.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginService loginService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws IOException {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String Token = request.getHeader("Authorization");
        log.info("=================request start===========================");
        String requestURL = request.getRequestURI();
        log.info("request url :{}",requestURL);
        log.info("request method {}",request.getMethod());
        log.info("token:{}",Token);
        log.info("=================request end===========================");
        if (Token == null) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().println(JSON.toJSONString(Result.fail(500,"失败")));
            return false;
        }
        SysUser sysUser = loginService.checkToken(Token);
        if (sysUser == null) {
            Result result = Result.fail(500,"失败");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().println(JSON.toJSONString(result));
            return false;
        }
        return true;
    }
}
