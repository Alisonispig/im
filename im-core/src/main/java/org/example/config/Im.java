package org.example.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.KeyEnum;
import org.example.packets.bean.Group;
import org.example.packets.bean.User;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;
import org.tio.utils.lock.SetWithLock;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class Im extends ImConfig {

    /**
     * 阻塞发送消息
     *
     * @param channelContext 上下文信息
     * @param packet         包信息
     */
    public static void bSend(ChannelContext channelContext, Packet packet) {
        if (channelContext == null || packet == null) {
            return;
        }
        Tio.bSend(channelContext, packet);
    }

    /**
     * 发送消息
     *
     * @param channelContext 上下文信息
     * @param packet         包信息
     */
    public static void send(ChannelContext channelContext, Packet packet) {
        if (channelContext == null || packet == null) {
            return;
        }
        Tio.send(channelContext, packet);
    }

    public static void sendToGroup(String groupId, Packet packet) {
        Tio.sendToGroup(Im.get().tioConfig, groupId, packet);
    }

    public static void bSendToGroup(String groupId, Packet packet) {
        Tio.bSendToGroup(Im.get().tioConfig, groupId, packet);
    }

    /**
     * 绑定到群组
     *
     * @param channelContext 上下文信息
     * @param group          群组信息
     */
    public static void bindGroup(ChannelContext channelContext, Group group) {
        String groupId = group.getRoomId();
        Tio.bindGroup(channelContext, groupId);
    }

    /**
     * 移除群组绑定
     *
     * @param channelContext 上下文
     * @param roomId         群组Id
     */
    public static void unBindGroup(ChannelContext channelContext, String roomId) {
        Tio.unbindGroup(roomId, channelContext);
    }

    /**
     * 移除群组
     *
     * @param groupId 解散群组
     */
    public static void removeGroup(String groupId) {
        List<ChannelContext> groupChannels = getByGroup(groupId);
        for (ChannelContext channel : groupChannels) {
            unBindGroup(channel,groupId);
        }
    }

    /**
     * 绑定用户(如果配置了回调函数执行回调)
     *
     * @param channelContext IM通道上下文
     * @param user           绑定用户信息
     */
    public static void bindUser(ChannelContext channelContext, User user) {
        if (Objects.isNull(user) || StrUtil.isBlank(user.getId())) {
            log.error("user or userId is null");
            return;
        }
        String userId = user.getId();
        ImSessionContext imSessionContext = (ImSessionContext) channelContext.get(KeyEnum.IM_CHANNEL_SESSION_CONTEXT_KEY.getKey());
        Tio.bindUser(channelContext, userId);
        SetWithLock<ChannelContext> channelContextSetWithLock = Tio.getByUserid(Im.get().getTioConfig(), userId);
        ReentrantReadWriteLock.ReadLock lock = channelContextSetWithLock.getLock().readLock();
        try {
            lock.lock();
            if (CollUtil.isEmpty(channelContextSetWithLock.getObj())) {
                return;
            }
            imSessionContext.getImClientNode().setUser(user);
            get().imUserListener.onAfterBind(channelContext, user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    public static List<ChannelContext> getByGroup(String roomId) {
        SetWithLock<ChannelContext> users = Tio.getByGroup(Im.get().tioConfig, roomId);
        if (users == null) {
            return new ArrayList<>();
        }
        return convertChannel(users);
    }


    public static List<ChannelContext> convertChannel(SetWithLock<ChannelContext> channelContextSetWithLock) {
        ReentrantReadWriteLock.ReadLock lock = channelContextSetWithLock.getLock().readLock();
        try {
            lock.lock();
            Set<ChannelContext> channelContexts = channelContextSetWithLock.getObj();
            return new ArrayList<>(channelContexts);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            lock.unlock();
        }
        return new ArrayList<>();
    }

    /**
     * 移除用户, 和close方法一样，只不过不再进行重连等维护性的操作
     *
     * @param userId 用户ID
     * @param remark 移除原因描述
     */
    public static void remove(String userId, String remark) {
        SetWithLock<ChannelContext> userChannelContexts = Tio.getByUserid(ImConfig.get().getTioConfig(), userId);
        Set<ChannelContext> channels = userChannelContexts.getObj();
        if (channels.isEmpty()) {
            return;
        }
        ReentrantReadWriteLock.ReadLock readLock = userChannelContexts.getLock().readLock();
        try {
            readLock.lock();
            for (ChannelContext channelContext : channels) {
                remove(channelContext, remark);
            }
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 移除连接
     *
     * @param channelContext 上下文信息
     * @param remark         备注
     */
    public static void remove(ChannelContext channelContext, String remark) {
        Tio.remove(channelContext, remark);
    }

    /**
     * 获取当前用户上下文完整信息
     *
     * @param channelContext 上下文信息
     * @return 用户信息（完整）
     */
    public static User getUser(ChannelContext channelContext) {
        return getUser(channelContext, true);
    }

    /**
     * 获取当前用户上下文简易
     *
     * @param channelContext 上下文信息
     * @param isAllInfo      是否包含全部信息
     * @return 用户信息（基础信息）
     */
    public static User getUser(ChannelContext channelContext, boolean isAllInfo) {
        ImSessionContext imSessionContext = (ImSessionContext) channelContext.get(KeyEnum.IM_CHANNEL_SESSION_CONTEXT_KEY.getKey());
        User user = imSessionContext.getImClientNode().getUser();
        if (user == null) {
            Im.close(channelContext, "异常关闭");
        }
        if (isAllInfo) {
            return user;
        }
        return user.clone();
    }

    public static void addGroup(ChannelContext channelContext, Group group) {
        ImSessionContext imSessionContext = (ImSessionContext) channelContext.get(KeyEnum.IM_CHANNEL_SESSION_CONTEXT_KEY.getKey());
        imSessionContext.getImClientNode().getUser().addGroup(group);
    }

    public static void close(ChannelContext channelContext, String remark) {
        Tio.close(channelContext, remark);
    }

    /**
     * 判断用户是否在线
     *
     * @param id 用户ID
     * @return 是否在线
     */
    public static boolean isOnline(String id) {
        List<ChannelContext> channelByUserId = getChannelByUserId(id);
        return CollUtil.isNotEmpty(channelByUserId);
    }

    /**
     * 获取用户下所有通道
     *
     * @param id 用户主键
     * @return 所有连接
     */
    public static List<ChannelContext> getChannelByUserId(String id) {
        SetWithLock<ChannelContext> userChannelContext = Tio.getByUserid(Im.get().getTioConfig(), id);
        if (userChannelContext == null) {
            return new ArrayList<>();
        }
        return convertChannel(userChannelContext);
    }


}
