package org.example.store;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.config.Im;
import org.example.enums.KeyEnum;
import org.example.packets.Group;
import org.example.packets.LastMessage;
import org.example.packets.Status;
import org.example.packets.User;
import org.example.packets.handler.ChatReqBody;
import org.tio.core.ChannelContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public void putGroupMessage(ChatReqBody chatReqBody) {
        String key = chatReqBody.getRoomId() + StrUtil.C_COLON + KeyEnum.IM_GROUP_MESSAGE_KEY.getKey();
        RedisStore.push(key, JSON.toJSONString(chatReqBody));
    }

    @Override
    public List<String> getUnReadMessage(String userId, String roomId) {
        String key = userId + StrUtil.C_COLON + roomId + StrUtil.C_COLON + KeyEnum.IM_GROUP_UNREAD_MESSAGE_KEY.getKey();
        return RedisStore.list(key);
    }

    @Override
    public void putUnReadMessage(String userId, String roomId, long id) {
        String key = userId + StrUtil.C_COLON + roomId + StrUtil.C_COLON + KeyEnum.IM_GROUP_UNREAD_MESSAGE_KEY.getKey();
        RedisStore.push(key, String.valueOf(id));
    }

    @Override
    public void clearUnReadMessage(ChannelContext channelContext, String roomId) {
        User user = Im.getUser(channelContext);
        String key = user.get_id() + StrUtil.C_COLON + roomId + StrUtil.C_COLON + KeyEnum.IM_GROUP_UNREAD_MESSAGE_KEY.getKey();
        RedisStore.remove(key);
    }

    @Override
    public List<String> getHistoryMessage(String roomId) {
        String key = roomId + StrUtil.C_COLON + KeyEnum.IM_GROUP_MESSAGE_KEY.getKey();
        return RedisStore.list(key);
    }

    @Override
    public ChatReqBody getGroupMessage(String roomId, String messageId) {
        List<String> messages = getHistoryMessage(roomId);
        final long id = Long.parseLong(messageId);
        List<ChatReqBody> collect = messages.stream().map(x -> JSONObject.parseObject(x, ChatReqBody.class)).filter(x -> x.get_id() == id).collect(Collectors.toList());
        if (CollUtil.isEmpty(collect)) {
            return null;
        }
        return collect.get(0);
    }

    @Override
    public void updateLastMessage(ChatReqBody chatReqBody) {
        Group groupInfo = getGroupInfo(chatReqBody.getRoomId());
        groupInfo.setLastMessage(LastMessage.builder().content(chatReqBody.getContent()).senderId(chatReqBody.getSenderId())
                .username(chatReqBody.getSenderId()).timestamp(chatReqBody.getTimestamp()).saved(true).distributed(true).seen(true).isNew(true).build());
        setGroupInfo(groupInfo);
    }

    @Override
    public List<User> getUserList() {
        List<String> list = RedisStore.list(KeyEnum.IM_USER_LIST_KEY.getKey());
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().map(this::getUserInfo).collect(Collectors.toList());
    }

    @Override
    public void initAccount(String account, String id) {
        userListAdd(id);
        putAccount(account, id);
    }

    @Override
    public void userListAdd(String userId) {
        List<String> list = RedisStore.list(KeyEnum.IM_USER_LIST_KEY.getKey());
        if (list != null && !list.contains(userId)) {
            RedisStore.push(KeyEnum.IM_USER_LIST_KEY.getKey(), userId);
        }
    }

    @Override
    public void putAccount(String account, String userId) {
        RedisStore.hSet(KeyEnum.IM_ACCOUNT_MAP_KEY.getKey(), account, userId);
    }

    @Override
    public User getByAccount(String account){
        String userId = RedisStore.hGet(KeyEnum.IM_ACCOUNT_MAP_KEY.getKey(), account);

        if(StrUtil.isBlank(userId)){
            return null;
        }
        return getUserInfo(userId);
    }

    private List<String> getGroupUserIds(String roomId) {
        return RedisStore.list(roomId + StrUtil.C_COLON + KeyEnum.IM_GROUP_USERS_KEY.getKey());
    }

    public void initGroupUsers(ChannelContext channelContext, Group group) {
        String roomId = group.getRoomId();
        User user = Im.getUser(channelContext);
        // 将当前用户加入到群组中
        addGroupUser(user.get_id(), group.getRoomId());

        // 初始化用户的群组信息
        initUserGroups(user.get_id(), roomId);

        for (Group userGroup : user.getGroups()) {
            if (!userGroup.getRoomId().equals(roomId)) {
                continue;
            }
            setGroupInfo(userGroup);
        }

    }

    public void setGroupInfo(Group userGroup) {
        RedisStore.set(userGroup.getRoomId() + StrUtil.C_COLON + KeyEnum.IM_GROUP_INFO_KEY.getKey(), userGroup);
    }

    @Override
    public void addGroupUser(String userId, String roomId) {
        String key = roomId + StrUtil.C_COLON + KeyEnum.IM_GROUP_USERS_KEY.getKey();

        List<String> list = RedisStore.list(key);
        if (!list.contains(userId)) {
            RedisStore.push(key, userId);
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
