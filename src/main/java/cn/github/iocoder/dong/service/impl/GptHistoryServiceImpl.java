package cn.github.iocoder.dong.service.impl;

import cn.github.iocoder.dong.controller.vo.ChatItemVo;
import cn.github.iocoder.dong.entity.GptHistory;
import cn.github.iocoder.dong.enums.AISourceEnum;
import cn.github.iocoder.dong.mapper.GptHistoryMapper;
import cn.github.iocoder.dong.service.GptHistoryService;
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
    public void pushChatItem(AISourceEnum source, String user, ChatItemVo item) {
        GptHistory userAiHistoryDO = new GptHistory();
        userAiHistoryDO.setAiType(source.getName());
        userAiHistoryDO.setIp(user);
        userAiHistoryDO.setQuestion(item.getQuestion());
        userAiHistoryDO.setAnswer(item.getAnswer());
        gptHistoryMapper.insert(userAiHistoryDO);
    }
}
