package org.example.packets.handler;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MessageReactionRespBody {

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 房间号
     */
    private String roomId;

    /**
     * 表情回复
     */
    private Map<String, List<String>> reactions;

}
