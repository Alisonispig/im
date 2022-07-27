package org.example.packets.handler.emoticon;

import lombok.Data;

@Data
public class EmoticonSearchReqBody {

    private String id;

    /**
     * 查询内容
     */
    private String content;
}
