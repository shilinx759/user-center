package com.shilinx.usercenterbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shilinx.usercenterbackend.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author 86181
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-07-15 09:57:19
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 密码
     * @param checkPassword 校验密码
     * @return 用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount 账号
     * @param userPassword 密码
     * @param request request对象
     * @return 用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser 原始用户数据
     * @return safetyUser
     */
    User getSafetyUser(User originUser);
}
