package cn.github.iocoder.dong.service.chat.service.impl.alibaba;

import cn.github.iocoder.dong.controller.vo.ChatItemVo;
import cn.github.iocoder.dong.controller.vo.ChatRecordsVo;
import cn.github.iocoder.dong.model.enums.AISourceEnum;
import cn.github.iocoder.dong.model.enums.AiChatStatEnum;
import cn.github.iocoder.dong.model.enums.ChatAnswerTypeEnum;
import cn.github.iocoder.dong.service.chat.service.AbstractGptService;
import com.alibaba.dashscope.aigc.conversation.Conversation;
import com.alibaba.dashscope.aigc.conversation.ConversationParam;
import com.alibaba.dashscope.aigc.conversation.ConversationResult;
import com.alibaba.dashscope.common.ResultCallback;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

@Service
@Slf4j
public class AlibabaQianWenServiceImpl extends AbstractGptService {

    @Value("${gpt-ai.alibaba.key}")
    private String key;


    /**
     * 异步返回结果
     *
     * @param user
     * @param chatRes 保存提问 & 返回的结果，最终会返回给前端用户
     * @param consumer 具体将 response 写回前端的实现策略
     * @return 返回的会话状态，控制是否需要将结果直接返回给前端
     */
    @Override
    public AiChatStatEnum doAsyncAnswer(Long user, ChatRecordsVo chatRes, BiConsumer<AiChatStatEnum, ChatRecordsVo> consumer) {
        ChatItemVo item = chatRes.getRecords().get(0);
        Conversation conversation = new Conversation();
        Constants.apiKey = key;
        ConversationParam param = ConversationParam
                .builder()
                .model(Conversation.Models.QWEN_MAX)
                .prompt(item.getQuestion())
                .build();
        try{
            conversation.streamCall(param, new ResultCallback<ConversationResult>() {
                @Override
                public void onEvent(ConversationResult conversationResult) {
                    item.appendAnswer1(conversationResult.getOutput().getText());
                    consumer.accept(AiChatStatEnum.MID, chatRes);
                }

                @Override
                public void onComplete() {
                    item.appendAnswer("\n")
                            .setAnswerType(ChatAnswerTypeEnum.STREAM_END);
                    consumer.accept(AiChatStatEnum.END, chatRes);
                }

                @Override
                public void onError(Exception e) {
                    item.appendAnswer("Error:" + e.getMessage()).setAnswerType(ChatAnswerTypeEnum.STREAM_END);
                    consumer.accept(AiChatStatEnum.ERROR, chatRes);
                }
            });
        }catch(ApiException | NoApiKeyException | InputRequiredException ex){
            System.out.println(ex.getMessage());
        }

        return AiChatStatEnum.IGNORE;
    }

    /**
     * 提问，并将结果写入chat
     *
     * @param user
     * @param chat
     * @return true 表示正确回答了； false 表示回答出现异常
     */
    @Override
    public AiChatStatEnum doAnswer(Long user, ChatItemVo chat) {
        if (directReturn(chat)){
            return AiChatStatEnum.END;
        }
        return AiChatStatEnum.ERROR;
    }

    /**
     * 具体AI选择
     *
     * @return
     */
    @Override
    public AISourceEnum source() {
        return AISourceEnum.ALIBABA_QIAN_WEN;
    }

    /**
     * 是否时异步优先
     *
     * @return
     */
    @Override
    public boolean asyncFirst() {
        return true;
    }

    public boolean directReturn( ChatItemVo chat) {
        Constants.apiKey = key;
        Conversation conversation = new Conversation();
        String prompt = chat.getQuestion();
        ConversationParam param = ConversationParam
                .builder()
                .model(Conversation.Models.QWEN_TURBO)
                .prompt(prompt)
                .build();
        ConversationResult result = null;
        try {
            result = conversation.call(param);
            String text = result.getOutput().getText();
            chat.initAnswer(text, ChatAnswerTypeEnum.TEXT);
            log.info("chatgpt试用! 传参:{}, 返回:{}", chat, text);
            return true;
        } catch (NoApiKeyException | InputRequiredException e) {
            chat.initAnswer(e.getMessage());
            log.info("chatgpt执行异常！ key:{}", chat, e);
            return false;
        }
    }
}
