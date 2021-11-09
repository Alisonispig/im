package org.example.config;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.CommandEnum;
import org.example.packets.bean.FriendInfo;
import org.example.packets.bean.Group;
import org.example.packets.bean.UnReadMessage;
import org.example.packets.bean.User;
import org.example.packets.handler.*;
import org.example.service.*;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.utils.lock.SetWithLock;
import org.tio.websocket.common.WsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Chat {

    private static final GroupService groupService = new GroupService();

    private static final UnReadMessageService unReadMessageService = new UnReadMessageService();

    private static final UserService userService = new UserService();

    private static final UserGroupService userGroupService = new UserGroupService();

    private static final FriendInfoService friendInfoService = new FriendInfoService();


    /**
     * 聊天消息
     *
     * @param chatRespBody 聊天消息体
     */
    public static void sendToGroup(ChatRespBody chatRespBody) {
        // 构建消息体
        User userInfo = userService.getUserInfo(chatRespBody.getSenderId());
        chatRespBody.setAvatar(userInfo.getAvatar());
        chatRespBody.setUsername(userInfo.getUsername());
        chatRespBody.setDeleted(false);
        chatRespBody.setSystem(false);

//        chatRespBody.setFiles(CollUtil.isEmpty(chatRespBody.getFiles()) ? null : chatRespBody.getFiles());

        log.info("目标数据：" + RespBody.success(CommandEnum.COMMAND_CHAT_REQ, chatRespBody));

        // 获取到群组内的所有用户
        List<User> groupUsers = userGroupService.getGroupUsers(chatRespBody.getRoomId());
        for (User groupUser : groupUsers) {

            // 给这个用户设置未读消息
            unReadMessageService.putUnReadMessage(groupUser.getId(), chatRespBody.getRoomId(), chatRespBody.getId());
            // 取出未读消息, 并设置未读数量
            List<UnReadMessage> unReadMessage = unReadMessageService.getUnReadMessage(groupUser.getId(), chatRespBody.getRoomId());
            chatRespBody.setUnreadCount(CollUtil.isEmpty(unReadMessage) ? 0 : unReadMessage.size());
            WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_CHAT_REQ, chatRespBody), Im.CHARSET);
            // 发送给在线用户
            List<ChannelContext> channelContexts = Im.getChannelByUserId(groupUser.getId());

            // 如果当前
            if (CollUtil.isNotEmpty(channelContexts)) {
                for (ChannelContext channelContext : channelContexts) {
                    Im.send(channelContext, wsResponse);
                }
            }
        }
    }

    /**
     * 用户状态变更消息
     *
     * @param userStatusBody 用户状态消息
     * @param channelContext 群组
     */
    public static void sendToGroup(UserStatusBody userStatusBody, ChannelContext channelContext) {
        sendToGroup(userStatusBody, channelContext, false);
    }

    /**
     * 用户状态变更消息
     *
     * @param userStatusBody 用户状态消息
     * @param channelContext 群组
     */
    public static void sendToGroup(UserStatusBody userStatusBody, ChannelContext channelContext, Boolean sendAll) {
        WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_USER_STATUS_RESP, userStatusBody), Im.CHARSET);
        SetWithLock<ChannelContext> users = Tio.getByGroup(Im.get().getTioConfig(), userStatusBody.getGroup().getRoomId());
        List<ChannelContext> channelContexts = Im.convertChannel(users);
        User nowUser = Im.getUser(channelContext, false);
        for (ChannelContext context : channelContexts) {
            User user = Im.getUser(context);
            if (user.getId().equals(nowUser.getId()) && !sendAll) {
                continue;
            }
            Im.send(context, wsResponse);
        }
    }

    /**
     * 群组表情回复
     *
     * @param messageReactionRespBody 表情回复
     */
    public static void sendToGroup(MessageReactionRespBody messageReactionRespBody) {
        WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_SEND_MESSAGE_REACTION_RESP, messageReactionRespBody), Im.CHARSET);
        SetWithLock<ChannelContext> users = Tio.getByGroup(Im.get().getTioConfig(), messageReactionRespBody.getRoomId());
        List<ChannelContext> channelContexts = Im.convertChannel(users);
        for (ChannelContext context : channelContexts) {
            Im.send(context, wsResponse);
        }
    }


    public static void sendToGroup(JoinGroupNotifyBody joinGroupNotifyBody) {

        List<User> groupUsers = userGroupService.getGroupUsers(joinGroupNotifyBody.getGroup().getRoomId());
        joinGroupNotifyBody.getGroup().setUsers(groupUsers);

        if (CollUtil.isEmpty(joinGroupNotifyBody.getUsers())) {
            return;
        }
        SetWithLock<ChannelContext> users = Tio.getByGroup(Im.get().tioConfig, joinGroupNotifyBody.getGroup().getRoomId());
        if (users == null) {
            return;
        }
        List<User> userList = new ArrayList<>();
        joinGroupNotifyBody.getUsers().forEach(x -> userList.add(userService.getUserInfo(x.getId())));

        List<ChannelContext> channelContexts = Im.convertChannel(users);
        String collect = userList.stream().map(User::getUsername).collect(Collectors.joining(StrUtil.COMMA));

        joinGroupNotifyBody.setMessage(collect + ",已加入群聊!");

        WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_JOIN_GROUP_NOTIFY_RESP, joinGroupNotifyBody), Im.CHARSET);
        for (ChannelContext context : channelContexts) {
            Im.send(context, wsResponse);
        }
    }


    public static void resetGroup(Group group, String userId) {
        log.info("{}", group);
        if (!group.getIsFriend()) {
            return;
        }
        // 获取好友信息
        FriendInfo friendInfo = friendInfoService.getFriendInfo(group.getRoomId(), userId);
        if (friendInfo != null) {
            User friend = userService.getUserInfo(friendInfo.getFriendId());
            group.setRoomName(friendInfo.getRemark());

            group.setFriendId(friend.getId());
            group.setAvatar(friend.getAvatar());
            if (StrUtil.isBlank(group.getRoomName())) {
                group.setRoomName(friend.getUsername());
            }
        }
        log.info("{}", group);
    }

}
