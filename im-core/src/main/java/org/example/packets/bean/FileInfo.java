package org.example.packets.bean;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

@Data
public class FileInfo {

    /**
     * md5
     */
    @BsonId
    private String id;

    private String md5;

    /**
     * 文件路径
     */
    private String url;

    private Long size;

    private String name;

    private String type;

    public static final String COL_MD5 = "md5";
    public static final String COL_SIZE = "size";
    public static final String COL_NAME = "name";
    public static final String COL_TYPE = "type";
}
