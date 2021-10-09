package org.example.store;

import cn.hutool.core.util.StrUtil;
import org.example.config.Im;
import org.example.enums.KeyEnum;
import org.example.packets.Group;
import org.example.packets.Status;
import org.example.packets.User;
import org.tio.core.ChannelContext;

import java.util.ArrayList;
import java.util.List;

public class RedisMessageHelper implements MessageHelper {

    @Override
    public void onAfterGroupBind(ChannelContext channelContext, Group group) {
        initGroupUsers(channelContext, group);
    }

    @Override
    public void onAfterUserBind(ChannelContext channelContext, User user) {
        User build = user.clone();
        updateUserInfo(build);
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

    @Override
    public List<Group> getUserGroups(String userId) {
        String key = userId + StrUtil.C_COLON + KeyEnum.IM_USER_GROUPS_KEY.getKey();
        List<String> groupIds = RedisStore.list(key);
        List<Group> groups = new ArrayList<>();
        groupIds.forEach(x -> groups.add(getGroupInfo(x)));
        return groups;
    }

    @Override
    public void userOffline(ChannelContext channelContext) {
        User user = Im.getUser(channelContext);
        User userInfo = getUserInfo(user.get_id());
        userInfo.setStatus(Status.offline());
        updateUserInfo(userInfo);
    }

    private void updateUserInfo(User user) {
        RedisStore.set(user.get_id() + StrUtil.C_COLON + KeyEnum.IM_USER_INFO_KEY.getKey(), user);
    }

    @Override
    public Group getGroupInfo(String roomId) {
        return RedisStore.get(roomId + StrUtil.C_COLON + KeyEnum.IM_GROUP_INFO_KEY.getKey(), Group.class);
    }

    private List<String> getGroupUserIds(String roomId) {

        return RedisStore.list(roomId + StrUtil.C_COLON + KeyEnum.IM_GROUP_USERS_KEY.getKey());
    }

    public void initGroupUsers(ChannelContext channelContext, Group group) {
        String roomId = group.getRoomId();
        User user = Im.getUser(channelContext);

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

    @Override
    public void initUserGroups(String userId, String roomId) {
        String key = userId + StrUtil.C_COLON + KeyEnum.IM_USER_GROUPS_KEY.getKey();
        List<String> list = RedisStore.list(key);
        if (list.contains(roomId)) {
            return;
        }
        RedisStore.push(key, roomId);
    }
}
