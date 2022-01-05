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
     * 配置类型
     */
    private UserGroupConfigTypeEnum type;
}
