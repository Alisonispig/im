package org.example.packets;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String _id;

    /**
     * 用户名/ 昵称
     */
    private String username;

    /**
     * 账户
     */
    private String account;

    private String avatar;

    private Status status;

    private List<Group> groups;

    public void addGroup(Group group) {
        if (groups == null) {
            groups = new ArrayList<>();
        }
        groups.add(group);
    }

    public User clone() {
        return BeanUtil.copyProperties(this, User.class, "groups");
    }

}
