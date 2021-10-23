package org.example.packets.handler;

import lombok.Data;

@Data
public class FileMessageBody {

    private String name;

    private long size;

    private String type;

    private String url;

}
