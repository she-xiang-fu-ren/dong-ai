package cn.github.iocoder.dong.service.user.service.impl;

import cn.github.iocoder.dong.core.helper.UserSessionHelper;
import cn.github.iocoder.dong.core.helper.UserPwdEncoder;
import cn.github.iocoder.dong.model.context.ReqInfoContext;
import cn.github.iocoder.dong.model.enums.StatusEnum;
import cn.github.iocoder.dong.model.enums.YesOrNoEnum;
import cn.github.iocoder.dong.model.exception.ExceptionUtil;
import cn.github.iocoder.dong.service.user.repository.entity.UserDO;
import cn.github.iocoder.dong.service.user.repository.mapper.UserMapper;
import cn.github.iocoder.dong.service.user.service.UserService;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserSessionHelper userSessionHelper;

    @Resource
    private UserPwdEncoder userPwdEncoder;

    /**
     * 初始话用户
     *
     * @param session
     * @param reqInfo
     */
    @Override
    public void initLoginUser(String session, ReqInfoContext.ReqInfo reqInfo) {
        //判断session是否为空
        if (ObjectUtil.isNotNull(session)) {
            Long userId = userSessionHelper.getUserIdBySession(session);
            UserDO userDO = userMapper.selectById(userId);
            if (ObjectUtil.isNotNull(userDO)) {
                reqInfo.setSession(session);
                reqInfo.setUserId(userId);
                reqInfo.setUser(userDO);
            }
        }
    }

    /**
     * 用户名、密码校验登录
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public String register(String username, String password) {
        UserDO userDO = userMapper.getUserByName(username);
        //校验账号是否存在
        if (!ObjectUtil.isNotNull(userDO)){
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, "userName=" + username);
        }
        //校验密码
        if(!userPwdEncoder.match(password,userDO.getPassword())){
            throw ExceptionUtil.of(StatusEnum.USER_PWD_ERROR);
        }
        // 登录成功，返回对应的session
        ReqInfoContext.getReqInfo().setUserId(userDO.getId());
        return userSessionHelper.genSession(userDO.getId());
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
        return null;
    }
}
