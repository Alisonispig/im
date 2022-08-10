package org.example.packets.handler.room;

import lombok.Data;

@Data
public class SearchRoomReqBody {

    /**
     * 用户名称
     */
    private String name;

    /**
     * 搜索ID
     */
    private String searchId;

    /**
     * 最后一个用户的ID
     */
    private String roomId;
}
