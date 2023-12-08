package cn.github.iocoder.dong.service.user.service.impl;

import cn.github.iocoder.dong.controller.vo.UserInfoVO;
import cn.github.iocoder.dong.core.exception.ExceptionUtil;
import cn.github.iocoder.dong.core.helper.Ip2regionSearcher;
import cn.github.iocoder.dong.core.helper.UserPwdEncoder;
import cn.github.iocoder.dong.core.helper.UserSessionHelper;
import cn.github.iocoder.dong.core.helper.dto.IpInfoDTO;
import cn.github.iocoder.dong.core.utils.IpUtil;
import cn.github.iocoder.dong.core.utils.ServletUtils;
import cn.github.iocoder.dong.model.context.ReqInfoContext;
import cn.github.iocoder.dong.model.convert.UserConvert;
import cn.github.iocoder.dong.model.enums.LoginResultEnum;
import cn.github.iocoder.dong.model.enums.StatusEnum;
import cn.github.iocoder.dong.model.enums.YesOrNoEnum;
import cn.github.iocoder.dong.service.user.repository.entity.LoginLogDO;
import cn.github.iocoder.dong.service.user.repository.entity.UserDO;
import cn.github.iocoder.dong.service.user.repository.entity.UserInfoDO;
import cn.github.iocoder.dong.service.user.repository.mapper.LoginLogMapper;
import cn.github.iocoder.dong.service.user.repository.mapper.UserInfoMapper;
import cn.github.iocoder.dong.service.user.repository.mapper.UserMapper;
import cn.github.iocoder.dong.service.user.service.UserService;
import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

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
        UserDO userByName = userMapper.getUserByName(username);
        userInfoMapper.insert(new UserInfoDO().setPhoto("http://175.178.3.46:9000/dong/123.jpg")
                .setUserName(username)
                .setUserId(userByName.getId())
                .setDeleted(YesOrNoEnum.NO.getCode()));
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

    @Override
    public UserInfoVO queryUserInfo(Long userId,HttpServletRequest httpServletRequest) {
        UserInfoVO userInfoVO = new UserInfoVO();
        if (userId==null){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS, "userId");
        }
        //只能查询本人的个人信息
//        if (!ReqInfoContext.getReqInfo().getUserId().equals(userId)){
//            userId = ReqInfoContext.getReqInfo().getUserId();
//        }
        UserInfoDO userInfoDO = userInfoMapper.getUserId(userId);
        if (userInfoDO==null){
            return null;
        }
        BeanUtils.copyProperties(userInfoDO, userInfoVO);
        // 用户资料完整度
        int cnt = 0;
        if (StringUtils.isNotBlank(userInfoDO.getCompany())) {
            ++cnt;
        }
        if (StringUtils.isNotBlank(userInfoDO.getPosition())) {
            ++cnt;
        }
        if (StringUtils.isNotBlank(userInfoDO.getProfile())) {
            ++cnt;
        }
        userInfoVO.setInfoPercent(cnt * 100 / 3);
        // 加入天数
        int joinDayCount = (int) ((System.currentTimeMillis() - userInfoDO.getCreateTime()
                .getTime()) / (1000 * 3600 * 24));
        userInfoVO.setJoinDayCount(Math.max(1, joinDayCount));
        userInfoVO.setRegion(ip2regionSearcher.search(IpUtil.getClientIp(httpServletRequest)).getAddress());
        return userInfoVO;
    }

    @Override
    public void saveUserInfo(UserInfoVO userInfoVO) {
        UserInfoDO userInfoDO = UserConvert.INSTANCE.convert(userInfoVO);
        if (userInfoVO.getUserId()==null){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS,"userId");
        }
        UserInfoDO userId = userInfoMapper.getUserId(userInfoVO.getUserId());
        if (!ObjectUtil.isNotNull(userId)){
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS,"信息不存在");
        }
        userInfoDO.setId(userId.getId());
        if (StringUtils.isEmpty(userInfoDO.getUserName())){
            userInfoDO.setUserName(null);
        }
        if (StringUtils.isEmpty(userInfoDO.getPhoto())){
            userInfoDO.setPhoto(null);
        }
        userInfoMapper.updateById(userInfoDO);
    }
}
