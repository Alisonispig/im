package org.example.packets;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserReqBody extends Message{

    /**
     * 用户id;
     */
    private String userId;
    /**
     * 0:单个,1:所有在线用户,2:所有用户(在线+离线);
     */
    private Integer type;

}
