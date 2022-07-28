package org.example.packets.handler.emoticon;

import lombok.Data;
import org.example.enums.EmoticonOperationTypeEnum;

@Data
public class EmoticonOperationReqBody {

    /**
     * 表情ID
     */
    private String emoticonId;

    /**
     * 表情路径
     */
    private String url;

    private Integer size;

    /**
     * 表情名称
     */
    private String name;

    /**
     * 操作类型
     */
    private EmoticonOperationTypeEnum type;
}
