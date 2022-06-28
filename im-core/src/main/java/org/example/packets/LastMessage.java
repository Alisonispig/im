package org.example.packets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 最后一条消息
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LastMessage {

    private String messageId;

    /**
     * 消息主体
     */
    private String content;

    /**
     * 发送者ID
     */
    private String senderId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 发送时间 10:20
     */
    private String timestamp;

    /**
     * 发送年月日
     */
    private String date;

    /**
     * 是否已保存
     */
    private Boolean saved;

    /**
     * 已分发
     */
    private Boolean distributed;

    /**
     * 已读
     */
    private Boolean seen;

    /**
     * 发送时间时间戳
     */
    private Long indexId;

}
