package cn.github.iocoder.dong.service.chat.factory;

import cn.github.iocoder.dong.model.enums.AISourceEnum;
import cn.github.iocoder.dong.service.chat.service.GptService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author dong
 * @date 2023/11/16
 */
@Component
public class GptServiceFactory {
    private final Map<AISourceEnum, GptService> chatServiceMap;


    public GptServiceFactory(List<GptService> chatServiceList) {
        chatServiceMap = Maps.newHashMapWithExpectedSize(chatServiceList.size());
        for (GptService chatService : chatServiceList) {
            chatServiceMap.put(chatService.source(), chatService);
        }
    }

    public GptService getChatService(AISourceEnum aiSource) {
        return chatServiceMap.get(aiSource);
    }
}
