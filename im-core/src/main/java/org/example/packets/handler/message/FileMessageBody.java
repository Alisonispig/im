package org.example.packets.handler.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class FileMessageBody {

    @JSONField(name = "_id")
    private String id;

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

    /**
     * 是不是表情包
     */
    private Boolean isEmoticon;

}
