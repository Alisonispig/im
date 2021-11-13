package org.example.packets.handler.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinGroupNotifyBody {

    /**
     * 群组ID
     */
    private Group group;

    /**
     * 加入的用户
     */
    private List<User> users;

    /**
     * 加入状态
     */
    private int code;

    /**
     * 相关消息: 区分为创建成功, 加入群聊 退出群聊 移出群聊
     */
    private String message;
}
