package com.shilinx.usercenterbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shilinx.usercenterbackend.model.domain.User;
import com.shilinx.usercenterbackend.model.domain.request.UserLoginRequest;
import com.shilinx.usercenterbackend.model.domain.request.UserRegisterRequest;
import com.shilinx.usercenterbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.shilinx.usercenterbackend.constant.UserConstant.ADMIN_ROLE;
import static com.shilinx.usercenterbackend.constant.UserConstant.USER_LOGIN_STATUS;

/**
 * 用户接口
 * @author slx
 * @time 10:01
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount,userPassword,checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount,userPassword,request);
    }

    @GetMapping("/search")
    public List<User> searchUser(String userName,HttpServletRequest request) {
        //todo 最好写在service里
        //权限校验
        if (!isAdmin(request)) {
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like("username", userName);
        }
        //采用模糊查询
        List<User> userList = userService.list(queryWrapper);
        //只给前端返回脱敏后的数据
        return userList.stream().map(user -> userService.getSafetyUser(user))
                .collect(Collectors.toList());
    }

    @PutMapping("/delete")
    public boolean deleteUser(@RequestBody  long id,HttpServletRequest request) {
        //todo 最好写在service里
        if (!isAdmin(request)||id <= 0) {
            return false;
        }
        //删除用户
        return userService.removeById(id);
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        //权限设置 只有管理员可删除
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User) userObj;
        return user.getUserRole() == ADMIN_ROLE;
    }
}
