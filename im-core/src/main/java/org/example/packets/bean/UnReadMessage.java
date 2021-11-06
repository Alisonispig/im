package org.example.packets.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnReadMessage {

    private String messageId;

    private String roomId;

    private String userId;

}
