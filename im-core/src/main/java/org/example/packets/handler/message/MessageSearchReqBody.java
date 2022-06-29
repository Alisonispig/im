package org.example.packets.handler.message;

import lombok.Data;

@Data
public class MessageSearchReqBody {

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
    private String startDate;
    /**
     * 消息结束时间
     */
    private String endDate;
}
