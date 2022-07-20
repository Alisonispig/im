package org.example.packets.handler.message;

import lombok.Data;
import org.example.packets.bean.Message;

import java.util.List;

@Data
public class MessageForwardReqBody {

    /**
     * 会话列表
     */
    private List<String> chats;

    /**
     * 用户列表
     */
    private List<String> users;

    /**
     * 消息列表
     */
    private List<Message> messages;

}
