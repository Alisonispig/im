package org.example.packets.handler.message;

import lombok.Data;

import java.util.List;

@Data
public class MessageFileHistoryRespBody {

    /**
     * 当前天
     */
    private String date;

    /**
     * 返回的文件信息
     */
    private List<FileMessageBody> files;

    /**
     * 是否有下一天
     */
    private Boolean hasNext;
}
