package cn.github.iocoder.dong.service.history.service.impl;

import cn.github.iocoder.dong.controller.vo.ChatItemVO;
import cn.github.iocoder.dong.service.history.repository.entity.GptHistory;
import cn.github.iocoder.dong.model.enums.AISourceEnum;
import cn.github.iocoder.dong.service.history.repository.mapper.GptHistoryMapper;
import cn.github.iocoder.dong.service.history.service.GptHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GptHistoryServiceImpl implements GptHistoryService {

    @Resource
    private GptHistoryMapper gptHistoryMapper;

    /**
     * 保存聊天历史记录
     *
     * @param source
     * @param user
     * @param item
     */
    @Override
    public void pushChatItem(AISourceEnum source, Long user, ChatItemVO item) {
        GptHistory userAiHistoryDO = new GptHistory();
        userAiHistoryDO.setAiType(source.getName());
        userAiHistoryDO.setUserId(user);
        userAiHistoryDO.setQuestion(item.getQuestion());
        userAiHistoryDO.setAnswer(item.getAnswer());
        gptHistoryMapper.insert(userAiHistoryDO);
    }
}
