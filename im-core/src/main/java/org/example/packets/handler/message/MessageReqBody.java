package org.example.packets.handler.message;

import lombok.Data;

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
     * 分页偏移量
     */
    private Integer page;
    /**
     * 数量
     */
    private Integer number;
}
