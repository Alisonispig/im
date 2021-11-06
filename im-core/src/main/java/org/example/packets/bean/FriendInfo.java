package org.example.packets.bean;

import lombok.Data;

/**
 * 好友信息
 */
@Data
public class FriendInfo {

    /**
     * 房间号
     */
    private String roomId;

    /**
     * 自己的Id
     */
    private String self;

    /**
     * 好友ID
     */
    private String friendId;

    /**
     * 好友备注
     */
    private String remark;
}
