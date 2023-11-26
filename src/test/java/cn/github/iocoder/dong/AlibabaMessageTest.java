package cn.github.iocoder.dong;


import com.alibaba.dashscope.aigc.conversation.Conversation;
import com.alibaba.dashscope.aigc.conversation.ConversationParam;
import com.alibaba.dashscope.aigc.conversation.ConversationResult;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.ResultCallback;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import com.alibaba.dashscope.utils.Constants;
import io.reactivex.Flowable;

import java.util.List;
import java.util.concurrent.Semaphore;

//@Slf4j
public class AlibabaMessageTest {


    public static void callWithMessage() throws NoApiKeyException, ApiException, InputRequiredException {
        Generation gen = new Generation();
        MessageManager msgManager = new MessageManager(10);
        Message systemMsg =
                Message.builder().role(Role.SYSTEM.getValue()).content("You are a helpful assistant.").build();
        Message userMsg = Message.builder().role(Role.USER.getValue()).content("如何做西红柿鸡蛋？").build();
        msgManager.add(systemMsg);
        msgManager.add(userMsg);
        Constants.apiKey = "sk-e3c60119978d4a8885b451dcc48d22b5";
        QwenParam param =
                QwenParam.builder().model(Generation.Models.QWEN_MAX).messages(msgManager.get())
                        .resultFormat(QwenParam.ResultFormat.MESSAGE)
                        .topP(0.8)
                        .enableSearch(true)
                        .build();
        GenerationResult result = gen.call(param);
        List<GenerationOutput.Choice> choices = result.getOutput().getChoices();
        System.out.println(result);
    }
    public static void callWithMessage1()
            throws NoApiKeyException, ApiException, InputRequiredException {
        Generation gen = new Generation();
        MessageManager msgManager = new MessageManager(10);
        Message systemMsg =
                Message.builder().role(Role.SYSTEM.getValue()).content("sk-e3c60119978d4a8885b451dcc48d22b5").build();
        Message userMsg = Message.builder().role(Role.USER.getValue()).content("你好，周末去哪里玩？").build();
        msgManager.add(systemMsg);
        msgManager.add(userMsg);
        Constants.apiKey = "sk-e3c60119978d4a8885b451dcc48d22b5";
        QwenParam param =
                QwenParam.builder().model(Generation.Models.QWEN_PLUS).messages(msgManager.get())
                        .resultFormat(QwenParam.ResultFormat.MESSAGE)
                        .topP(0.8)
                        .enableSearch(true)
                        .build();
        GenerationResult result = gen.call(param);
        System.out.println(result);
        msgManager.add(result);
        System.out.println(JsonUtils.toJson(result));
        param.setPrompt("找个近点的");
        param.setMessages(msgManager.get());
        result = gen.call(param);
        System.out.println(result);
        System.out.println(JsonUtils.toJson(result));
    }
    public static void callWithMessage2()
            throws NoApiKeyException, ApiException, InputRequiredException {
        Generation gen = new Generation();
        MessageManager msgManager = new MessageManager(10);
        Message userMsg = Message.builder().role(Role.USER.getValue()).content("如何做西红柿炖牛腩？").build();
        msgManager.add(userMsg);
        Constants.apiKey = "sk-e3c60119978d4a8885b451dcc48d22b5";
        QwenParam param =
                QwenParam.builder().model(Generation.Models.QWEN_MAX).messages(msgManager.get())
                        .resultFormat(QwenParam.ResultFormat.MESSAGE)
                        .topP(0.8)
                        .enableSearch(true)
                        .build();
        GenerationResult result = gen.call(param);
        System.out.println(result);
        String text = result.getOutput().getText();
        System.out.println("============================================");
        System.out.println(text);
    }
    public static void testStreamCall() throws ApiException, NoApiKeyException, InterruptedException {
        Conversation conversation = new Conversation();
        String prompt = "用萝卜、土豆、茄子做饭，给我个菜谱。";
//        Constants.apiKey = "sk-e3c60119978d4a8885b451dcc48d22b5";
        ConversationParam param = ConversationParam
                .builder()
                .model(Conversation.Models.QWEN_MAX)
                .prompt(prompt)
                .build();
        Semaphore semaphore = new Semaphore(0);
        try{
            conversation.streamCall(param, new ResultCallback<ConversationResult>() {
                @Override
                public void onEvent(ConversationResult conversationResult) {
                    System.out.println(conversationResult.getOutput().getText());
                }

                @Override
                public void onComplete() {
                    System.out.println("onComplete");
                    semaphore.release();
                }

                @Override
                public void onError(Exception e) {
                    System.out.println(e.getMessage());
                    semaphore.release();
                }
            });
            semaphore.acquire();
//            ConversationResult result = conversation.call(param);
//            System.out.println(result.getOutput().getText());

        }catch(ApiException | InputRequiredException ex){
            System.out.println(ex.getMessage());
        }
    }
    private final static String PROMPT = "用萝卜、土豆、茄子做饭，给我个菜谱。";
    public static void qwenQuickStart()
            throws NoApiKeyException, ApiException, InputRequiredException {
//        Generation gen = new Generation();
//        QwenParam param = QwenParam.builder().model(Generation.Models.QWEN_MAX).prompt(PROMPT)
//                .topP(0.8).build();
//        GenerationResult result = gen.call(param);
//        System.out.println(JsonUtils.toJson(result));
    }

    public static void qwenQuickStartCallback()
            throws NoApiKeyException, ApiException, InputRequiredException, InterruptedException {
        Generation gen = new Generation();
        QwenParam param = QwenParam.builder().model(Generation.Models.QWEN_MAX).prompt(PROMPT)
                .topP(0.8).build();
        Semaphore semaphore = new Semaphore(0);
        gen.call(param, new ResultCallback<GenerationResult>() {

            @Override
            public void onEvent(GenerationResult message) {
                System.out.println(message);
            }
            @Override
            public void onError(Exception ex){
                System.out.println(ex.getMessage());
                semaphore.release();
            }
            @Override
            public void onComplete(){
                System.out.println("onComplete");
                semaphore.release();
            }

        });
        semaphore.acquire();
    }


    public static void main(String[] args){
        try {
            Constants.apiKey = "sk-e3c60119978d4a8885b451dcc48d22b5";
            testStreamCall();
            qwenQuickStartCallback();
        } catch (ApiException | NoApiKeyException |InputRequiredException |InterruptedException e) {
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }
}
