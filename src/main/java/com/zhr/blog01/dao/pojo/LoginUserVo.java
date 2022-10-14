package com.zhr.blog01.dao.pojo;

import lombok.Data;

@Data
public class LoginUserVo {
    private long id;
    private String account;
    private String nickname;
    private String avatar;
}
