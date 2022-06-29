package org.example.packets.handler.message;

import lombok.Data;
import org.example.enums.MessageFetchTypeEnum;

@Data
public class MessageReqBody {

    /**
     * 群组id;
     */
    private String roomId;

    /**
     * 请求消息类型
     */
    private MessageFetchTypeEnum type;
    /**
     * 起始消息ID
     */
    private String messageId;

    private Boolean returnDefault;
}
