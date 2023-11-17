package cn.github.iocoder.dong.chat.helper;


import cn.github.iocoder.dong.chat.rest.dto.RabbitMqDTO;
import cn.github.iocoder.dong.context.ReqInfoContext;
import cn.github.iocoder.dong.controller.vo.ChatRecordsVo;
import cn.github.iocoder.dong.enums.AISourceEnum;
import cn.github.iocoder.dong.service.ChatFacade;
import cn.github.iocoder.dong.service.GptUserService;
import cn.github.iocoder.dong.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dong
 * @date 2023/11/16
 */
@Slf4j
@Component
public class WsAnswerHelper {
    public static final String AI_SOURCE_PARAM = "AI";

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatFacade chatFacade;


    public Boolean sendMsgToUser(RabbitMqDTO rabbitMqDTO) {
        ReqInfoContext.ReqInfo reqInfo = new ReqInfoContext.ReqInfo();
        SpringUtil.getBean(GptUserService.class).initLoginUser(rabbitMqDTO.getSession(),reqInfo);
        ReqInfoContext.addReqInfo(reqInfo);
        try {
            ChatRecordsVo res = chatFacade.autoChat(AISourceEnum.valueOf(rabbitMqDTO.getAiType()), rabbitMqDTO.getMsg(), vo -> response(rabbitMqDTO.getSession(), vo));
            log.info("AI直接返回：{}", res);
        }finally {
            ReqInfoContext.clear();
        }
        return true;
    }

    public void sendMsgHistoryToUser(String session, AISourceEnum ai) {
        ChatRecordsVo vo = chatFacade.history(ai);
        response(session, vo);
    }

    /**
     * 将返回结果推送给用户
     *
     * @param session
     * @param response
     */
    public void response(String session, ChatRecordsVo response) {
        // convertAndSendToUser 方法可以发送信给给指定用户,
        // 底层会自动将第二个参数目的地址 /chat/rsp 拼接为
        // /user/username/chat/rsp，其中第二个参数 username 即为这里的第一个参数 session
        // username 也是AuthHandshakeHandler中配置的 Principal 用户识别标志
        simpMessagingTemplate.convertAndSendToUser(session, "/chat/rsp", response);
    }
}
