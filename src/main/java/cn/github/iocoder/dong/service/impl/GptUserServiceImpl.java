package cn.github.iocoder.dong.service.impl;

import cn.github.iocoder.dong.config.UserSessionHelper;
import cn.github.iocoder.dong.context.ReqInfoContext;
import cn.github.iocoder.dong.entity.GptUser;
import cn.github.iocoder.dong.mapper.GptUserMapper;
import cn.github.iocoder.dong.service.GptUserService;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class GptUserServiceImpl implements GptUserService {

    @Resource
    private GptUserMapper gptUserMapper;

    @Resource
    private UserSessionHelper userSessionHelper;

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
            GptUser gptUser = gptUserMapper.selectById(userId);
            if (ObjectUtil.isNotNull(gptUser)) {
                reqInfo.setSession(session);
                reqInfo.setUserId(userId);
                reqInfo.setUser(gptUser);
                reqInfo.setLoginOrNot(true);
            }
        }
    }
}
