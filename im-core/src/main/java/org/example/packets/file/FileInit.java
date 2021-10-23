package org.example.packets.file;

import lombok.Data;

@Data
public class FileInit {

    private String path;

    private String filename;

    private String contentType;

    private String md5;

    private Integer partCount;

}
