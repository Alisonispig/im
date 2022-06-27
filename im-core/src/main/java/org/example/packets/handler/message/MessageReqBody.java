package org.example.packets.handler.message;

import lombok.Data;
import org.example.enums.MessageFetchTypeEnum;

@Data
public class MessageReqBody {

    /**
     * 群组id;
     */
    private String roomId;
    /**
     * 查询内容
     */
    private String content;
    /**
     * 消息开始时间;
     */
    private Double beginTime;
    /**
     * 消息结束时间
     */
    private Double endTime;

    /**
     * 请求消息类型
     */
    private MessageFetchTypeEnum type;
    /**
     * 起始消息ID
     */
    private String messageId;
}
