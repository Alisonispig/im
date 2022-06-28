package org.example.packets.handler.message;

import lombok.Data;

import java.util.List;

@Data
public class MessageSearchRespBody {

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 消息列表
     */
    private List<ChatRespBody> messages;
}
