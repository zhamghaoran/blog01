package com.zhr.blog01.common.aop;

import java.lang.annotation.*;
 // type 代表可以放在放在类上面，method代表可以放在方法上
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String module() default "";
    String operation() default "";

}
