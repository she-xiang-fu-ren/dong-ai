package cn.github.iocoder.dong.service.user.service;

import cn.github.iocoder.dong.model.context.ReqInfoContext;

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
    String register(String username, String password);

    /**
     * 创建用户
     * @param username
     * @param password
     * @return
     */
    String createUser(String username, String password);
}
