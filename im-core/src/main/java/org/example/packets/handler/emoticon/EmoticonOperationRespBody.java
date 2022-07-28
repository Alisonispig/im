package org.example.packets.handler.emoticon;

import lombok.Data;
import org.example.enums.EmoticonOperationTypeEnum;
import org.example.packets.bean.Emoticon;

@Data
public class EmoticonOperationRespBody {

    private EmoticonOperationTypeEnum type;

    private Emoticon emoticon;
}
