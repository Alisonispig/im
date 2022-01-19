package org.example.packets.handler.message;

import lombok.Data;

@Data
public class FileMessageBody {

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件Url
     */
    private String url;

}
