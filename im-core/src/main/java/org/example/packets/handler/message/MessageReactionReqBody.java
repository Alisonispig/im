package org.example.packets.handler.message;

import lombok.Data;

/**
 * 表情回复消息
 */
@Data
public class MessageReactionReqBody {

    /**
     * 表情unicode
     */
    private String reaction;

    /**
     * 是否删除
     */
    private Boolean remove;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 房间号
     */
    private String roomId;

}
