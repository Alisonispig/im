package org.example.packets;

import lombok.Builder;

import java.util.List;

@Builder
public class Group {

    /**
     * 群组ID
     */
    private String roomId;

    /**
     * 群组名称
     */
    private String roomName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 最后一条消息
     */
    private LastMessage lastMessage;

    /**
     * 组用户
     */
    private List<User> users;
}
