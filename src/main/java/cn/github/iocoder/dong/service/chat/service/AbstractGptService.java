package cn.github.iocoder.dong.service.chat.service;

import cn.github.iocoder.dong.model.context.ChatConstants;
import cn.github.iocoder.dong.controller.vo.ChatItemVo;
import cn.github.iocoder.dong.controller.vo.ChatRecordsVo;
import cn.github.iocoder.dong.model.enums.AISourceEnum;
import cn.github.iocoder.dong.model.enums.AiChatStatEnum;
import cn.github.iocoder.dong.core.utils.RedisClient;
import cn.github.iocoder.dong.service.history.service.GptHistoryService;
import cn.github.iocoder.dong.service.chat.service.GptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Gpt服务抽象类
 */
@Slf4j
@Service
public abstract class AbstractGptService implements GptService {

    @Resource
    private GptHistoryService gptHistoryService;

    /**
     * 查询已经使用的次数
     *
     * @param user
     * @return
     */
    protected int queryUserdCnt(Long user) {
        Integer cnt = RedisClient.hGet(ChatConstants.getAiRateKeyPerDay(source()), user.toString(), Integer.class);
        if (cnt == null) {
            cnt = 0;
        }
        return cnt;
    }


    /**
     * 使用次数+1
     *
     * @param user
     * @return
     */
    protected Long incrCnt(Long user) {
        String key = ChatConstants.getAiRateKeyPerDay(source());
        Long cnt = RedisClient.hIncr(key, String.valueOf(user), 1);
        if (cnt == 1L) {
            // 做一个简单的判定，如果是某个用户的第一次提问，那就刷新一下这个缓存的有效期
            // fixme 这里有个不太优雅的地方：每新来一个用户，会导致这个有效期重新刷一边，可以通过再查一下hash的key个数，如果只有一个才进行重置有效期；这里出于简单考虑，省了这一步
            RedisClient.expire(key, 86400L);
        }
        return cnt;
    }

    /**
     * 保存聊天记录
     */
    protected void recordChatItem(Long user, ChatItemVo item) {
        // 写入 MySQL
        gptHistoryService.pushChatItem(source(), user, item);

        // 写入 Redis
        RedisClient.lPush(ChatConstants.getAiHistoryRecordsKey(source(), user), item);
    }

    /**
     * 查询用户的聊天历史
     *
     * @return
     */
    public ChatRecordsVo getChatHistory(Long user, AISourceEnum source) {
        List<ChatItemVo> chats = RedisClient.lRange(ChatConstants.getAiHistoryRecordsKey(source(), user), 0, 50, ChatItemVo.class);
        chats.add(0, new ChatItemVo().initAnswer("开始你的AI之旅吧!"));
        ChatRecordsVo vo = new ChatRecordsVo();
        vo.setMaxCnt(getMaxQaCnt(user));
        vo.setUsedCnt(queryUserdCnt(user));
        vo.setSource(source());
        vo.setRecords(chats);
        return vo;
    }

    /**
     * 开始进入聊天
     *
     * @param user     提问人
     * @param question 聊天的问题
     * @return 返回的结果
     */
    @Override
    public ChatRecordsVo chat(Long user, String question) {
        // 构建提问、返回的实体类，计算使用次数，最大次数
        ChatRecordsVo res = initResVo(user, question);
        if (!res.hasQaCnt()) {
            return res;
        }

        // 执行提问
        answer(user, res);
        // 返回AI应答结果
        return res;
    }

    /**
     * 开始进入聊天
     *
     * @param user     提问人
     * @param question 聊天的问题
     * @param consumer 接收到AI返回之后可执行的回调
     * @return 同步直接返回的结果
     */
    @Override
    public ChatRecordsVo chat(Long user, String question, Consumer<ChatRecordsVo> consumer) {
        ChatRecordsVo res = initResVo(user, question);
        if (!res.hasQaCnt()) {
            return res;
        }

        // 同步聊天时，直接返回结果
        answer(user, res);
        consumer.accept(res);
        return res;
    }


