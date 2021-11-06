package org.example.packets.bean;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

@Data
public class FileInfo {

    /**
     * md5
     */
    @BsonId
    private String md5;

    /**
     * 文件路径
     */
    private String url;
}
