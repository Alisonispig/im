package org.example.packets.handler;

import lombok.Data;
import org.example.packets.ReplyMessage;

import java.util.List;

@Data
public class ChatReqBody {

    /**
     * 消息ID
     */
    private String id;

    /**
     * 发送者编号
     */
    private String senderId;

    /**
     * 房间号
     */
    private String roomId;

    /**
     * 内容
     */
    private String content;

    /**
     * 发送日期
     */
    private String date;

    /**
     * 是否系统消息
     */
    private Boolean system;

    /**
     * 发送时间
     */
    private String timestamp;

    /**
     * 文件信息
     */
    private List<FileMessageBody> files;

    /**
     * 回复消息
     */
    private ReplyMessage replyMessage;

    public static ChatReqBody buildSystem(String roomId, String senderId, String content) {
        ChatReqBody chatReqBody = new ChatReqBody();
        chatReqBody.setRoomId(roomId);
        chatReqBody.setSenderId(senderId);
        chatReqBody.setContent(content);
        chatReqBody.setSystem(true);
        return chatReqBody;
    }

}
