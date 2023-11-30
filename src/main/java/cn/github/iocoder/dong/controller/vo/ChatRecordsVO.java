package cn.github.iocoder.dong.controller.vo;

import cn.github.iocoder.dong.model.enums.AISourceEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 聊天记录
 *
 * @author dong
 * @date 2023/11/16
 */
@Data
@Accessors(chain = true)
public class ChatRecordsVO implements Serializable, Cloneable {
    private static final long serialVersionUID = -2666259615985932920L;
    /**
     * AI来源
     */
    private AISourceEnum source;

    /**
     * 当前用户最多可问答的次数
     */
    private int maxCnt;

    /**
     * 使用的次数
     */
    private int usedCnt;

    /**
     * 聊天记录，最新的在前面；最多返回50条
     */
    private List<ChatItemVO> records;

    @Override
    public ChatRecordsVO clone() {
        ChatRecordsVO vo = new ChatRecordsVO();
        vo.source = source;
        vo.maxCnt = maxCnt;
        vo.usedCnt = usedCnt;
        if (records != null) {
            vo.setRecords(records.stream().map(ChatItemVO::clone).collect(Collectors.toList()));
        }
        return vo;
    }

    /**
     * 判断是否拥有提问次数
     *
     * @return true 表示拥有提问次数
     */
    public boolean hasQaCnt() {
        return maxCnt > usedCnt;
    }
}
