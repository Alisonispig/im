package org.example.packets.handler.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.packets.bean.Message;

@EqualsAndHashCode(callSuper = true)
@Data
public class MessageDeleteRespBody extends Message {

    /**
     * 是否最后一条消息, 决定是否要构建消息体
     */
    private Boolean isLastMessage;

    private String deleteUserName;
}
