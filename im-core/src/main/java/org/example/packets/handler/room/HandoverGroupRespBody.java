package org.example.packets.handler.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.packets.bean.User;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HandoverGroupRespBody {

    private String roomId;

    private String oldAdmin;

    private String newAdmin;

}
