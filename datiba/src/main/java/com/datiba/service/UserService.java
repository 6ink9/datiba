package com.datiba.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datiba.common.constant.CommonConstant;
import com.datiba.domain.User;
import com.datiba.domain.res.InfoRes;
import com.datiba.exception.BusinessException;
import com.datiba.mapper.UserMapper;
import com.datiba.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/13
 */
@Service
@Slf4j
public class UserService extends ServiceImpl<UserMapper, User> {

    /**
     * 用户登录
     * @param phone
     * @param password
     * @return
     */
    public InfoRes login(String phone, String password) {
        QueryWrapper qw = new QueryWrapper<>().eq("phone",phone).eq("password",password);
        User user = this.getOne(qw);
        if (user == null){
            throw new BusinessException("用户不存在或密码错误");
        }
        String token = JwtUtil.createToken(user.getId()+"");
        InfoRes res = InfoRes.builder()
                .token(token)
                .avatar(user.getAvatar())
                .role(user.getRole())
                .nickName(user.getNickName())
                .description(user.getDescription())
                .build();
        return res;
    }

    /**
     * 用户注册
     * @param phone
     * @param password
     * @return
     */
    public InfoRes sign(String phone, String password, String role) {
        QueryWrapper qw = new QueryWrapper<>().eq("phone",phone);
        User exitUser = this.getOne(qw);
        if (exitUser != null){
            throw new BusinessException("请勿重复注册");
        }

        User user = User.builder()
                .phone(phone)
                .description(CommonConstant.DEFAULT_DESCRIPTION)
                .avatar(CommonConstant.DEFAULT_AVATAR)
                .nickName(CommonConstant.DEFAULT_NICKNAME)
                .password(password)
                .role(Integer.valueOf(role))
                .build();

        save(user);

        String token = JwtUtil.createToken(user.getId()+"");
        InfoRes res = InfoRes.builder()
                .token(token)
                .avatar(user.getAvatar())
                .role(user.getRole())
                .nickName(user.getNickName())
                .description(user.getDescription())
                .build();
        return res;
    }

    /**
     * 修改资料
     * @param userId
     * @param nickName
     * @param avatar
     * @param motto
     * @return
     */

    public InfoRes edit(Integer userId, String nickName, String avatar, String motto) {
        log.info("用户{}尝试修改个人资料...",userId);
        User user = getById(userId);
        InfoRes res = InfoRes.builder()
                .avatar(user.getAvatar())
                .nickName(user.getNickName())
                .role(user.getRole())
                .description(user.getDescription())
                .build();

        if(StringUtils.isNotBlank(nickName)){
            user.setNickName(nickName);
            res.setNickName(nickName);
        }
        if(StringUtils.isNotBlank(avatar)){
            user.setAvatar(avatar);
            res.setAvatar(avatar);

        }
        if(StringUtils.isNotBlank(motto)){
            user.setDescription(motto);
            res.setDescription(motto);
        }
        updateById(user);
        log.info("用户修改个人资料{}成功..",res.toString());
        return res;

    }
}
