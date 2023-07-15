package com.shilinx.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shilinx.usercenterbackend.model.domain.User;
import com.shilinx.usercenterbackend.service.UserService;
import com.shilinx.usercenterbackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.shilinx.usercenterbackend.constant.UserConstant.USER_LOGIN_STATUS;

/**
* @author 86181
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-07-15 09:57:19
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    static final String SALT = "shilinx";

    /**
     * 用户状态的键
     */

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验
        //非空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        //长度要求
        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }
        //账号是否特殊字符
        String reg = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Matcher matcher = Pattern.compile(reg).matcher(userAccount);
        if (matcher.find()) {
            //如果在 账号中找到了特殊字符,则返回失败
            return -1;
        }
        //密码是否相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        //账号是否已经存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }
        //2.密码加密.插入数据库
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.插入数据库,返回生成的 ID
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean save = this.save(user);
        if (!save) {
            System.out.println("插入数据库失败");
            return -1;
        }
        return user.getId();

    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        //非空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        //长度要求
        if (userAccount.length() < 4 || userPassword.length() < 8 ) {
            return null;
        }
        //账号是否特殊字符
        String reg = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Matcher matcher = Pattern.compile(reg).matcher(userAccount);
        if (matcher.find()) {
            //如果在 账号中找到了特殊字符,则返回失败
            return null;
        }
        //校验密码是否正确
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //账号是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user==null) {
            System.out.println("账号或密码错误");
            log.info("account cannot match password");
            return null;
        }
        //登录成功,返回脱敏后的数据对象
        User safetyUser = getSafetyUser(user);
        request.getSession().setAttribute(USER_LOGIN_STATUS,safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setUserPhone(originUser.getUserPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }
}




