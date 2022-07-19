package org.example.packets.handler.user;

import lombok.Data;

@Data
public class SearchUserReqBody {

    /**
     * 用户名称
     */
    private String name;

    /**
     * 搜索ID
     */
    private String searchId;

    /**
     * 最后一个用户的ID
     */
    private String userId;
}
