package org.example.packets;

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
     * 群组名称
     */
    private String roomName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 最后一条消息
     */
    private LastMessage lastMessage;

    /**
     * 组用户
     */
    private List<User> users;

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

        public Builder addUser(User user){
            if(CollUtil.isEmpty(this.users)){
                this.users = new ArrayList<>();
            }
            this.users.add(user);
            return this;
        }

        public Group build() {
            return new Group(this.roomId, this.roomName, this.avatar, this.lastMessage, this.users);
        }
    }
}
