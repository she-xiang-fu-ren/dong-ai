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
 * @author YiHui
 * @date 2023/6/9
 */
@Slf4j
@Service
public class ChatFacade {

    @Resource
    private GptServiceFactory gptServiceFactory;


    /**
     * 高度封装的AI聊天访问入口，对于使用这而言，只需要提问，定义接收返回结果的回调即可
     *
     * @param question 提出的问题
     * @param callback 定义异步聊天接口返回时的回调策略
     * @return 表示同步直接返回的结果
     */
    public ChatRecordsVo autoChat(Integer code,String question, Consumer<ChatRecordsVo> callback) {
        AISourceEnum source = getRecommendAiSource(code);
        return autoChat(source, question, callback);
    }


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
        return gptServiceFactory.getChatService(source).chat(ReqInfoContext.getReqInfo().getClientIp(), question);
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
                .chat(ReqInfoContext.getReqInfo().getClientIp(), question, callback);
    }

    /**
     * 异步聊天的方式
     *
     * @param source
     * @param question
     */
    public ChatRecordsVo asyncChat(AISourceEnum source, String question, Consumer<ChatRecordsVo> callback) {
        return gptServiceFactory.getChatService(source)
                .asyncChat(ReqInfoContext.getReqInfo().getClientIp(), question, callback);
    }

    /**
     * 自动获取用户的聊天历史
     *
     * @return
     */
    public ChatRecordsVo history(Integer code) {
        return history(getRecommendAiSource(code));
    }



    /**
     * 返回历史聊天记录
     *
     * @param source
     * @return
     */
    public ChatRecordsVo history(AISourceEnum source) {
        return gptServiceFactory.getChatService(source).getChatHistory(ReqInfoContext.getReqInfo().getClientIp());
    }

    private AISourceEnum getRecommendAiSource(Integer code) {
        switch (code){
            case 1:
                return AISourceEnum.ALIBABA_WAN_XIANG;
            case 2:
                return AISourceEnum.BAI_DU_AI;
            case 3:
                return AISourceEnum.XUN_FEI_AI_1_5;
            case 4:
                return AISourceEnum.XUN_FEI_AI_2_0;
            case 5:
                return AISourceEnum.XUN_FEI_AI_3_0;
        }
        return null;
    }

}
