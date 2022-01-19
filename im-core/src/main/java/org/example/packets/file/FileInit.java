package org.example.packets.file;

import lombok.Data;

@Data
public class FileInit {

    /**
     * 路径
     */
    private String path;

    /**
     * 文件名
     */
    private String filename;

    private String contentType;

    private String md5;

    private Integer partCount;

}
