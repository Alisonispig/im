package org.example.store;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.example.config.Im;
import org.example.enums.KeyEnum;
import org.example.packets.*;
import org.example.packets.handler.ChatReqBody;
import org.example.store.redis.RedisStore;
import org.tio.core.ChannelContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // 群组信息 ------------------------------------------------------------------

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
    public List<Group> getUserGroups(String userId) {
        String key = KeyEnum.IM_USER_GROUPS_KEY.getKey() + StrUtil.C_COLON + userId;
        List<String> groupIds = RedisStore.list(key);
        List<Group> groups = new ArrayList<>();
        groupIds.forEach(x -> groups.add(getGroupInfo(x)));
        return groups;
    }


    @Override
    public Group getGroupInfo(String roomId) {
        return RedisStore.get(KeyEnum.IM_GROUP_INFO_KEY.getKey() + StrUtil.C_COLON + roomId, Group.class);
    }

    @Override
    public void putGroupMessage(ChatReqBody chatReqBody) {
        String key = KeyEnum.IM_GROUP_MESSAGE_KEY.getKey() + StrUtil.C_COLON + chatReqBody.getRoomId();
        RedisStore.push(key, JSON.toJSONString(chatReqBody));
    }

    // 用户信息 -----------------------------------------------------------------------

    @Override
    public User getUserInfo(String userId) {
        return RedisStore.get(KeyEnum.IM_USER_INFO_KEY.getKey() + StrUtil.C_COLON + userId, User.class);
    }

    @Override
    public void userOffline(ChannelContext channelContext) {
        User user = Im.getUser(channelContext);
        User userInfo = getUserInfo(user.get_id());
        userInfo.setStatus(Status.offline());
        updateUserInfo(userInfo);
    }

    private void updateUserInfo(User user) {
        RedisStore.set(KeyEnum.IM_USER_INFO_KEY.getKey() + StrUtil.C_COLON + user.get_id(), user);
    }


    @Override
    public List<String> getUnReadMessage(String userId, String roomId) {
        String key = KeyEnum.IM_GROUP_UNREAD_MESSAGE_KEY.getKey() + StrUtil.C_COLON + userId + StrUtil.C_COLON + roomId;
        return RedisStore.list(key);
    }

    @Override
    public void putUnReadMessage(String userId, String roomId, String id) {
        String key = KeyEnum.IM_GROUP_UNREAD_MESSAGE_KEY.getKey() + StrUtil.C_COLON + userId + StrUtil.C_COLON + roomId;
        RedisStore.push(key, String.valueOf(id));
    }

    @Override
    public void clearUnReadMessage(ChannelContext channelContext, String roomId) {
        User user = Im.getUser(channelContext);
        String key = KeyEnum.IM_GROUP_UNREAD_MESSAGE_KEY.getKey() + StrUtil.C_COLON + user.get_id() + StrUtil.C_COLON + roomId;
        RedisStore.remove(key);
    }

    @Override
    public List<String> getHistoryMessage(String roomId) {
        String key = KeyEnum.IM_GROUP_MESSAGE_KEY.getKey() + StrUtil.C_COLON + roomId;
        return RedisStore.list(key);
    }

    @Override
    public ChatReqBody getGroupMessage(String roomId, String messageId) {
        List<String> messages = getHistoryMessage(roomId);
        List<ChatReqBody> collect = messages.stream().map(x -> JSONObject.parseObject(x, ChatReqBody.class)).filter(x -> x.get_id().equals(messageId)).collect(Collectors.toList());
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
    public Map<String, String> getUserFriends(String userId) {
        return RedisStore.hGet(KeyEnum.IM_USER_FRIENDS_KEY.getKey() + StrUtil.COLON + userId);
    }


    @Override
    public List<String> getUserChats(String userId) {
        return RedisStore.list(KeyEnum.IM_USER_CHATS_KEY.getKey() + StrUtil.COLON + userId);
    }

    @Override
    public void addChat(String roomId, List<User> users) {
        for (User user : users) {
            addChat(user.get_id(), roomId);
        }
    }

    @Override
    public void addChat(String userId, String roomId) {
        String key = KeyEnum.IM_USER_CHATS_KEY.getKey() + StrUtil.COLON + userId;
        List<String> userChats = getUserChats(userId);
        if (!userChats.contains(userId)) {
            RedisStore.push(key, roomId);
        }
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
    public User getByAccount(String account) {
        String userId = RedisStore.hGet(KeyEnum.IM_ACCOUNT_MAP_KEY.getKey(), account);

        if (StrUtil.isBlank(userId)) {
            return null;
        }
        return getUserInfo(userId);
    }

    private List<String> getGroupUserIds(String roomId) {
        return RedisStore.list(KeyEnum.IM_GROUP_USERS_KEY.getKey() + StrUtil.C_COLON + roomId);
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

    @Override
    public void setGroupInfo(Group userGroup) {
        RedisStore.set(KeyEnum.IM_GROUP_INFO_KEY.getKey() + StrUtil.C_COLON + userGroup.getRoomId(), userGroup.clone());
    }

    @Override
    public void putFriendInfo(String userId, String roomId, FriendInfo friendInfo) {
        String key = KeyEnum.IM_USER_FRIENDS_KEY.getKey() + StrUtil.COLON + userId;
        RedisStore.hSet(key, roomId, JSON.toJSONString(friendInfo));
    }

    @Override
    public void addReaction(String roomId, String messageId, String reaction, Boolean remove, String userId) {
        String key = KeyEnum.IM_MESSAGE_REACTION_MAP_KEY.getKey() + StrUtil.COLON + roomId;

        Map<String, List<String>> reactionMap = getReaction(roomId, messageId);
        List<String> userIds = reactionMap.get(reaction);
        if (Boolean.TRUE.equals(remove)) {
            userIds.remove(userId);
        } else {
            if (CollUtil.isEmpty(userIds)) {
                reactionMap.put(reaction, CollUtil.newArrayList(userId));
            } else {
                userIds.add(userId);
            }
        }

        RedisStore.hSet(key, String.valueOf(messageId), JSON.toJSONString(reactionMap));
    }

    @Override
    public String getFileUrl(String md5) {
        String key = KeyEnum.IM_FILE_UPLOAD_KEY.getKey() ;
        return RedisStore.hGet(key,md5);
    }

    @Override
    public void setFileUrl(String md5,String url) {
        String key = KeyEnum.IM_FILE_UPLOAD_KEY.getKey() ;
        RedisStore.hSet(key,md5,url);
    }

    @Override
    public Map<String, List<String>> getReaction(String roomId, String messageId) {
        String key = KeyEnum.IM_MESSAGE_REACTION_MAP_KEY.getKey() + StrUtil.COLON + roomId;
        String release = RedisStore.hGet(key, String.valueOf(messageId));
        if (StrUtil.isBlank(release)) {
            return new HashMap<>();
        }
        return JSON.parseObject(release, new TypeReference<HashMap<String, List<String>>>() {
        });
    }

    @Override
    public void addGroupUser(String userId, String roomId) {
        String key = KeyEnum.IM_GROUP_USERS_KEY.getKey() + StrUtil.C_COLON + roomId;

        List<String> list = RedisStore.list(key);
        if (!list.contains(userId)) {
            RedisStore.push(key, userId);
        }
    }

    @Override
    public void initUserGroups(String userId, String roomId) {
        String key = KeyEnum.IM_USER_GROUPS_KEY.getKey() + StrUtil.C_COLON + userId;
        List<String> list = RedisStore.list(key);
        if (list.contains(roomId)) {
            return;
        }
        RedisStore.push(key, roomId);
    }
}
