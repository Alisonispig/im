package org.example.packets.handler;

import lombok.Data;

@Data
public class ChatReqBody {

    /**
     * 消息ID
     */
    private long _id;

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

    /**
     * 发送日期
     */
    private String date;

    /**
     * 发送时间
     */
    private String timestamp;

}
