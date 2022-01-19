package org.example.packets.handler.user;

import lombok.Data;

@Data
public class EditProfileReqBody {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 头像路径
     */
    private String avatar;

    /**
     * 修改后的名称
     */
    private String name;
}
