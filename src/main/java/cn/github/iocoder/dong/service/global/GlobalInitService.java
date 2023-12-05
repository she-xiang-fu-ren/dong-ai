package cn.github.iocoder.dong.service.global;

import cn.github.iocoder.dong.core.helper.UserSessionHelper;
import cn.github.iocoder.dong.model.context.ReqInfoContext;
import cn.github.iocoder.dong.model.context.dto.UserDTO;
import cn.github.iocoder.dong.model.convert.UserConvert;
import cn.github.iocoder.dong.service.global.vo.GlobalVo;
import cn.github.iocoder.dong.service.user.repository.entity.UserDO;
import cn.github.iocoder.dong.service.user.repository.entity.UserInfoDO;
import cn.github.iocoder.dong.service.user.repository.mapper.UserInfoMapper;
import cn.github.iocoder.dong.service.user.repository.mapper.UserMapper;
import cn.github.iocoder.dong.service.user.service.UserStatisticService;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class GlobalInitService {

    @Autowired
    private UserStatisticService userStatisticService;

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserSessionHelper userSessionHelper;

    /**
     * 全局属性配置
     */
    public GlobalVo globalAttr() {
        GlobalVo vo = new GlobalVo();
        vo.setOnlineCnt(userStatisticService.getOnlineUserCnt());
        try {
            if (ReqInfoContext.getReqInfo() != null && ReqInfoContext.getReqInfo().getUserId()!=null){
                UserInfoDO userInfoDO = userInfoMapper.getUserId(ReqInfoContext.getReqInfo().getUserId());
                UserDTO user = ReqInfoContext.getReqInfo().getUser();
                user.setPhotoUrl(userInfoDO.getPhoto());
                vo.setUser(user);
                vo.setIsLogin(true);
            }else {
                vo.setIsLogin(false);
            }
        }catch (Exception e){
            log.error("loginCheckError:", e);
        }
        return vo;
    }

    /**
     * 初始话用户
     *
     * @param session
     * @param reqInfo
     */
    public void initLoginUser(String session, ReqInfoContext.ReqInfo reqInfo) {
        //判断session是否为空
        if (ObjectUtil.isNotNull(session)) {
            Long userId = userSessionHelper.getUserIdBySession(session);
            UserDO userDO = userMapper.selectById(userId);
            UserDTO userDTO = UserConvert.INSTANCE.convert(userDO);
            if (ObjectUtil.isNotNull(userDO)) {
                reqInfo.setSession(session);
                reqInfo.setUserId(userId);
                reqInfo.setUser(userDTO);
            }
        }
    }
}
