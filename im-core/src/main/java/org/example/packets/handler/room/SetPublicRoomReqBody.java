package org.example.packets.handler.room;

import lombok.Data;

@Data
public class SetPublicRoomReqBody {

    private String roomId;

    private Boolean publicRoom;
}
