package org.example.packets.bean;

import lombok.Data;
import org.example.enums.RoomRoleEnum;

@Data
public class UserGroup {

    /**
     * 用户iD
     */
    private String userId;

    /**
     * 群组ID
     */
    private String roomId;

    /**
     * 当前群组角色
     */
    private RoomRoleEnum role;

    /**
     * 房间是否删除
     */
    private Boolean roomDeleted;
}
