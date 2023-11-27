package cn.github.iocoder.dong.core.helper;


import cn.github.iocoder.dong.core.helper.dto.RabbitMqDTO;
import cn.github.iocoder.dong.model.context.ReqInfoContext;
import cn.github.iocoder.dong.controller.vo.ChatRecordsVo;
import cn.github.iocoder.dong.model.enums.AISourceEnum;
import cn.github.iocoder.dong.service.chat.ChatFacade;
import cn.github.iocoder.dong.service.global.GlobalInitService;
import cn.github.iocoder.dong.service.user.service.UserService;
import cn.github.iocoder.dong.core.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

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
        SpringUtil.getBean(GlobalInitService.class).initLoginUser(rabbitMqDTO.getSession(),reqInfo);
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
