package cn.github.iocoder.dong.service.history.service;

import cn.github.iocoder.dong.controller.vo.ChatItemVo;
import cn.github.iocoder.dong.model.enums.AISourceEnum;

public interface GptHistoryService {

    /**
     * 保存聊天历史记录
     *
     * @param source
     * @param user
     * @param item
     */
    void pushChatItem(AISourceEnum source, Long user, ChatItemVo item);
}
