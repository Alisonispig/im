package org.example.packets.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.packets.ReplyMessage;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRespBody {

    /**
     * 消息ID
     */
    private long _id;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 未读消息数量
     */
    private Integer unreadCount;

    /**
     * 发送者ID
     */
    private String senderId;

    /**
     * 房间ID
     */
    private String roomId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 年月日
     */
    private String date;

    /**
     * 00:00
     */
    private String timestamp;

    /**
     * 是否系统消息
     */
    private Boolean system;

    /**
     * 是否已保存
     */
    private Boolean saved;

    /**
     * 是否删除的消息
     */
    private Boolean deleted;

    /**
     * 发送消息
     */
    private Boolean disableActions;

    /**
     * 发送消息
     */
    private Boolean disableReactions;

    /**
     * 回复消息
     */
    private ReplyMessage replyMessage;

}
