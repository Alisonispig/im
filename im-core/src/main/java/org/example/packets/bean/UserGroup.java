package org.example.packets.bean;

import lombok.Data;

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
}
