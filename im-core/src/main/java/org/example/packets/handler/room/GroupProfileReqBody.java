package org.example.packets.handler.room;

import lombok.Data;

@Data
public class GroupProfileReqBody {

    private String roomId;

    private String roomName;

    private String avatar;

}
