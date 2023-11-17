package cn.github.iocoder.dong.service;

import cn.github.iocoder.dong.context.ReqInfoContext;

/**
 * 用户的接口
 */
public interface GptUserService {

    /**
     * 初始话用户
     * @param session
     * @param reqInfo
     */
    void initLoginUser(String session, ReqInfoContext.ReqInfo reqInfo);
}
