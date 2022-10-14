package com.zhr.blog01.handler;

import com.zhr.blog01.vo.params.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// 对加了Controller 的方法进行拦截处理 AOP的实现
@ControllerAdvice
public class AllExceptionHandler {
    // 对异常进行处理
    @ExceptionHandler(Exception.class)
    @ResponseBody // 返回json数据
    public Result doException(Exception ex) {
        ex.printStackTrace();
        return Result.fail(-999,"系统异常");
    }

}
