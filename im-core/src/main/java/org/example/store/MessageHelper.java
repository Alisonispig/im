package org.example.store;

import org.example.packets.FriendInfo;
import org.example.packets.Group;
import org.example.packets.User;
import org.example.packets.handler.ChatReqBody;
import org.tio.core.ChannelContext;

import java.util.List;
import java.util.Map;

public interface MessageHelper {

    /**
     * 绑定群组持久化到缓存中
     *
     * @param channelContext 用户上下文信息
     * @param group          群组信息
     * @return 是否成功
     */
    void onAfterGroupBind(ChannelContext channelContext, Group group);

    /**
     * 绑定用户持久化到缓存中
     *
     * @param channelContext 用户上下文信息
     * @param user           用户
     */
    void onAfterUserBind(ChannelContext channelContext, User user);

    /**
     * 获取群组用户
     *
     * @param roomId 群组ID
     */
    List<User> getGroupUsers(String roomId);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserInfo(String userId);

    /**
     * 获取用户下的群组信息
     *
     * @param userId 用户ID
     * @return 群组信息
     */
    List<Group> getUserGroups(String userId);

    /**
     * 用户下线
     *
     * @param channelContext 上下文信息
     */
    void userOffline(ChannelContext channelContext);

    List<String> getUserChats(String userId);

    /**
     * 添加用户到用户列表
     *
     * @param userId 用户ID
     */
    void userListAdd(String userId);

    /**
     * 绑定账号和用户ID
     *
     * @param account 账号信息
     * @param userId  用户ID
     */
    void putAccount(String account, String userId);

    /**
     * 通过账号获取用户信息
     *
     * @param account 账号
     * @return 用户信息
     */
    User getByAccount(String account);

    /**
     * @param md5 Md5
     * @param url 文件Url
     */
    void setFileUrl(String md5, String url);

    Map<String, List<String>> getReaction(String roomId, String messageId);

    /**
     * 将用户加入到群组中
     *
     * @param userId 用户Id
     * @param roomId 群组Id
     */
    void addGroupUser(String userId, String roomId);

    /**
     * 初始化用户群组持久化信息
     *
     * @param userId 用户ID
     * @param roomId 组ID
     */
    void initUserGroups(String userId, String roomId);

    /**
     * 获取群组信息
     *
     * @param roomId 群组ID
     * @return 群组信息
     */
    Group getGroupInfo(String roomId);

    /**
     * 缓存消息
     *
     * @param chatReqBody 消息体
     */
    void putGroupMessage(ChatReqBody chatReqBody);

    /**
     * 取出未读消息
     *
     * @param userId 用户编号
     * @param roomId 群组ID
     * @return 未读消息列表
     */
    List<String> getUnReadMessage(String userId, String roomId);

    /**
     * 设置未读消息
     *
     * @param userId 用户ID
     * @param roomId 群组ID
     * @param id     消息id
     */
    void putUnReadMessage(String userId, String roomId, String id);

    /**
     * 清理未读消息
     *
     * @param channelContext 上下文
     * @param roomId         房间ID
     */
    void clearUnReadMessage(ChannelContext channelContext, String roomId);

    /**
     * 获取群组历史消息
     *
     * @param roomId 群组ID
     * @return 历史消息集合
     */
    List<String> getHistoryMessage(String roomId);

    /**
     * 获取群组内消息
     *
     * @param roomId    房间ID
     * @param messageId 群组ID
     * @return 消息
     */
    ChatReqBody getGroupMessage(String roomId, String messageId);

    /**
     * 更新最后一条消息
     *
     * @param chatReqBody 最后一条消息
     */
    void updateLastMessage(ChatReqBody chatReqBody);

    /**
     * 获取当前所有用户
     *
     * @return 用户信息
     */
    List<User> getUserList();

    /**
     * 初始化账号信息
     *
     * @param account 账户信息
     * @param id      用户ID
     */
    void initAccount(String account, String id);

    /**
     * 获取用户好友列表
     *
     * @param userId 主键
     * @return 好友列表
     */
    Map<String, String> getUserFriends(String userId);

    /**
     * 添加会话
     *
     * @param roomId 房间号
     * @param users  用户列表
     */
    void addChat(String roomId, List<User> users);

    /**
     * 添加会话
     *
     * @param userId 用户ID
     * @param roomId 群组ID
     */
    void addChat(String userId, String roomId);

    /**
     * 添加一个群组信息
     *
     * @param group 群组
     */
    void setGroupInfo(Group group);

    /**
     * 为用户添加一个好友信息
     *
     * @param userId     用户ID
     * @param roomId     群组ID
     * @param friendInfo 好友信息
     */
    void putFriendInfo(String userId, String roomId, FriendInfo friendInfo);

    /**
     * 添加一个表情回复
     *
     * @param roomId    房间号
     * @param messageId 消息ID
     * @param reaction  表情
     * @param remove    是否删除
     */
    void addReaction(String roomId, String messageId, String reaction, Boolean remove, String userId);

    /**
     * 获取缓存的文件
     *
     * @param md5
     * @return
     */
    String getFileUrl(String md5);
}
