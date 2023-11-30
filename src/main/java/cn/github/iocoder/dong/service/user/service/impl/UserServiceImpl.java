package cn.github.iocoder.dong.service.user.service.impl;

import cn.github.iocoder.dong.core.exception.ExceptionUtil;
import cn.github.iocoder.dong.core.helper.Ip2regionSearcher;
import cn.github.iocoder.dong.core.helper.UserPwdEncoder;
import cn.github.iocoder.dong.core.helper.UserSessionHelper;
import cn.github.iocoder.dong.core.helper.dto.IpInfoDTO;
import cn.github.iocoder.dong.core.utils.IpUtil;
import cn.github.iocoder.dong.core.utils.ServletUtils;
import cn.github.iocoder.dong.model.context.ReqInfoContext;
import cn.github.iocoder.dong.model.enums.LoginResultEnum;
import cn.github.iocoder.dong.model.enums.StatusEnum;
import cn.github.iocoder.dong.model.enums.YesOrNoEnum;
import cn.github.iocoder.dong.service.user.repository.entity.LoginLogDO;
import cn.github.iocoder.dong.service.user.repository.entity.UserDO;
import cn.github.iocoder.dong.service.user.repository.mapper.LoginLogMapper;
import cn.github.iocoder.dong.service.user.repository.mapper.UserMapper;
import cn.github.iocoder.dong.service.user.service.UserService;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private LoginLogMapper loginLogMapper;

    @Autowired
    private Ip2regionSearcher ip2regionSearcher;

    @Resource
    private UserSessionHelper userSessionHelper;

    @Resource
    private UserPwdEncoder userPwdEncoder;

    /**
     * 用户名、密码校验登录
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public String register(String username, String password, HttpServletRequest request) {
        UserDO userDO = userMapper.getUserByName(username);
        //校验账号是否存在
        if (!ObjectUtil.isNotNull(userDO)){
            createLoginLog(null,username, LoginResultEnum.BAD_CREDENTIALS,request);
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, "userName=" + username);
        }
        //校验密码
        if(!userPwdEncoder.match(password,userDO.getPassword())){
            createLoginLog(userDO.getId(),username, LoginResultEnum.BAD_CREDENTIALS,request);
            throw ExceptionUtil.of(StatusEnum.USER_PWD_ERROR);
        }
        //添加日志
        createLoginLog(userDO.getId(),username, LoginResultEnum.SUCCESS,request);
        // 登录成功，返回对应的session
        ReqInfoContext.getReqInfo().setUserId(userDO.getId());
        return userSessionHelper.genSession(userDO.getId());
    }

    /**
     * 添加登录日志
     * @param userId
     * @param username
     * @param loginResultEnum
     */
    private void createLoginLog(Long userId,String username,LoginResultEnum loginResultEnum,HttpServletRequest request) {
        LoginLogDO loginLogDO = new LoginLogDO();
        loginLogDO.setUserId(userId);
        String clientIp = IpUtil.getClientIp(request);
        loginLogDO.setUserIp(clientIp);
        IpInfoDTO search = ip2regionSearcher.search(clientIp);
        loginLogDO.setIpVest(search.getAddress());
        loginLogDO.setUserAgent(ServletUtils.getUserAgent());
        loginLogDO.setResult(loginResultEnum.getResult());
        loginLogMapper.insert(loginLogDO);
    }

    /**
     * 创建用户
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public String createUser(String username, String password) {
        //查询当前账号是否被注册过
        UserDO userDO = userMapper.getUserByName(username);
        if (ObjectUtil.isNotNull(userDO)){
            throw ExceptionUtil.of(StatusEnum.USER_EXISTS, "userName=" + username);
        }
        //密码加密
        String s = userPwdEncoder.encPwd(password);
        //构建参数
        userMapper.insert(new UserDO().setPassword(s)
                .setDeleted(YesOrNoEnum.NO.getCode())
                .setUsername(username));
        return "注册成功";
    }

    /**
     * 登出
     *
     * @param session
     */
    @Override
    public void logout(String session) {
        userSessionHelper.removeSession(session);
    }
}
