package org.example.packets.handler;

import lombok.Data;

@Data
public class CreateGroupReqBody {

    /**
     * 群组名称
     */
    private String roomName;
}
