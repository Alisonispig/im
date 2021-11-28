package org.example.packets.handler.message;

import lombok.Data;

@Data
public class MessageReadReqBody {

    private String roomId;

    private String messageId;
}
