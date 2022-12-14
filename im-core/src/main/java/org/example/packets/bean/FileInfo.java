package org.example.packets.bean;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

@Data
public class FileInfo {

    /**
     * 主键
     */
    @BsonId
    private String id;

    /**
     * 文件MD5
     */
    private String md5;

    /**
     * 文件路径
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

    public static final String COL_MD5 = "md5";
    public static final String COL_SIZE = "size";
    public static final String COL_NAME = "name";
    public static final String COL_TYPE = "type";
}
