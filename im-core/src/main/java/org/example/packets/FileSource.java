package org.example.packets;

import lombok.Data;

@Data
public class FileSource {

    private String name;

    private Integer size;

    private String type;

    private Boolean audio;

    private Double duration;

    private String url;

    private String preview;
}
