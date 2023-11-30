package cn.github.iocoder.dong.service.chat;

import cn.github.iocoder.dong.model.context.ReqInfoContext;
import cn.github.iocoder.dong.controller.vo.ChatRecordsVO;
import cn.github.iocoder.dong.model.enums.AISourceEnum;
import cn.github.iocoder.dong.service.chat.factory.GptServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.function.Consumer;

/**
 * 聊天的门面类
 *
 * @author dong
 * @date 2023/11/16
 */
@Slf4j
@Service
public class ChatFacade {

    @Resource
    private GptServiceFactory gptServiceFactory;



    /**
     * 自动根据AI的支持方式，选择同步/异步的交互方式
     *
     * @param source
     * @param question
     * @param callback
     * @return
     */
    public ChatRecordsVO autoChat(AISourceEnum source, String question, Consumer<ChatRecordsVO> callback) {
        if (source.asyncSupport() && gptServiceFactory.getChatService(source).asyncFirst()) {
            // 支持异步且异步优先的场景下，自动选择异步方式进行聊天
            return asyncChat(source, question, callback);
        }
        return chat(source, question, callback);
    }

    /**
     * 开始聊天
     *
     * @param question
     * @param source
     * @return
     */
    public ChatRecordsVO chat(AISourceEnum source, String question) {
        return gptServiceFactory.getChatService(source).chat(ReqInfoContext.getReqInfo().getUserId(), question);
    }

    /**
     * 开始聊天
     *
     * @param question
     * @param source
     * @return
     */
    public ChatRecordsVO chat(AISourceEnum source, String question, Consumer<ChatRecordsVO> callback) {
        return gptServiceFactory.getChatService(source)
                .chat(ReqInfoContext.getReqInfo().getUserId(), question, callback);
    }

    /**
     * 异步聊天的方式
     *
     * @param source
     * @param question
     */
    public ChatRecordsVO asyncChat(AISourceEnum source, String question, Consumer<ChatRecordsVO> callback) {
        return gptServiceFactory.getChatService(source)
                .asyncChat(ReqInfoContext.getReqInfo().getUserId(), question, callback);
    }


    /**
     * 返回历史聊天记录
     *
     * @param source
     * @return
     */
    public ChatRecordsVO history(AISourceEnum source) {
        return gptServiceFactory.getChatService(source).getChatHistory(ReqInfoContext.getReqInfo().getUserId(),source);
    }

}
