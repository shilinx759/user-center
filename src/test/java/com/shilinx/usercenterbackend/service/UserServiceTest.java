package com.shilinx.usercenterbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author slx
 * @time 10:53
 */
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    void userRegister() {
        //测试 空值
        String userAccount = "";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        //长度测试
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        userAccount = "123";
        userPassword = "12345678";
        checkPassword = "12345678";
        result=userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);

        userAccount = "1234";
        userPassword = "123456";
        checkPassword = "12345678";
        result=userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);

        //特殊字符测试
        userAccount = "slx?";
        userPassword = "123456";
        checkPassword = "12345678";
        result=userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);

        //账号重复测试
        userAccount = "slx";
        userPassword = "12345678";
        checkPassword = "12345678";
        result=userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);

        //密码不一致测试
        //特殊字符测试
        userAccount = "ihatesummer";
        userPassword = "12345679";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //注册成功测试
        userAccount = "ihatesummer";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result>0);
    }
}