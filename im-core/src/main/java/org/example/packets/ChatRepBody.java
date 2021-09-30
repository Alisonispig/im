package org.example.packets;

import lombok.Data;

@Data
public class ChatRepBody {

    /**
     * 消息ID
     */
    private Integer _id;

    /**
     * 消息内容
     */
    private String content;

    /**
     *
     */
    private String senderId;


}
