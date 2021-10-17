package org.example.packets;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group implements Serializable {

    /**
     * 群组ID
     */
    private String roomId;

    /**
     * 排序
     */
    private long index;

    /**
     * 群组名称
     */
    private String roomName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 好友ID
     */
    private String friendId;

    /**
     * 最后一条消息
     */
    private LastMessage lastMessage;

    /**
     * 组用户
     */
    private List<User> users;

    public Group clone(){
        return BeanUtil.copyProperties(this,Group.class,"users");
    }

    public static Group.Builder builder() {
        return new Builder();
    }

    public static class Builder {

        /**
         * 群组ID
         */
        private String roomId;

        /**
         * 群组名称
         */
        private String roomName;

        /**
         * 头像
         */
        private String avatar;

        private long index;

        /**
         * 好友ID
         */
        private String friendId;

        /**
         * 最后一条消息
         */
        private LastMessage lastMessage;

        /**
         * 组用户
         */
        private List<User> users;

        public Builder() {
        }

        public Builder roomId(String roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder index(long index) {
            this.index = index;
            return this;
        }

        public Builder roomName(String roomName) {
            this.roomName = roomName;
            return this;
        }

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder lastMessage(LastMessage lastMessage) {
            this.lastMessage = lastMessage;
            return this;
        }

        public Builder users(List<User> users) {
            this.users = users;
            return this;
        }

        public Builder addUser(User user) {
            if (CollUtil.isEmpty(this.users)) {
                this.users = new ArrayList<>();
            }
            this.users.add(user);
            return this;
        }

        public Builder addUsers(List<User> users) {
            if (CollUtil.isEmpty(this.users)) {
                this.users = new ArrayList<>();
            }
            this.users.addAll(users);
            return this;
        }

        public Group build() {
            return new Group(this.roomId,this.index, this.roomName, this.avatar, this.friendId, this.lastMessage, this.users);
        }
    }
}
