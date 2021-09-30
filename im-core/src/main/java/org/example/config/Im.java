package org.example.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.CommandEnum;
import org.example.enums.KeyEnum;
import org.example.packets.ChatRepBody;
import org.example.packets.Group;
import org.example.packets.RespBody;
import org.example.packets.User;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;
import org.tio.utils.lock.SetWithLock;
import org.tio.websocket.common.WsResponse;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class Im extends ImConfig {

    public static boolean bSend(ChannelContext channelContext, Packet packet) {
        if (channelContext == null || packet == null) {
            return false;
        }
        return Tio.bSend(channelContext, packet);
    }

    public static boolean send(ChannelContext channelContext, Packet packet) {
        if (channelContext == null || packet == null) {
            return false;
        }
        return Tio.send(channelContext, packet);
    }

    public static boolean bindGroup(ChannelContext channelContext, Group group) {
        String groupId = group.getRoomId();
        Tio.bindGroup(channelContext, groupId);

        return true;
    }

    /**
     * 绑定用户(如果配置了回调函数执行回调)
     *
     * @param channelContext IM通道上下文
     * @param user           绑定用户信息
     */
    public static boolean bindUser(ChannelContext channelContext, User user) {
        if (Objects.isNull(user) || StrUtil.isBlank(user.get_id())) {
            log.error("user or userId is null");
            return false;
        }
        String userId = user.get_id();
        ImSessionContext imSessionContext = (ImSessionContext) channelContext.get(KeyEnum.IM_CHANNEL_SESSION_CONTEXT_KEY.getKey());
        Tio.bindUser(channelContext, userId);
        SetWithLock<ChannelContext> channelContextSetWithLock = Tio.getByUserid(channelContext.tioConfig, userId);
        ReentrantReadWriteLock.ReadLock lock = channelContextSetWithLock.getLock().readLock();
        try {
            lock.lock();
            if (CollUtil.isEmpty(channelContextSetWithLock.getObj())) {
                return false;
            }
            imSessionContext.getImClientNode().setUser(user);
            get().imUserListener.onAfterBind(channelContext, user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            lock.unlock();
        }
        return true;
    }

    public static void sendToGroup(ChatRepBody chatRepBody, ChannelContext channelContext) {
        // 构建消息体
        User userInfo = get().messageHelper.getUserInfo(chatRepBody.getSenderId());

        chatRepBody.setAvatar(userInfo.getAvatar());
        chatRepBody.setUsername(userInfo.getUsername());
        Date date = new Date();
        chatRepBody.setDate(DateUtil.formatDate(date));
        chatRepBody.setTimestamp(DateUtil.formatTime(date));
        chatRepBody.setDeleted(false);
        chatRepBody.setSystem(false);

        WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_CHAT_REQ, chatRepBody), CHARSET);
        SetWithLock<ChannelContext> users = Tio.getByGroup(channelContext.tioConfig, chatRepBody.getRoomId());
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
        return null;
    }
}
