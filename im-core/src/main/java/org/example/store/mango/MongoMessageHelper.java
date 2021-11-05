package org.example.store.mango;

import org.example.packets.FriendInfo;
import org.example.packets.Group;
import org.example.packets.User;
import org.example.packets.handler.ChatReqBody;
import org.example.store.MessageHelper;
import org.example.store.mango.dao.UserRepository;
import org.tio.core.ChannelContext;

import java.util.List;
import java.util.Map;

public class MongoMessageHelper implements MessageHelper {

    private final UserRepository userRepository;

    public MongoMessageHelper(){
        userRepository = new UserRepository();
    }

    @Override
    public void onAfterGroupBind(ChannelContext channelContext, Group group) {

    }

    @Override
    public void onAfterUserBind(ChannelContext channelContext, User user) {

    }

    @Override
    public List<User> getGroupUsers(String roomId) {
        return null;
    }

    @Override
    public User getUserInfo(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<Group> getUserGroups(String userId) {
        return null;
    }

    @Override
    public void userOffline(ChannelContext channelContext) {

    }

    @Override
    public List<String> getUserChats(String userId) {
        return null;
    }

    @Override
    public void userListAdd(String userId) {

    }

    @Override
    public void putAccount(String account, String userId) {

    }

    @Override
    public User getByAccount(String account) {
        return null;
    }

    @Override
    public void setFileUrl(String md5, String url) {

    }

    @Override
    public Map<String, List<String>> getReaction(String roomId, String messageId) {
        return null;
    }

    @Override
    public void addGroupUser(String userId, String roomId) {

    }

    @Override
    public void initUserGroups(String userId, String roomId) {

    }

    @Override
    public Group getGroupInfo(String roomId) {
        return null;
    }

    @Override
    public void putGroupMessage(ChatReqBody chatReqBody) {

    }

    @Override
    public void updateUserInfo(User user) {

    }

    @Override
    public List<String> getUnReadMessage(String userId, String roomId) {
        return null;
    }

    @Override
    public void putUnReadMessage(String userId, String roomId, String id) {

    }

    @Override
    public void clearUnReadMessage(ChannelContext channelContext, String roomId) {

    }

    @Override
    public List<String> getHistoryMessage(String roomId) {
        return null;
    }

    @Override
    public ChatReqBody getGroupMessage(String roomId, String messageId) {
        return null;
    }

    @Override
    public void updateLastMessage(ChatReqBody chatReqBody) {

    }

    @Override
    public List<User> getUserList() {
        return null;
    }

    @Override
    public void initAccount(String account, String id) {

    }

    @Override
    public Map<String, String> getUserFriends(String userId) {
        return null;
    }

    @Override
    public void addChat(String roomId, List<User> users) {

    }

    @Override
    public void addChat(String userId, String roomId) {

    }

    @Override
    public void setGroupInfo(Group group) {

    }

    @Override
    public void putFriendInfo(String userId, String roomId, FriendInfo friendInfo) {

    }

    @Override
    public void addReaction(String roomId, String messageId, String reaction, Boolean remove, String userId) {

    }

    @Override
    public String getFileUrl(String md5) {
        return null;
    }
}
