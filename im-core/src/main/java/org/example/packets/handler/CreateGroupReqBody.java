package org.example.packets.handler;

import lombok.Data;
import org.example.packets.User;

import java.util.List;

@Data
public class CreateGroupReqBody {

    /**
     * 群组名称
     */
    private String roomName;

    /**
     * 是否好友会话
     */
    private Boolean isFriend;

    /**
     * 群组创建时携带的人
     */
    private List<User> users;
}
