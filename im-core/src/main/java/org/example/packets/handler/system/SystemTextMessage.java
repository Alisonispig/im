package org.example.packets.handler.system;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.SystemMessageTypeEnum;
import org.example.packets.bean.Message;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class SystemTextMessage extends SystemMessageReqBody {

    private String content;

    public static SystemTextMessage create(String content, List<String> receivers) {

        SystemTextMessage systemTextMessage = new SystemTextMessage();
        systemTextMessage.setContent(content);

        systemTextMessage.setType(SystemMessageTypeEnum.TEXT);
        systemTextMessage.setReceivers(receivers);
        systemTextMessage.setSenderId("SYSTEM");
        return systemTextMessage;
    }

    public Message build(String senderId) {
        Message message = new Message();
        message.setId(IdUtil.getSnowflake().nextIdStr());
        Date date = new Date();
        message.setDate(DateUtil.formatDate(date));
        message.setTimestamp(DateUtil.formatTime(date));
        message.setSenderId(senderId);
        message.setSystem(false);
        message.setDeleted(false);
        message.setSaved(true);
        message.setDistributed(true);
        message.setSeen(false);
        message.setSendTime(System.currentTimeMillis());
        message.setContent(this.content);
        return message;
    }


}
