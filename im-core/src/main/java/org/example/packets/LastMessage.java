package org.example.packets;

import lombok.Data;

/**
 * 最后一条消息
 */
@Data
public class LastMessage {

    /**
     * 消息主体
     */
    private String content;

    /**
     * 发送者ID
     */
    private String senderId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 发送时间 10:20
     */
    private String timestamp;

    /**
     * 是否已保存
     */
    private Boolean saved;

    /**
     * 是否
     */
    private Boolean distributed;

    private Boolean seen;

    private Boolean isNew;
}
