package org.example.packets;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class Group implements Serializable {

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
