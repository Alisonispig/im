package org.example.packets;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FriendInfo {

    /**
     * 好友ID
     */
    private String _id;

    /**
     * 备注信息
     */
    private String remark;


}
