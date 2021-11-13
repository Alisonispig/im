package org.example.packets.handler.message;

import lombok.Data;

@Data
public class MessageFileHistoryReqBody {

    /**
     * 群组id;
     */
    private String roomId;
    /**
     * 消息开始时间;
     */
    private Double beginTime;
    /**
     * 消息结束时间
     */
    private Double endTime;
    /**
     * 当前请求天
     */
    private String date;

}
