package org.example.packets.handler.room;

import lombok.Data;
import org.example.enums.UserGroupConfigTypeEnum;

/**
 * 群组用户系统配置
 *
 * @author smart
 * @since 1.0.0
 */
@Data
public class UserGroupConfigReqBody {

    /**
     * 群组ID
     */
    private String roomId;

    /**
     * 打开或关闭该群组通知
     */
    private Boolean notice;

    /**
     * 置顶或取消置顶
     */
    private Boolean moveTop;

    /**
     * 置顶或取消置顶返回的排序号
     */
    private Long index;

    /**
     * 配置类型
     */
    private UserGroupConfigTypeEnum type;
}
