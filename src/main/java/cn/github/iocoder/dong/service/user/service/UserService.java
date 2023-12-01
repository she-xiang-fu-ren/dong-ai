package cn.github.iocoder.dong.service.user.service;

import cn.github.iocoder.dong.controller.vo.UserInfoVO;
import cn.github.iocoder.dong.model.context.ReqInfoContext;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户的接口
 */
public interface UserService {

    String SESSION_KEY = "f-session";


    /**
     * 用户名、密码校验登录
     * @param username
     * @param password
     * @return
     */
    String register(String username, String password, HttpServletRequest request);

    /**
     * 创建用户
     * @param username
     * @param password
     * @return
     */
    String createUser(String username, String password);

    /**
     * 登出
     * @param session
     */
    void logout(String session);

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    UserInfoVO queryUserInfo(Long userId,HttpServletRequest httpServletRequest);

    void saveUserInfo(UserInfoVO userInfoVO);
}
