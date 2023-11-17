package cn.github.iocoder.dong.service;

import cn.github.iocoder.dong.context.ReqInfoContext;
import cn.github.iocoder.dong.controller.vo.ChatRecordsVo;
import cn.github.iocoder.dong.enums.AISourceEnum;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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
    public ChatRecordsVo autoChat(AISourceEnum source, String question, Consumer<ChatRecordsVo> callback) {
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
    public ChatRecordsVo chat(AISourceEnum source, String question) {
        return gptServiceFactory.getChatService(source).chat(ReqInfoContext.getReqInfo().getUserId(), question);
    }

    /**
     * 开始聊天
     *
     * @param question
     * @param source
     * @return
     */
    public ChatRecordsVo chat(AISourceEnum source, String question, Consumer<ChatRecordsVo> callback) {
        return gptServiceFactory.getChatService(source)
                .chat(ReqInfoContext.getReqInfo().getUserId(), question, callback);
    }

    /**
     * 异步聊天的方式
     *
     * @param source
     * @param question
     */
    public ChatRecordsVo asyncChat(AISourceEnum source, String question, Consumer<ChatRecordsVo> callback) {
        return gptServiceFactory.getChatService(source)
                .asyncChat(ReqInfoContext.getReqInfo().getUserId(), question, callback);
    }


    /**
     * 返回历史聊天记录
     *
     * @param source
     * @return
     */
    public ChatRecordsVo history(AISourceEnum source) {
        return gptServiceFactory.getChatService(source).getChatHistory(ReqInfoContext.getReqInfo().getUserId(),source);
    }

}
