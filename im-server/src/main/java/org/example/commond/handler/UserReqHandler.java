package org.example.commond.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.commond.AbstractCmdHandler;
import org.example.config.Chat;
import org.example.config.Im;
import org.example.config.ImConfig;
import org.example.enums.CommandEnum;
import org.example.packets.bean.Group;
import org.example.packets.bean.Message;
import org.example.packets.bean.UnReadMessage;
import org.example.packets.bean.User;
import org.example.packets.handler.ChatRespBody;
import org.example.packets.handler.RespBody;
import org.example.packets.handler.UserReqBody;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserReqHandler extends AbstractCmdHandler {

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_GET_USER_REQ;
    }

    @Override
    public WsResponse handler(Packet packet, ChannelContext channelContext) {

        WsRequest wsRequest = (WsRequest) packet;
        UserReqBody userReqBody = JSON.parseObject(wsRequest.getWsBodyText(), UserReqBody.class);
        log.info("userReqBody : {}", userReqBody);
        User user = Im.getUser(channelContext);

        List<Group> chats = new ArrayList<>();

        for (Group group : user.getGroups()) {
            // 组织群组用户信息
            String roomId = group.getRoomId();
            List<User> groupUsers = userGroupService.getGroupUsers(roomId);
            group.setUsers(groupUsers);

            Chat.resetGroup(group, user.getId());
        }
        user.setChats(chats);

        WsResponse response = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_GET_USER_RESP, user), ImConfig.CHARSET);
        Im.send(channelContext, response);

        for (Group group : user.getGroups()) {
            // 获取到最后一条消息未读消息,并且发重新发送
            List<UnReadMessage> unReadMessages = unReadMessageService.getUnReadMessage(user.getId(), group.getRoomId());
            if (CollUtil.isNotEmpty(unReadMessages)) {
                UnReadMessage unReadMessage = unReadMessages.get(unReadMessages.size() - 1);
                Message message = messageService.getMessage(unReadMessage.getMessageId());
                ChatRespBody chatRespBody = BeanUtil.copyProperties(message, ChatRespBody.class);
                User userInfo = userService.getUserInfo(chatRespBody.getSenderId());
                chatRespBody.setAvatar(userInfo.getAvatar());
                chatRespBody.setUsername(userInfo.getUsername());
                chatRespBody.setUnreadCount(CollUtil.isEmpty(unReadMessages) ? 0 : unReadMessages.size());
                WsResponse wsResponse = WsResponse.fromText(RespBody.success(CommandEnum.COMMAND_CHAT_REQ, chatRespBody), Im.CHARSET);
                Im.send(channelContext, wsResponse);
            }
        }

        return null;
    }
}
