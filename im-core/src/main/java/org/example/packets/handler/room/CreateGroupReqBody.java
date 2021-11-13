package org.example.packets.handler.room;

import lombok.Data;
import org.example.packets.bean.User;

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
     * 头像 (群组设置)
     */
    private String avatar;

    /**
     * 群组创建时携带的人
     */
    private List<User> users;
}
