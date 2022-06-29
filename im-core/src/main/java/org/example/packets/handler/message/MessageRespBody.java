package org.example.packets.handler.message;

import lombok.Data;
import org.example.enums.MessageFetchTypeEnum;

import java.util.List;

@Data
public class MessageRespBody {

    /**
     * 消息ID
     */
    private MessageFetchTypeEnum type;

    /**
     * 回归正常的第一次请求
     */
    private Boolean returnDefault;

    /**
     * 消息列表
     */
    private List<ChatRespBody> messages;
}
