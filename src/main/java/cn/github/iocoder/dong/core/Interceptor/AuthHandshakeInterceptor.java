package cn.github.iocoder.dong.core.Interceptor;


import cn.github.iocoder.dong.core.helper.WsAnswerHelper;
import cn.github.iocoder.dong.model.context.AISourceThreadLocalContext;
import cn.github.iocoder.dong.model.context.ReqInfoContext;
import cn.github.iocoder.dong.model.enums.AISourceEnum;
import cn.github.iocoder.dong.service.global.GlobalInitService;
import cn.github.iocoder.dong.service.user.service.UserService;
import cn.github.iocoder.dong.core.utils.SessionUtil;
import cn.github.iocoder.dong.core.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * 握手拦截器, 用于身份验证识别
 *
 * @author dong
 * @date 2023/11/16
 */
@Slf4j
public class AuthHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    /**
     * 握手前，进行用户身份校验识别
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes: 即对应的是Message中的 simpSessionAttributes 请求头
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("准备开始握手了!");
        String session = SessionUtil.findCookieByName(request, "f-session");
        ReqInfoContext.ReqInfo reqInfo = new ReqInfoContext.ReqInfo();
        SpringUtil.getBean(GlobalInitService.class).initLoginUser(session, reqInfo);

        if (reqInfo.getUser() == null) {
            log.info("websocket 握手失败，请登录之后再试");
            return false;
        }
        // 将用户信息写入到属性中
        attributes.put("f-session", reqInfo);
        String ai = initAiSource(request.getURI().getPath());
        reqInfo.setAi(ai);
        attributes.put(WsAnswerHelper.AI_SOURCE_PARAM, ai);
        return true;
    }

    private String initAiSource(String path) {
        int index = path.lastIndexOf("/");
        return path.substring(index + 1);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        log.info("握手成功了!!!");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
