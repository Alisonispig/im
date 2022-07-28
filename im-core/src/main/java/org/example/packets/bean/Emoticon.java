package org.example.packets.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

@Data
public class Emoticon {

    @BsonId
    @JSONField(name = "_id")
    private String id;

    /**
     * 文件URL
     */
    private String url;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 是否私有
     */
    private Boolean isPrivate;

    private Long index;

}
