package org.example.packets;

import lombok.Data;

@Data
public class ChatRepBody {

    /**
     * 消息ID
     */
    private long _id;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送者ID
     */
    private String senderId;

    /**
     * 房间ID
     */
    private String roomId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 年月日
     */
    private String date;

    /**
     * 00:00
     */
    private String timestamp;

    /**
     * 是否系统消息
     */
    private Boolean system;

    /**
     * 是否已保存
     */
    private Boolean saved;

    /**
     * 是否删除的消息
     */
    private Boolean deleted;

    private Boolean disableActions;

    private Boolean disableReactions;
}
