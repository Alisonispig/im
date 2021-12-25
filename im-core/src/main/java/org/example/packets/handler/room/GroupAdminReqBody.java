package org.example.packets.handler.room;

import lombok.Data;
import org.example.enums.GroupAdminTypeEnum;
import org.example.enums.GroupOutTypeEnum;

@Data
public class GroupAdminReqBody {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 群组ID
     */
    private String roomId;

    /**
     * 移除类型: 设置\解除
     */
    private GroupAdminTypeEnum type;
}
