package cn.github.iocoder.dong.service;

import cn.github.iocoder.dong.controller.vo.ChatRecordsVo;
import cn.github.iocoder.dong.enums.AISourceEnum;

import java.util.function.Consumer;

/**
 * Gpt服务接口
 */
public interface GptService {
    /**
     * 具体AI选择
     *
     * @return
     */
    AISourceEnum source();

    /**
     * 是否时异步优先
     *
     * @return
     */
    default boolean asyncFirst() {
        return true;
    }

    /**
     * 开始进入聊天
     *
     * @param user     提问人
     * @param question 聊天的问题
     * @return 返回的结果
     */
    ChatRecordsVo chat(String user, String question);

    /**
     * 开始进入聊天
     *
     * @param user     提问人
     * @param question 聊天的问题
     * @param consumer 接收到AI返回之后可执行的回调
     * @return 同步直接返回的结果
     */
    ChatRecordsVo chat(String user, String question, Consumer<ChatRecordsVo> consumer);

    /**
     * 异步聊天
     *
     * @param user
     * @param question
     * @param consumer 执行成功之后，直接异步回调的通知
     * @return 同步直接返回的结果
     */
    ChatRecordsVo asyncChat(String user, String question, Consumer<ChatRecordsVo> consumer);


    /**
     * 查询聊天历史
     *
     * @param user
     * @return
     */
    ChatRecordsVo getChatHistory(String user);
}
