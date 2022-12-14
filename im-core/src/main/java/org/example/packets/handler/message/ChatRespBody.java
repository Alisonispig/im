package org.example.packets.handler.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.packets.ReplyMessage;

import java.util.List;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRespBody {

    /**
     * 消息ID
     */
    @JSONField(name = "_id")
    private String id;


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
     * 当前用户ID
     */
    private String currentUserId;

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
     * 已分发
     */
    private Boolean distributed;

    /**
     * 已读
     */
    private Boolean seen;

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

    private List<FileMessageBody> files;

    /**
     * 表情回复消息
     */
    private Map<String, List<String>> reactions;

    private Long sendTime;

}
