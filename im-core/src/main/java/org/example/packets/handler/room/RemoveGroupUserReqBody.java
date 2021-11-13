package org.example.packets.handler.room;

import lombok.Data;

@Data
public class RemoveGroupUserReqBody {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 群组ID
     */
    private String roomId;

    /**
     * 移除类型: 主动退出\踢出群组
     */
    private String type;
}
