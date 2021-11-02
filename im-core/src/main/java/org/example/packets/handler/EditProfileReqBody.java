package org.example.packets.handler;

import lombok.Data;

@Data
public class EditProfileReqBody {

    /**
     * 房间号 / 用户ID
     */
    private String roomId;

    /**
     * 是否群组
     */
    private Boolean isGroup;

    /**
     * 头像路径
     */
    private String avatar;

    /**
     * 修改后的名称
     */
    private String name;
}
