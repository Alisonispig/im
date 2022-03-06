package org.example.packets.handler.video;

import lombok.Data;
import org.example.enums.VideoCommandEnum;

/**
 * 音视频通话请求
 */
@Data
public class VideoReqBody {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 来源ID
     */
    private String fromId;

    /**
     * 房间ID
     */
    private String roomId;

    /**
     * 执行命令
     */
    private VideoCommandEnum command;

    /**
     * VIDEO / AUDIO 音频/视频
     */
    private String type;

}
