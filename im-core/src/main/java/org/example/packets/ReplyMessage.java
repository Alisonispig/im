package org.example.packets;

import lombok.Data;

@Data
public class ReplyMessage {

    /**
     * 引用内容
     */
    private String content;

    /**
     * 发送者
     */
    private String senderId;

    /**
     * 文件信息
     */
    private FileSource file;

}
