package org.example.store;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.example.config.ImSessionContext;
import org.example.enums.KeyEnum;
import org.example.packets.Group;
import org.example.packets.User;
import org.tio.core.ChannelContext;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class RedisMessageHelper implements MessageHelper {

    @Override
    public void onAfterGroupBind(ChannelContext channelContext, Group group) {
        initGroupUsers(channelContext, group);
    }

    @Override
    public void onAfterUserBind(ChannelContext channelContext, User user) {
        User build = User.builder()._id(user.get_id()).avatar(user.getAvatar()).status(user.getStatus()).username(user.getUsername()).build();
        RedisStore.set(user.get_id() + StrUtil.C_COLON + KeyEnum.IM_USER_INFO_KEY.getKey(), build);
    }

    @Override
    public List<User> getGroupUsers(String roomId) {
        List<String> groupUserIds = getGroupUserIds(roomId);
        if (groupUserIds == null) {
            return null;
        }
        List<User> users = new ArrayList<>();
        for (String userId : groupUserIds) {
            User user = getUserInfo(userId);
            users.add(user);
        }
        return users;
    }

    @Override
    public User getUserInfo(String userId) {
        return RedisStore.get(userId + StrUtil.C_COLON + KeyEnum.IM_USER_INFO_KEY.getKey(), User.class);
    }

    private List<String> getGroupUserIds(String roomId) {
        Group group = RedisStore.get(roomId + StrUtil.C_COLON + KeyEnum.IM_GROUP_INFO_KEY.getKey(), Group.class);
        if (group == null) {
            return null;
        }
        return RedisStore.list(roomId + StrUtil.C_COLON + KeyEnum.IM_GROUP_USERS_KEY.getKey());
    }

    private void initGroupUsers(ChannelContext channelContext, Group group) {
        String roomId = group.getRoomId();
        ImSessionContext imSessionContext = (ImSessionContext) channelContext.get(KeyEnum.IM_CHANNEL_SESSION_CONTEXT_KEY.getKey());
        User user = imSessionContext.getImClientNode().getUser();

        String key = roomId + StrUtil.C_COLON + KeyEnum.IM_GROUP_USERS_KEY.getKey();

        List<String> list = RedisStore.list(key);
        if (!list.contains(user.get_id())) {
            RedisStore.push(key, user.get_id());
        }

        initUserGroups(user.get_id(), roomId);

        for (Group userGroup : user.getGroups()) {
            if (!userGroup.getRoomId().equals(roomId)) {
                continue;
            }
            RedisStore.set(roomId + StrUtil.C_COLON + KeyEnum.IM_GROUP_INFO_KEY.getKey(), userGroup);
        }

    }

    private void initUserGroups(String userId, String roomId) {
        String key = userId + StrUtil.C_COLON + KeyEnum.IM_USER_GROUPS_KEY.getKey();
        List<String> list = RedisStore.list(key);
        if (list.contains(roomId)) {
            return;
        }
        RedisStore.push(key, roomId);
    }
}
