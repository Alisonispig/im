package org.example.packets.handler.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.SystemMessageTypeEnum;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SystemMessageReqBody {

    private SystemMessageTypeEnum type;

    private String senderId;

    /**
     * 接收人列表
     */
    private List<String> receivers;

}