    private ChatRecordsVo initResVo(Long user, String question) {
        ChatRecordsVo res = new ChatRecordsVo();
        res.setSource(source());
        int maxCnt = getMaxQaCnt(user);
        int usedCnt = queryUserdCnt(user);
        res.setMaxCnt(maxCnt);
        res.setUsedCnt(usedCnt);

        ChatItemVo item = new ChatItemVo().initQuestion(question);
        if (!res.hasQaCnt()) {
            // 次数已经使用完毕
            item.initAnswer(ChatConstants.TOKEN_OVER);
        }
        res.setRecords(Arrays.asList(item));
        return res;
    }

    protected AiChatStatEnum answer(Long user, ChatRecordsVo res) {
        ChatItemVo itemVo = res.getRecords().get(0);
        AiChatStatEnum ans;
//        if (sensitiveService.contains(itemVo.getQuestion())) {
        if (false){
            itemVo.initAnswer(ChatConstants.SENSITIVE_QUESTION);
            ans = AiChatStatEnum.ERROR;
        } else {
            ans = doAnswer(user, itemVo);
            if (ans == AiChatStatEnum.END) {
                processAfterSuccessedAnswered(user, res);
            }
        }
        return ans;
    }


    /**
     * 异步聊天
     *
     * @param user
     * @param question
     * @param consumer 执行成功之后，直接异步回调的通知
     * @return 同步直接返回的结果
     */
    @Override
    public ChatRecordsVo asyncChat(Long user, String question, Consumer<ChatRecordsVo> consumer) {
        ChatRecordsVo res = initResVo(user, question);
        if (!res.hasQaCnt()) {
            // 次数使用完毕
            consumer.accept(res);
            return res;
        }
        if (false){
//        if (sensitiveService.contains(res.getRecords().get(0).getQuestion())) {
            // 包含敏感词的提问，直接返回异常
            res.getRecords().get(0).initAnswer(ChatConstants.SENSITIVE_QUESTION);
            consumer.accept(res);
        } else {
            final ChatRecordsVo newRes = res.clone();
            AiChatStatEnum needReturn = doAsyncAnswer(user, newRes, (ans, vo) -> {
                if (ans == AiChatStatEnum.END) {
                    // 只有最后一个会话，即ai的回答结束，才需要进行持久化，并计数
                    processAfterSuccessedAnswered(user, newRes);
                } else if (ans == AiChatStatEnum.ERROR) {
                    // 执行异常，更新AI模型

                }
                // ai异步返回结果之后，我们将结果推送给前端用户
                consumer.accept(newRes);
            });

            if (needReturn.needResponse()) {
                // 异步响应时，为了避免长时间的等待，这里直接响应用户的提问，返回一个稍等得提示文案
                ChatItemVo nowItem = res.getRecords().get(0);
                nowItem.initAnswer(ChatConstants.ASYNC_CHAT_TIP);
                consumer.accept(res);
            }
        }
        return null;
    }


    /**
     * 异步返回结果
     *
     * @param user
     * @param response 保存提问 & 返回的结果，最终会返回给前端用户
     * @param consumer 具体将 response 写回前端的实现策略
     * @return 返回的会话状态，控制是否需要将结果直接返回给前端
     */
    public abstract AiChatStatEnum doAsyncAnswer(Long user, ChatRecordsVo response, BiConsumer<AiChatStatEnum, ChatRecordsVo> consumer);


    /**
     * 提问，并将结果写入chat
     *
     * @param user
     * @param chat
     * @return true 表示正确回答了； false 表示回答出现异常
     */
    public abstract AiChatStatEnum doAnswer(Long user, ChatItemVo chat);

    /**
     * 查询当前用户最多可提问的次数（给所有用户默认设置50次访问）
     *
     * @param user
     * @return
     */
    protected int getMaxQaCnt(Long user) {
        return 50;
    }

    /**
     * 成功返回之后的后置操作
     *
     * @param user
     * @param response
     */
    protected void processAfterSuccessedAnswered(Long user, ChatRecordsVo response) {
        // 回答成功，保存聊天记录，剩余次数-1
        response.setUsedCnt(incrCnt(user).intValue());
        recordChatItem(user, response.getRecords().get(0));
        if (response.getUsedCnt() > ChatConstants.MAX_HISTORY_RECORD_ITEMS) {
            // 最多保存五百条历史聊天记录
            RedisClient.lTrim(ChatConstants.getAiHistoryRecordsKey(source(), user), 0, ChatConstants.MAX_HISTORY_RECORD_ITEMS);
        }
    }
}
