package org.example.packets.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.packets.Group;
import org.example.packets.User;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserStatusBody {

    /**
     * 状态变化的用户
     */
    private User user;

    private Group group;

}
