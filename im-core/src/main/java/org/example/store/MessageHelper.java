package org.example.store;

import org.example.packets.Group;
import org.example.packets.User;
import org.tio.core.ChannelContext;

import java.util.List;

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
     * @param channelContext 上下文信息
     */
    void userOffline(ChannelContext channelContext);

    /**
     * 初始化用户群组持久化信息
     * @param userId 用户ID
     * @param roomId 组ID
     */
    void initUserGroups(String userId, String roomId);
}
