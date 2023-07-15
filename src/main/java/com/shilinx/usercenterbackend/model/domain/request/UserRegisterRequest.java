package com.shilinx.usercenterbackend.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 * @author slx
 * @time 11:39
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 8125813286921570774L;
    public String userAccount;
    public String userPassword;
    public String checkPassword;
}
