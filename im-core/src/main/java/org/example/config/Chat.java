package org.example.config;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.CommandEnum;
import org.example.packets.bean.FriendInfo;
import org.example.packets.bean.Group;
import org.example.packets.bean.UnReadMessage;
import org.example.packets.bean.User;
import org.example.packets.handler.message.ChatRespBody;
import org.example.packets.handler.system.RespBody;
import org.example.packets.handler.user.UserStatusBody;
import org.example.packets.handler.room.JoinGroupNotifyBody;
import org.example.service.*;
import org.tio.core.ChannelContext;
import org.tio.websocket.common.WsResponse;

import java.util.List;

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

        chatRespBody.setSystem(chatRespBody.getSystem());
        if (ObjectUtil.defaultIfNull(chatRespBody.getSystem(), false)) {
            chatRespBody.setSenderId("");
        }

//        chatRespBody.setFiles(CollUtil.isEmpty(chatRespBody.getFiles()) ? null : chatRespBody.getFiles());

        log.info("目标数据：" + RespBody.success(CommandEnum.COMMAND_CHAT_REQ, chatRespBody));

        // 获取到群组内的所有用户
        List<User> groupUsers = userGroupService.getGroupUsers(chatRespBody.getRoomId());
        for (User groupUser : groupUsers) {
            if(!groupUser.getId().equals(chatRespBody.getSenderId())){
                // 给这个用户设置未读消息
                unReadMessageService.putUnReadMessage(groupUser.getId(), chatRespBody.getRoomId(), chatRespBody.getId());
            }

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
        List<ChannelContext> channelContexts = Im.getByGroup(userStatusBody.getGroup().getRoomId());

        User nowUser = Im.getUser(channelContext, false);

        for (ChannelContext context : channelContexts) {
            User user = Im.getUser(context);
            if (user.getId().equals(nowUser.getId()) && !sendAll) {
                continue;
            }
            Im.send(context, wsResponse);
        }
    }

    public static void sendToGroup(JoinGroupNotifyBody joinGroupNotifyBody) {
        // 判空, 说明有异常
        if (CollUtil.isEmpty(joinGroupNotifyBody.getUsers())) {
            return;
        }

        // 此处分为两种情况,或者用户,或者群组. 群组直接群发
        if (joinGroupNotifyBody.getGroup().getIsFriend()) {
            for (User user : joinGroupNotifyBody.getUsers()) {
                List<ChannelContext> channelContexts = Im.getChannelByUserId(user.getId());
                Chat.resetGroup(joinGroupNotifyBody.getGroup(), user.getId());
                for (ChannelContext channelContext : channelContexts) {
                    WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_JOIN_GROUP_NOTIFY_RESP, joinGroupNotifyBody), Im.CHARSET);
                    Im.send(channelContext, wsResponse);
                }
            }
            return;
        }

        WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_JOIN_GROUP_NOTIFY_RESP, joinGroupNotifyBody), Im.CHARSET);
        Im.sendToGroup(joinGroupNotifyBody.getGroup().getRoomId(), wsResponse);

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
