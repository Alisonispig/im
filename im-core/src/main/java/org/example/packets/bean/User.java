package org.example.packets.bean;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.example.enums.RoomRoleEnum;
import org.example.packets.Status;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    /**
     * 主键
     */
    @BsonId
    @JSONField(name = "_id")
    private String id;

    /**
     * 用户名/ 昵称
     */
    private String username;

    /**
     * 账户
     */
    private String account;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户状态
     */
    private Status status;

    /**
     * 是否系统账号
     */
    private Boolean isSystem;

    /**
     * 群组身份
     */
    private RoomRoleEnum role;

    /**
     * 用户群组列表
     */
    private List<Group> groups;

    /**
     * 用户会话列表
     */
    private List<Group> chats;

    public void addGroup(Group group) {
        if (groups == null) {
            groups = new ArrayList<>();
        }
        groups.add(group);
    }

    public User clone() {
        return BeanUtil.copyProperties(this, User.class, "groups", "chats");
    }

}
