package org.example.packets;

import lombok.Data;

@Data
public class ChatReqBody {

    /**
     * 发送者编号
     */
    private String senderId;

    /**
     * 房间号
     */
    private String roomId;

    /**
     * 内容
     */
    private String content;

}
