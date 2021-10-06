package org.example.packets.handler;

import lombok.Data;
import org.example.packets.User;

@Data
public class JoinGroupNotifyBody {

    /**
     * 群组ID
     */
    private String roomId;

    /**
     * 加入的用户
     */
    private User user;

    /**
     * 加入状态
     */
    private int code;
}
