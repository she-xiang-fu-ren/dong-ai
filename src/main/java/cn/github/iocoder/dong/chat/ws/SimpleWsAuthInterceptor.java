package cn.github.iocoder.dong.chat.ws;


import cn.github.iocoder.dong.context.ReqInfoContext;
import cn.github.iocoder.dong.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * v1. 简单版本聊天： 长连接的登录校验拦截器
 *
 * @author dong
 * @date 2023/11/16
 */
@Slf4j
public class SimpleWsAuthInterceptor extends HttpSessionHandshakeInterceptor implements ChannelInterceptor {

    @Override
    public boolean preReceive(MessageChannel channel) {
        return ChannelInterceptor.super.preReceive(channel);
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String session = ((ServletServerHttpRequest) request).getServletRequest().getParameter("session");
        ReqInfoContext.ReqInfo reqInfo = new ReqInfoContext.ReqInfo();
//        SpringUtil.getBean(GlobalInitService.class).initLoginUser(session, reqInfo);
        ReqInfoContext.addReqInfo(reqInfo);
        if (reqInfo.getUserId() == null) {
            // 未登录，拒绝链接
            log.info("用户未登录，拒绝聊天!");
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }
        log.info("{} 开始了聊天!", reqInfo);
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        ReqInfoContext.clear();
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
