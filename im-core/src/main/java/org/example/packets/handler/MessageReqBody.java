package org.example.packets.handler;

import lombok.Data;

@Data
public class MessageReqBody {

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
     * 分页偏移量
     */
    private Integer offset;
    /**
     * 数量
     */
    private Integer count;
}
