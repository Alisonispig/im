package org.example.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.CommandEnum;
import org.example.enums.KeyEnum;
import org.example.packets.*;
import org.example.packets.handler.ChatRespBody;
import org.example.packets.handler.JoinGroupNotifyBody;
import org.example.packets.handler.RespBody;
import org.example.packets.handler.UserStatusBody;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;
import org.tio.utils.lock.SetWithLock;
import org.tio.websocket.common.WsResponse;

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
     * 绑定用户(如果配置了回调函数执行回调)
     *
     * @param channelContext IM通道上下文
     * @param user           绑定用户信息
     */
    public static void bindUser(ChannelContext channelContext, User user) {
        if (Objects.isNull(user) || StrUtil.isBlank(user.get_id())) {
            log.error("user or userId is null");
            return;
        }
        String userId = user.get_id();
        ImSessionContext imSessionContext = (ImSessionContext) channelContext.get(KeyEnum.IM_CHANNEL_SESSION_CONTEXT_KEY.getKey());
        Tio.bindUser(channelContext, userId);
        SetWithLock<ChannelContext> channelContextSetWithLock = Tio.getByUserid(channelContext.tioConfig, userId);
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

    public static void sendToGroup(JoinGroupNotifyBody joinGroupNotifyBody, ChannelContext channelContext) {
        WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_JOIN_GROUP_NOTIFY_RESP, joinGroupNotifyBody), CHARSET);
        SetWithLock<ChannelContext> users = Tio.getByGroup(channelContext.tioConfig, joinGroupNotifyBody.getRoomId());
        if (users == null) {
            return;
        }
        List<ChannelContext> channelContexts = convertChannel(users);
        for (ChannelContext context : channelContexts) {
            send(context, wsResponse);
        }
    }

    /**
     * 聊天消息
     *
     * @param chatRespBody   聊天消息体
     * @param channelContext 用户上下文
     */
    public static void sendToGroup(ChatRespBody chatRespBody, ChannelContext channelContext) {
        // 构建消息体
        User userInfo = get().messageHelper.getUserInfo(chatRespBody.getSenderId());

        chatRespBody.setAvatar(userInfo.getAvatar());
        chatRespBody.setUsername(userInfo.getUsername());
        Date date = new Date();
        chatRespBody.setDate(DateUtil.formatDate(date));
        chatRespBody.setTimestamp(DateUtil.formatTime(date));
        chatRespBody.setDeleted(false);
        chatRespBody.setSystem(false);
        log.info("目标数据：" + RespBody.success(CommandEnum.COMMAND_CHAT_REQ, chatRespBody));

        WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_CHAT_REQ, chatRespBody), CHARSET);
        SetWithLock<ChannelContext> users = Tio.getByGroup(channelContext.tioConfig, chatRespBody.getRoomId());
        List<ChannelContext> channelContexts = convertChannel(users);
        for (ChannelContext context : channelContexts) {
            send(context, wsResponse);
        }
    }

    /**
     * 用户状态变更消息
     *
     * @param userStatusBody 用户状态消息
     * @param channelContext 群组
     */
    public static void sendToGroup(UserStatusBody userStatusBody, ChannelContext channelContext) {
        WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_USER_STATUS_RESP, userStatusBody), CHARSET);
        SetWithLock<ChannelContext> users = Tio.getByGroup(channelContext.tioConfig, userStatusBody.getGroup().getRoomId());
        List<ChannelContext> channelContexts = convertChannel(users);
        for (ChannelContext context : channelContexts) {
            send(context, wsResponse);
        }
    }

    private static List<ChannelContext> convertChannel(SetWithLock<ChannelContext> channelContextSetWithLock) {
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
        if (isAllInfo) {
            return user;
        }
        return user.clone();
    }


}
