package org.example.packets.file;

import lombok.Data;

@Data
public class FileMerge {

    /**
     * 上传ID
     */
    private String uploadId;

    /**
     * 上传地址
     */
    private String objectName;

    /**
     * md5
     */
    private String md5;

    private Long size;

    private String name;

    private String type;


}
