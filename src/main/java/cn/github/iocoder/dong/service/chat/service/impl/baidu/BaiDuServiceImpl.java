package cn.github.iocoder.dong.service.chat.service.impl.baidu;

import cn.github.iocoder.dong.controller.vo.ChatItemVO;
import cn.github.iocoder.dong.controller.vo.ChatRecordsVO;
import cn.github.iocoder.dong.model.enums.AISourceEnum;
import cn.github.iocoder.dong.model.enums.AiChatStatEnum;
import cn.github.iocoder.dong.service.chat.service.AbstractGptService;

import java.util.function.BiConsumer;

public class BaiDuServiceImpl extends AbstractGptService {
    /**
     * 异步返回结果
     *
     * @param user
     * @param response 保存提问 & 返回的结果，最终会返回给前端用户
     * @param consumer 具体将 response 写回前端的实现策略
     * @return 返回的会话状态，控制是否需要将结果直接返回给前端
     */
    @Override
    public AiChatStatEnum doAsyncAnswer(Long user, ChatRecordsVO response, BiConsumer<AiChatStatEnum, ChatRecordsVO> consumer) {
        return null;
    }

    /**
     * 提问，并将结果写入chat
     *
     * @param user
     * @param chat
     * @return true 表示正确回答了； false 表示回答出现异常
     */
    @Override
    public AiChatStatEnum doAnswer(Long user, ChatItemVO chat) {
        return null;
    }

    /**
     * 具体AI选择
     *
     * @return
     */
    @Override
    public AISourceEnum source() {
        return AISourceEnum.BAI_DU_AI;
    }
}
